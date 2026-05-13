/**
 * Firebase Cloud Functions for Vito
 * Deploy with: firebase deploy --only functions
 * 
 * Prerequisites:
 * - Enable Cloud Functions: firebase functions:config:set stripe.secret_key=sk_xxx
 * - Enable Firestore: firebase firestore:enable
 */

const functions = require('firebase-functions');
const admin = require('firebase-admin');
const stripe = require('stripe')(functions.config().stripe.secret_key);

admin.initializeApp();

// ============================================================================
// QR Token Generation
// ============================================================================

/**
 * Generate custom auth token for QR registration
 * Accessible by Admin only
 */
exports.generateCustomToken = functions.https.onCall(async (data, context) => {
  // Admin check would go here
  const { token, role } = data;
  
  if (!token || !role) {
    throw new functions.https.HttpsError('invalid-argument', 'Token and role required');
  }
  
  // Verify token exists in Firestore
  const tokenDoc = await admin.firestore().collection('qr_tokens').doc(token).get();
  
  if (!tokenDoc.exists) {
    throw new functions.https.HttpsError('not-found', 'Invalid token');
  }
  
  const tokenData = tokenDoc.data();
  
  // Check expiry
  if (tokenData.expiresAt && tokenData.expiresAt.toDate() < new Date()) {
    throw new functions.https.HttpsError('failed-precondition', 'Token expired');
  }
  
  // Check if already used (for single-use tokens)
  if (tokenData.singleUse && tokenData.used) {
    throw new functions.https.HttpsError('failed-precondition', 'Token already used');
  }
  
  // Mark token as used
  await tokenDoc.ref.update({ used: true, usedAt: admin.firestore.FieldValue.serverTimestamp() });
  
  // Create custom token
  const uid = `qr_${token}_${Date.now()}`;
  const customToken = await admin.auth().createCustomToken(uid, { role });
  
  return { customToken, uid };
});

/**
 * Create PaymentIntent for Stripe
 */
exports.createPaymentIntent = functions.https.onCall(async (data, context) => {
  const { amount, currency = 'usd', customerId } = data;
  
  if (!amount) {
    throw new functions.https.HttpsError('invalid-argument', 'Amount required');
  }
  
  // Verify auth
  if (!context.auth) {
    throw new functions.https.HttpsError('unauthenticated', 'Must be authenticated');
  }
  
  const paymentIntent = await stripe.paymentIntents.create({
    amount: Math.round(amount * 100), // Convert to cents
    currency,
    customer: customerId,
    metadata: {
      userId: context.auth.uid
    }
  });
  
  return {
    clientSecret: paymentIntent.client_secret
  };
});

/**
 * Request payout (Driver)
 */
exports.requestPayout = functions.https.onCall(async (data, context) => {
  const { amount, bankAccountId } = data;
  
  if (!amount || !bankAccountId) {
    throw new functions.https.HttpsError('invalid-argument', 'Amount and bank account required');
  }
  
  // Verify driver
  if (!context.auth) {
    throw new functions.https.HttpsError('unauthenticated', 'Must be authenticated');
  }
  
  // Create payout record (would integrate with Stripe Connect)
  const payout = await admin.firestore().collection('payouts').add({
    userId: context.auth.uid,
    amount,
    bankAccountId,
    status: 'pending',
    createdAt: admin.firestore.FieldValue.serverTimestamp()
  });
  
  return { payoutId: payout.id };
});

/**
 * Save driver location (real-time)
 */
exports.updateDriverLocation = functions.https.onCall(async (data, context) => {
  const { lat, lng, status = 'available' } = data;
  
  if (!lat || !lng) {
    throw new functions.https.HttpsError('invalid-argument', 'Lat and lng required');
  }
  
  if (!context.auth) {
    throw new functions.https.HttpsError('unauthenticated', 'Must be authenticated');
  }
  
  await admin.firestore().collection('driver_locations').doc(context.auth.uid).set({
    lat,
    lng,
    status,
    updatedAt: admin.firestore.FieldValue.serverTimestamp()
  });
  
  return { success: true };
});

/**
 * Send push notification (Chat)
 */
exports.sendChatNotification = functions.firestore
  .document('chats/{chatId}/messages/{messageId}')
  .onCreate(async (snapshot, context) => {
    const message = snapshot.data();
    const { chatId } = context.params;
    
    // Get chat participants
    const chatDoc = await admin.firestore().collection('chats').doc(chatId).get();
    const chat = chatDoc.data();
    
    // Determine recipient (not sender)
    const recipientId = chat.participants.find(id => id !== message.senderId);
    
    // Get recipient's FCM token
    const userDoc = await admin.firestore().collection('users_v2').doc(recipientId).get();
    const user = userDoc.data();
    
    if (user.fcmToken) {
      await admin.messaging().send({
        token: user.fcmToken,
        notification: {
          title: message.senderName || 'Driver',
          body: message.text || 'Sent an image'
        },
        data: {
          chatId,
          type: 'chat'
        }
      });
    }
    
    return null;
  });

/**
 * Scheduled cleanup - expire old QR tokens
 */
exports.cleanupExpiredTokens = functions.pubsub
  .schedule('every 24 hours')
  .onRun(async () => {
    const expired = await admin.firestore().collection('qr_tokens')
      .where('expiresAt', '<', admin.firestore.Timestamp.now())
      .get();
    
    const batch = admin.firestore().batch();
    expired.docs.forEach(doc => batch.update(doc.ref, { status: 'expired' }));
    
    await batch.commit();
    
    return null;
  });