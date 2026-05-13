/**
 * Vito Cloud Functions
 * 
 * Implements:
 * - QR token authentication system
 * - Payment processing (Stripe)
 * - Payouts (Stripe Connect)
 * - User management
 * - Chat cleanup
 */

const functions = require('firebase-functions');
const admin = require('firebase-admin');
const stripe = require('stripe');

admin.initializeApp();
const db = admin.firestore();
const auth = admin.auth();

// Configure Stripe from environment
const getStripe = () => {
  const secret = functions.config().stripe?.secret || process.env.STRIPE_SECRET_KEY;
  if (!secret) {
    console.warn('Stripe secret key not configured');
    return null;
  }
  return stripe(secret);
};

// ============================================================================
// 1. QR TOKEN GENERATION (Admin only)
// ============================================================================

/**
 * Generate QR token for user registration
 * Input: { role: 'client'|'driver', expiryHours: number, deviceId?: string }
 * Output: { token: string, expiresAt: Timestamp }
 */
exports.generateQrToken = functions.https.onCall(async (data, context) => {
  // Verify admin authentication
  if (!context.auth) {
    throw new functions.https.HttpsError('unauthenticated', 'Must be authenticated');
  }
  
  const userRecord = await db.collection('users_v2').doc(context.auth.uid).get();
  if (!userRecord.exists || userRecord.data().role !== 'admin') {
    throw new functions.https.HttpsError('permission-denied', 'Admin only');
  }
  
  const { role = 'client', expiryHours = 24 } = data;
  
  if (!['client', 'driver'].includes(role)) {
    throw new functions.https.HttpsError('invalid-argument', 'Invalid role');
  }
  
  // Generate UUID token
  const token = require('crypto').randomUUID();
  const expiresAt = admin.firestore.Timestamp.fromDate(
    new Date(Date.now() + expiryHours * 60 * 60 * 1000)
  );
  
  // Store token
  await db.collection('qr_tokens').doc(token).set({
    token,
    role,
    createdBy: context.auth.uid,
    createdAt: admin.firestore.Timestamp.now(),
    expiresAt,
    used: false,
    deviceId: null
  });
  
  return { token, expiresAt: expiresAt.toDate().toISOString() };
});

// ============================================================================
// 2. CUSTOM AUTHENTICATION VIA QR TOKEN
// ============================================================================

/**
 * Register/sign in with QR token
 * Input: { token: string, deviceId: string }
 * Output: { customToken: string, uid: string, role: string }
 */
exports.registerWithToken = functions.https.onCall(async (data, context) => {
  const { token, deviceId } = data;
  
  if (!token || !deviceId) {
    throw new functions.https.HttpsError('invalid-argument', 'Missing token or deviceId');
  }
  
  // Get token document
  const tokenDoc = await db.collection('qr_tokens').doc(token).get();
  
  if (!tokenDoc.exists) {
    throw new functions.https.HttpsError('not-found', 'Invalid token');
  }
  
  const tokenData = tokenDoc.data();
  
  // Check expiry
  if (tokenData.expiresAt.toDate() < new Date()) {
    throw new functions.https.HttpsError('deadline-exceeded', 'Token expired');
  }
  
  // Check if already used
  if (tokenData.used && tokenData.deviceId) {
    throw new functions.https.HttpsError('already-exists', 'Token already used');
  }
  
  // Generate new user UID
  const uid = require('crypto').randomUUID();
  const role = tokenData.role;
  
  // Mark token as used
  await db.collection('qr_tokens').doc(token).update({
    used: true,
    deviceId,
    usedAt: admin.firestore.Timestamp.now()
  });
  
  // Create user document
  await db.collection('users_v2').doc(uid).set({
    uid,
    alias: `User_${Math.floor(Math.random() * 10000)}`,
    photoUrl: null,
    role,
    deviceId,
    walletBalance: 0,
    stripeCustomerId: null,
    isVerified: role === 'driver' ? false : true,
    documents: role === 'driver' ? {
      drivingLicence: null,
      registration: null,
      insurance: null,
      status: 'pending'
    } : null,
    vehicle: role === 'driver' ? null : null,
    bankAccountId: null,
    fcmToken: null,
    accountStatus: 'active',
    createdAt: admin.firestore.Timestamp.now()
  });
  
  // Generate Firebase custom token
  const customToken = await auth.createCustomToken(uid, { role });
  
  return { customToken, uid, role };
});

// ============================================================================
// 3. PAYMENT PROCESSING
// ============================================================================

/**
 * Create payment intent for wallet top-up or ride payment
 * Input: { amount: number (cents), currency: string, customerId?: string }
 * Output: { clientSecret: string, paymentIntentId: string }
 */
exports.createPaymentIntent = functions.https.onCall(async (data, context) => {
  if (!context.auth) {
    throw new functions.https.HttpsError('unauthenticated', 'Must be authenticated');
  }
  
  const { amount, currency = 'usd', paymentMethodId } = data;
  
  if (!amount || amount < 50) {
    throw new functions.https.HttpsError('invalid-argument', 'Minimum amount is $0.50');
  }
  
  const stripe = getStripe();
  if (!stripe) {
    throw new functions.https.HttpsError('failed-precondition', 'Stripe not configured');
  }
  
  const userDoc = await db.collection('users_v2').doc(context.auth.uid).get();
  const userData = userDoc.data();
  
  // Create or get Stripe customer
  let customerId = userData.stripeCustomerId;
  
  if (!customerId) {
    const customer = await stripe.customers.create({
      metadata: { uid: context.auth.uid }
    });
    customerId = customer.id;
    await userDoc.ref.update({ stripeCustomerId: customerId });
  }
  
  // Create payment intent
  const paymentIntent = await stripe.paymentIntents.create({
    amount,
    currency,
    customer: customerId,
    metadata: {
      userId: context.auth.uid,
      type: paymentMethodId ? 'payment' : 'topup'
    },
    off_session: !!paymentMethodId
  });
  
  return {
    clientSecret: paymentIntent.client_secret,
    paymentIntentId: paymentIntent.id
  };
});

/**
 * Confirm payment and add to wallet
 */
exports.confirmPayment = functions.https.onCall(async (data, context) => {
  if (!context.auth) {
    throw new functions.https.HttpsError('unauthenticated', 'Must be authenticated');
  }
  
  const { paymentIntentId, amount } = data;
  
  if (!paymentIntentId || !amount) {
    throw new functions.https.HttpsError('invalid-argument', 'Missing required fields');
  }
  
  const stripe = getStripe();
  if (!stripe) {
    throw new functions.https.HttpsError('failed-precondition', 'Stripe not configured');
  }
  
  // Verify payment intent
  const paymentIntent = await stripe.paymentIntents.retrieve(paymentIntentId);
  
  if (paymentIntent.status !== 'succeeded') {
    throw new functions.https.HttpsError('failed-precondition', 'Payment not successful');
  }
  
  // Add to wallet
  const userRef = db.collection('users_v2').doc(context.auth.uid);
  await userRef.update({
    walletBalance: admin.firestore.FieldValue.increment(amount)
  });
  
  return { success: true, newBalance: paymentIntent.amount };
});

// ============================================================================
// 4. DRIVER PAYOUT (Stripe Connect)
// ============================================================================

/**
 * Request payout from driver wallet
 * Input: { amount: number (cents) }
 * Output: { transferId: string }
 */
exports.requestPayout = functions.https.onCall(async (data, context) => {
  if (!context.auth) {
    throw new functions.https.HttpsError('unauthenticated', 'Must be authenticated');
  }
  
  const { amount } = data;
  
  if (!amount || amount < 1000) {
    throw new functions.https.HttpsError('invalid-argument', 'Minimum payout is $10.00');
  }
  
  // Verify driver
  const userDoc = await db.collection('users_v2').doc(context.auth.uid).get();
  const userData = userDoc.data();
  
  if (userData.role !== 'driver' || !userData.isVerified) {
    throw new functions.https.HttpsError('permission-denied', 'Driver not verified');
  }
  
  if (userData.walletBalance < amount) {
    throw new functions.https.HttpsError('failed-precondition', 'Insufficient balance');
  }
  
  if (!userData.bankAccountId) {
    throw new functions.https.HttpsError('failed-precondition', 'No bank account linked');
  }
  
  const stripe = getStripe();
  if (!stripe) {
    throw new functions.https.HttpsError('failed-precondition', 'Stripe not configured');
  }
  
  // Create transfer to driver's connected account
  const transfer = await stripe.transfers.create({
    amount,
    currency: 'usd',
    destination: userData.bankAccountId,
    metadata: {
      driverId: context.auth.uid,
      type: 'payout'
    }
  });
  
  // Deduct from wallet
  await userDoc.ref.update({
    walletBalance: admin.firestore.FieldValue.increment(-amount)
  });
  
  return { transferId: transfer.id };
});

// ============================================================================
// 5. RIDE FARE ESTIMATION
// ============================================================================

/**
 * Estimate fare for a ride
 * Input: { pickupLat, pickupLng, dropoffLat, dropoffLng, vehicleCategory }
 * Output: { estimatedFare: number, duration: number, distance: number }
 */
exports.estimateFare = functions.https.onCall(async (data, context) => {
  const { pickupLat, pickupLng, dropoffLat, dropoffLng, vehicleCategory = 'standard' } = data;
  
  if (!pickupLat || !pickupLng || !dropoffLat || !dropoffLng) {
    throw new functions.https.HttpsError('invalid-argument', 'Missing location data');
  }
  
  // Base fare by category
  const baseFares = { standard: 500, premium: 800, van: 1200 };
  const baseFare = baseFares[vehicleCategory] || 500;
  
  // Calculate distance (Haversine formula approximation)
  const R = 6371; // Earth's radius in km
  const dLat = Math.abs(dropoffLat - pickupLat) * Math.PI / 180;
  const dLng = Math.abs(dropoffLng - pickupLng) * Math.PI / 180;
  const a = Math.sin(dLat/2) * Math.sin(dLat/2) +
    Math.cos(pickupLat * Math.PI / 180) * Math.cos(dropoffLat * Math.PI / 180) *
    Math.sin(dLng/2) * Math.sin(dLng/2);
  const c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1-a));
  const distance = R * c; // km
  
  // Calculate fare (base + per km)
  const perKmRate = 150; // cents per km
  const estimatedFare = Math.round(baseFare + (distance * perKmRate));
  const duration = Math.round(distance * 3 * 60); // rough estimate: 3 min per km
  
  return { estimatedFare, duration, distance: Math.round(distance * 100) / 100 };
});

// ============================================================================
// 6. REVENUE TRACKING (For admin)
// ============================================================================

/**
 * Get revenue stats
 * Input: { startDate, endDate }
 * Output: { totalRevenue, rides, averageFare }
 */
exports.getRevenueStats = functions.https.onCall(async (data, context) => {
  if (!context.auth) {
    throw new functions.https.HttpsError('unauthenticated', 'Must be authenticated');
  }
  
  const userDoc = await db.collection('users_v2').doc(context.auth.uid).get();
  if (!userDoc.exists || userDoc.data().role !== 'admin') {
    throw new functions.https.HttpsError('permission-denied', 'Admin only');
  }
  
  const { startDate, endDate } = data;
  const start = startDate ? new Date(startDate) : new Date(Date.now() - 30 * 24 * 60 * 60 * 1000);
  const end = endDate ? new Date(endDate) : new Date();
  
  // Query completed rides
  const ridesSnapshot = await db.collection('rides')
    .where('completedAt', '>=', admin.firestore.Timestamp.fromDate(start))
    .where('completedAt', '<=', admin.firestore.Timestamp.fromDate(end))
    .where('status', '==', 'completed')
    .get();
  
  let totalRevenue = 0;
  for (const doc of ridesSnapshot.docs) {
    totalRevenue += doc.data().fare || 0;
  }
  
  return {
    totalRevenue,
    rides: ridesSnapshot.size,
    averageFare: ridesSnapshot.size > 0 ? Math.round(totalRevenue / ridesSnapshot.size) : 0
  };
});

// ============================================================================
// 7. SCHEDULED FUNCTIONS
// ============================================================================

/**
 * Cleanup old chat messages (runs every 24 hours)
 */
exports.cleanupChats = functions.pubsub.schedule('every 24 hours').onRun(async (context) => {
  const cutoff = admin.firestore.Timestamp.fromDate(
    new Date(Date.now() - 24 * 60 * 60 * 1000)
  );
  
  // Get chats older than 24 hours from completed trips
  const oldChats = await db.collection('chats')
    .where('lastMessageAt', '<', cutoff)
    .where('status', '==', 'completed')
    .get();
  
  const batch = db.batch();
  for (const chatDoc of oldChats.docs) {
    batch.delete(chatDoc.ref);
  }
  
  await batch.commit();
  console.log(`Deleted ${oldChats.size} old chat documents`);
});

// ============================================================================
// 8. FCM NOTIFICATIONS
// ============================================================================

/**
 * Send push notification to user
 */
exports.sendNotification = functions.https.onCall(async (data, context) => {
  if (!context.auth) {
    throw new functions.https.HttpsError('unauthenticated', 'Must be authenticated');
  }
  
  const { userId, title, body, data: payload } = data;
  
  const userDoc = await db.collection('users_v2').doc(userId).get();
  const userData = userDoc.data();
  
  if (!userData.fcmToken) {
    return { success: false, reason: 'No FCM token' };
  }
  
  const message = {
    notification: { title, body },
    data: payload || {},
    token: userData.fcmToken
  };
  
  await admin.messaging().send(message);
  return { success: true };
});

// Export firestore rules for deployment
module.exports.getFirestoreRules = () => `
rules_version = '2';
service cloud.firestore {
  match /databases/{database}/documents {
    
    // QR tokens - admin only
    match /qr_tokens/{token} {
      allow read, write: if request.auth != null && 
        get(/databases/$(database)/documents/users_v2/$(request.auth.uid)).data.role == 'admin';
    }
    
    // Users - own profile only
    match /users_v2/{userId} {
      allow read: if request.auth != null && request.auth.uid == userId;
      allow write: if request.auth != null && request.auth.uid == userId;
    }
    
    // Rides - participant access
    match /rides/{rideId} {
      allow read: if request.auth != null && 
        (resource.data.clientId == request.auth.uid || 
         resource.data.driverId == request.auth.uid ||
         get(/databases/$(database)/documents/users_v2/$(request.auth.uid)).data.role == 'admin');
      allow create: if request.auth != null;
      allow update: if request.auth != null &&
        (resource.data.clientId == request.auth.uid || 
         resource.data.driverId == request.auth.uid ||
         get(/databases/$(database)/documents/users_v2/$(request.auth.uid)).data.role == 'admin');
    }
    
    // Driver locations - public read, owner write
    match /driver_locations/{driverId} {
      allow read: if true;
      allow write: if request.auth != null && request.auth.uid == driverId;
    }
    
    // Chats - participant access
    match /chats/{chatId} {
      allow read: if request.auth != null && 
        (resource.data.participants[request.auth.uid] == true ||
         get(/databases/$(database)/documents/users_v2/$(request.auth.uid)).data.role == 'admin');
      allow write: if request.auth != null && resource.data.participants[request.auth.uid] == true;
    }
    
    // Mart orders - participant access
    match /mart_orders/{orderId} {
      allow read: if request.auth != null && 
        (resource.data.clientId == request.auth.uid ||
         resource.data.driverId == request.auth.uid ||
         get(/databases/$(database)/documents/users_v2/$(request.auth.uid)).data.role == 'admin');
      allow create: if request.auth != null;
      allow update: if request.auth != null &&
        (resource.data.clientId == request.auth.uid || 
         resource.data.driverId == request.auth.uid ||
         get(/databases/$(database)/documents/users_v2/$(request.auth.uid)).data.role == 'admin');
    }
  }
}
`;

module.exports.getStorageRules = () => `
rules_version = '2';
service firebase.storage {
  match /b/{bucket}/o {
    match /users/{userId}/{allPaths=**} {
      allow read: if request.auth != null && request.auth.uid == userId;
      allow write: if request.auth != null && request.auth.uid == userId;
    }
    
    match /driver_documents/{driverId}/{allPaths=**} {
      allow read: if request.auth != null && 
        (request.auth.uid == driverId ||
         get(/databases/(default)/documents/users_v2/$(request.auth.uid)).data.role == 'admin');
      allow write: if request.auth != null && request.auth.uid == driverId;
    }
    
    match /chat_images/{chatId}/{allPaths=**} {
      allow read: if request.auth != null;
      allow write: if request.auth != null;
    }
    
    match /proof_delivery/{orderId}/{allPaths=**} {
      allow read: if request.auth != null;
      allow write: if request.auth != null && request.auth.uid == resource.owner;
    }
  }
}
`;