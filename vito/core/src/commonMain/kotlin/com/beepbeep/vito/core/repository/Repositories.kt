/**
 * Vito Authentication Repository
 * Handles QR token-based authentication
 */
package com.beepbeep.vito.core.repository

import com.beepbeep.vito.core.model.VitoUser
import kotlinx.coroutines.flow.Flow

interface AuthRepository {
    /** Current authenticated user */
    val currentUser: Flow<VitoUser?>
    
    /** Whether user is authenticated */
    val isAuthenticated: Flow<Boolean>
    
    /**
     * Register/sign in with QR token
     * @param token QR token from admin
     * @param deviceId Unique device identifier (ANDROID_ID)
     * @return Result with user or error
     */
    suspend fun registerWithToken(token: String, deviceId: String): Result<VitoUser>
    
    /**
     * Sign out
     */
    suspend fun signOut()
    
    /**
     * Update user profile
     */
    suspend fun updateProfile(user: VitoUser): Result<Unit>
    
    /**
     * Update FCM token
     */
    suspend fun updateFcmToken(token: String): Result<Unit>
}

interface UserRepository {
    /**
     * Get user by ID
     */
    suspend fun getUser(userId: String): Result<VitoUser?>
    
    /**
     * Get current user
     */
    fun getCurrentUser(): Flow<VitoUser?>
    
    /**
     * Update user
     */
    suspend fun updateUser(user: VitoUser): Result<Unit>
    
    /**
     * Update wallet balance
     */
    suspend fun updateWalletBalance(userId: String, amount: Int): Result<Unit>
    
    /**
     * Delete account (anonymize data)
     */
    suspend fun deleteAccount(userId: String): Result<Unit>
    
    /**
     * Get all users (admin)
     */
    fun getAllUsers(): Flow<List<VitoUser>>
    
    /**
     * Suspend user (admin)
     */
    suspend fun suspendUser(userId: String): Result<Unit>
    
    /**
     * Get users by role
     */
    fun getUsersByRole(role: String): Flow<List<VitoUser>>
}

interface RideRepository {
    /**
     * Estimate fare
     */
    suspend fun estimateFare(
        pickupLat: Double,
        pickupLng: Double,
        dropoffLat: Double,
        dropoffLng: Double,
        category: String
    ): Result<com.beepbeep.vito.core.model.FareEstimate>
    
    /**
     * Create ride request
     */
    suspend fun createRide(ride: com.beepbeep.vito.core.model.Ride): Result<String>
    
    /**
     * Get ride by ID
     */
    suspend fun getRide(rideId: String): Result<com.beepbeep.vito.core.model.Ride?>
    
    /**
     * Observe ride status
     */
    fun observeRideStatus(rideId: String): Flow<com.beepbeep.vito.core.model.RideStatus>
    
    /**
     * Cancel ride
     */
    suspend fun cancelRide(rideId: String): Result<Unit>
    
    /**
     * Accept ride (driver)
     */
    suspend fun acceptRide(rideId: String, driverId: String): Result<Unit>
    
    /**
     * Update ride status (driver)
     */
    suspend fun updateRideStatus(rideId: String, status: com.beepbeep.vito.core.model.RideStatus): Result<Unit>
    
    /**
     * Get ride history
     */
    fun getRideHistory(userId: String): Flow<List<com.beepbeep.vito.core.model.Ride>>
}

interface PackageRepository {
    /**
     * Create package delivery request
     */
    suspend fun createPackage(pkg: com.beepbeep.vito.core.model.Package): Result<String>
    
    /**
     * Get package by ID
     */
    suspend fun getPackage(packageId: String): Result<com.beepbeep.vito.core.model.Package?>
    
    /**
     * Observe package status
     */
    fun observePackageStatus(packageId: String): Flow<com.beepbeep.vito.core.model.PackageStatus>
    
    /**
     * Cancel package
     */
    suspend fun cancelPackage(packageId: String): Result<Unit>
    
    /**
     * Accept package (driver)
     */
    suspend fun acceptPackage(packageId: String, driverId: String): Result<Unit>
    
    /**
     * Update package status
     */
    suspend fun updatePackageStatus(
        packageId: String,
        status: com.beepbeep.vito.core.model.PackageStatus,
        signatureUrl: String? = null,
        proofPhotoUrl: String? = null
    ): Result<Unit>
    
    /**
     * Get package history
     */
    fun getPackageHistory(userId: String): Flow<List<com.beepbeep.vito.core.model.Package>>
}

interface ChatRepository {
    /**
     * Get or create chat for trip/order
     */
    suspend fun getOrCreateChat(participants: Map<String, Boolean>, tripId: String?): Result<String>
    
    /**
     * Get messages for chat
     */
    fun getMessages(chatId: String): Flow<List<com.beepbeep.vito.core.model.ChatMessage>>
    
    /**
     * Send message
     */
    suspend fun sendMessage(chatId: String, senderId: String, text: String, imageUri: String?): Result<Unit>
    
    /**
     * Mark messages as read
     */
    suspend fun markAsRead(chatId: String, userId: String): Result<Unit>
}

interface LocationRepository {
    /**
     * Get driver location
     */
    suspend fun getDriverLocation(driverId: String): Result<com.beepbeep.vito.core.model.DriverLocation?>
    
    /**
     * Get all online drivers
     */
    fun getOnlineDrivers(): Flow<List<com.beepbeep.vito.core.model.DriverLocation>>
    
    /**
     * Update driver location
     */
    suspend fun updateDriverLocation(location: com.beepbeep.vito.core.model.DriverLocation): Result<Unit>
    
    /**
     * Set driver online status
     */
    suspend fun setOnline(driverId: String, online: Boolean): Result<Unit>
}

interface PaymentRepository {
    /**
     * Create payment intent
     */
    suspend fun createPaymentIntent(amount: Int, paymentMethodId: String?): Result<Pair<String, String>>
    
    /**
     * Top up wallet
     */
    suspend fun topUpWallet(amount: Int): Result<String>
    
    /**
     * Get saved payment methods
     */
    fun getPaymentMethods(): Flow<List<com.beepbeep.vito.core.model.PaymentMethod>>
    
    /**
     * Add payment method
     */
    suspend fun addPaymentMethod(cardId: String): Result<Unit>
    
    /**
     * Remove payment method
     */
    suspend fun removePaymentMethod(paymentMethodId: String): Result<Unit>
}

interface PromoCodeRepository {
    /**
     * Apply promo code
     */
    suspend fun applyPromoCode(code: String, amount: Int): Result<Int>
    
    /**
     * Create promo code (admin)
     */
    suspend fun createPromoCode(
        code: String,
        discountType: com.beepbeep.vito.core.model.DiscountType,
        discountValue: Int,
        maxUses: Int
    ): Result<Unit>
}