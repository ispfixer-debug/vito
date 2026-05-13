/**
 * Vito Use Cases
 * Business logic for the Vito platform
 */
package com.beepbeep.vito.core.usecase

import com.beepbeep.vito.core.model.*
import com.beepbeep.vito.core.repository.*

// ============================================================================
// AUTH USE CASES
// ============================================================================

class QrAuthUseCase(
    private val authRepository: AuthRepository,
    private val userRepository: UserRepository
) {
    /**
     * Register new user with QR token
     */
    suspend operator fun invoke(token: String, deviceId: String): Result<VitoUser> {
        return authRepository.registerWithToken(token, deviceId)
    }
    
    /**
     * Sign out
     */
    suspend fun signOut() {
        authRepository.signOut()
    }
}

// ============================================================================
// RIDE USE CASES
// ============================================================================

class EstimateFareUseCase(
    private val rideRepository: RideRepository
) {
    suspend operator fun invoke(
        pickupLat: Double,
        pickupLng: Double,
        dropoffLat: Double,
        dropoffLng: Double,
        category: String = "standard"
    ): Result<FareEstimate> {
        return rideRepository.estimateFare(
            pickupLat, pickupLng, dropoffLat, dropoffLng, category
        )
    }
}

class BookRideUseCase(
    private val rideRepository: RideRepository,
    private val promoCodeRepository: PromoCodeRepository
) {
    suspend operator fun invoke(ride: Ride): Result<String> {
        // Apply promo code if provided
        var finalFare = ride.fareEstimate
        if (ride.promoCode != null) {
            val discountedFare = promoCodeRepository.applyPromoCode(ride.promoCode, ride.fareEstimate)
            if (discountedFare.isSuccess) {
                finalFare = discountedFare.getOrThrow()
            }
        }
        
        val rideWithFare = ride.copy(fareEstimate = finalFare)
        return rideRepository.createRide(rideWithFare)
    }
}

class TrackRideUseCase(
    private val rideRepository: RideRepository
) {
    fun observeStatus(rideId: String): Flow<RideStatus> {
        return rideRepository.observeRideStatus(rideId)
    }
    
    suspend fun getRide(rideId: String): Ride? {
        return rideRepository.getRide(rideId).getOrNull()
    }
}

class CompleteRideUseCase(
    private val rideRepository: RideRepository,
    private val userRepository: UserRepository
) {
    suspend operator fun invoke(
        rideId: String,
        driverId: String,
        paymentMethod: String = "wallet"  // "cash", "card", "wallet", "google_pay"
    ): Result<Unit> {
        val ride = rideRepository.getRide(rideId).getOrNull()
            ?: return Result.failure(Exception("Ride not found"))
        
        // Update ride status
        rideRepository.updateRideStatus(rideId, RideStatus.COMPLETED)
        
        // Process payment
        if (paymentMethod == "wallet") {
            val user = userRepository.getUser(ride.clientId).getOrNull()
            if (user != null && user.walletBalance >= ride.fareEstimate) {
                userRepository.updateWalletBalance(ride.clientId, -ride.fareEstimate)
            } else {
                return Result.failure(Exception("Insufficient wallet balance"))
            }
        }
        
        // If driver gets cash, no wallet deduction needed
        // The driver will record the cash collection
        
        return Result.success(Unit)
    }
}

// ============================================================================
// DRIVER USE CASES
// ============================================================================

class DriverLocationUseCase(
    private val locationRepository: LocationRepository
) {
    suspend fun updateLocation(location: DriverLocation): Result<Unit> {
        return locationRepository.updateDriverLocation(location)
    }
    
    suspend fun goOnline(driverId: String): Result<Unit> {
        return locationRepository.setOnline(driverId, true)
    }
    
    suspend fun goOffline(driverId: String): Result<Unit> {
        return locationRepository.setOnline(driverId, false)
    }
    
    fun getOnlineDrivers(): Flow<List<DriverLocation>> {
        return locationRepository.getOnlineDrivers()
    }
}

class AcceptRideUseCase(
    private val rideRepository: RideRepository
) {
    suspend operator fun invoke(rideId: String, driverId: String): Result<Unit> {
        return rideRepository.acceptRide(rideId, driverId)
    }
}

class UpdateRideStatusUseCase(
    private val rideRepository: RideRepository,
    private val chatRepository: ChatRepository
) {
    suspend operator fun invoke(rideId: String, status: RideStatus): Result<Unit> {
        return rideRepository.updateRideStatus(rideId, status)
    }
}

// ============================================================================
// WALLET USE CASES
// ============================================================================

class WalletUseCase(
    private val paymentRepository: PaymentRepository,
    private val userRepository: UserRepository
) {
    /**
     * Top up wallet
     */
    suspend fun topUp(amount: Int): Result<String> {
        if (amount < 50) {
            return Result.failure(Exception("Minimum top-up is $0.50"))
        }
        return paymentRepository.topUpWallet(amount)
    }
    
    /**
     * Get wallet balance
     */
    suspend fun getBalance(userId: String): Result<Int> {
        val user = userRepository.getUser(userId).getOrNull()
        return Result.success(user?.walletBalance ?: 0)
    }
    
    /**
     * Get saved payment methods
     */
    fun getPaymentMethods(): Flow<List<PaymentMethod>> {
        return paymentRepository.getPaymentMethods()
    }
    
    /**
     * Add payment method
     */
    suspend fun addPaymentMethod(cardId: String): Result<Unit> {
        return paymentRepository.addPaymentMethod(cardId)
    }
    
    /**
     * Remove payment method
     */
    suspend fun removePaymentMethod(paymentMethodId: String): Result<Unit> {
        return paymentRepository.removePaymentMethod(paymentMethodId)
    }
}

// ============================================================================
// CHAT USE CASES
// ============================================================================

class ChatUseCase(
    private val chatRepository: ChatRepository
) {
    fun getMessages(chatId: String): Flow<List<ChatMessage>> {
        return chatRepository.getMessages(chatId)
    }
    
    suspend fun sendMessage(chatId: String, senderId: String, text: String): Result<Unit> {
        return chatRepository.sendMessage(chatId, senderId, text, null)
    }
    
    suspend fun sendImage(chatId: String, senderId: String, imageUri: String): Result<Unit> {
        return chatRepository.sendMessage(chatId, senderId, "", imageUri)
    }
}

// ============================================================================
// ADMIN USE CASES
// ============================================================================

class AdminGenerateQrUseCase(
    private val functions: FirebaseFunctions
) {
    suspend operator fun invoke(role: String, expiryHours: Int = 24): Result<Pair<String, String>> {
        // This would call the Cloud Function
        // For now, return placeholder
        return Result.success(Pair("placeholder-token", "2024-01-01T00:00:00Z"))
    }
}

class AdminUserManagementUseCase(
    private val userRepository: UserRepository
) {
    fun getAllUsers(): Flow<List<VitoUser>> {
        return userRepository.getAllUsers()
    }
    
    suspend fun suspendUser(userId: String): Result<Unit> {
        return userRepository.suspendUser(userId)
    }
    
    suspend fun deleteUser(userId: String): Result<Unit> {
        return userRepository.deleteAccount(userId)
    }
}

class AdminDriverVerificationUseCase(
    private val userRepository: UserRepository
) {
    suspend fun approveDriver(userId: String): Result<Unit> {
        val user = userRepository.getUser(userId).getOrNull()
            ?: return Result.failure(Exception("User not found"))
        
        val updatedDocs = user.documents?.copy(status = DocumentStatus.APPROVED)
        val updatedUser = user.copy(
            documents = updatedDocs,
            isVerified = true
        )
        return userRepository.updateUser(updatedUser)
    }
    
    suspend fun rejectDriver(userId: String, reason: String): Result<Unit> {
        val user = userRepository.getUser(userId).getOrNull()
            ?: return Result.failure(Exception("User not found"))
        
        val updatedDocs = user.documents?.copy(status = DocumentStatus.REJECTED)
        val updatedUser = user.copy(documents = updatedDocs)
        return userRepository.updateUser(updatedUser)
    }
}

class AdminFinanceUseCase(
    private val functions: FirebaseFunctions
) {
    suspend fun getRevenueStats(startDate: String?, endDate?: String?): Result<RevenueStats> {
        // Call Firebase function
        return Result.success(RevenueStats(0, 0, 0))
    }
}

data class RevenueStats(
    val totalRevenue: Int,
    val rides: Int,
    val averageFare: Int
)