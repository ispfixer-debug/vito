package com.vito.core.data.repository

import com.vito.core.data.model.UserDto
import com.vito.core.data.model.QrTokenDto
import com.vito.core.data.model.RideDto
import com.vito.core.data.model.PackageDto
import com.vito.core.data.model.MartProductDto
import com.vito.core.data.model.MartOrderDto
import com.vito.core.data.model.ChatMessageDto
import com.vito.core.data.model.DriverLocationDto
import com.vito.core.data.model.PayoutRequestDto
import kotlinx.coroutines.flow.Flow

interface AuthRepository {
    val currentUser: Flow<UserDto?>
    suspend fun signInWithCustomToken(token: String): Result<UserDto>
    suspend fun signInWithPhone(phone: String, code: String): Result<UserDto>
    suspend fun signOut()
    fun getCurrentUserId(): String?
}

interface RideRepository {
    fun getRide(rideId: String): Flow<RideDto?>
    fun getUserRides(userId: String): Flow<List<RideDto>>
    fun getDriverRides(driverId: String): Flow<List<RideDto>>
    fun getAvailableRides(): Flow<List<RideDto>>
    suspend fun requestRide(ride: RideDto): Result<String>
    suspend fun acceptRide(rideId: String, driverId: String): Result<Unit>
    suspend fun updateRideStatus(rideId: String, status: String): Result<Unit>
    suspend fun completeRide(rideId: String, actualPrice: Double): Result<Unit>
    suspend fun cancelRide(rideId: String): Result<Unit>
    suspend fun rateRide(rideId: String, rating: Int, review: String?, tip: Double): Result<Unit>
    fun getCurrentUserId(): String?
}

interface PackageRepository {
    fun getPackage(packageId: String): Flow<PackageDto?>
    fun getUserPackages(userId: String): Flow<List<PackageDto>>
    fun getDriverPackages(driverId: String): Flow<List<PackageDto>>
    fun getAvailablePackages(): Flow<List<PackageDto>>
    suspend fun sendPackage(pkg: PackageDto): Result<String>
    suspend fun acceptPackage(packageId: String, driverId: String): Result<Unit>
    suspend fun updatePackageStatus(packageId: String, status: String): Result<Unit>
    suspend fun deliverPackage(packageId: String, signatureUrl: String?, photoUrl: String?): Result<Unit>
    suspend fun ratePackage(packageId: String, rating: Int, review: String?, tip: Double): Result<Unit>
    fun getCurrentUserId(): String?
}

interface MartRepository {
    fun getProducts(): Flow<List<MartProductDto>>
    fun getProduct(productId: String): Flow<MartProductDto?>
    fun getCategoryProducts(category: String): Flow<List<MartProductDto>>
    fun getOrders(userId: String): Flow<List<MartOrderDto>>
    suspend fun createOrder(order: MartOrderDto): Result<String>
    suspend fun updateOrderStatus(orderId: String, status: String): Result<Unit>
    suspend fun rateOrder(orderId: String, rating: Int, review: String?): Result<Unit>
    fun getCurrentUserId(): String?
}

interface ChatRepository {
    fun getMessages(chatId: String): Flow<List<ChatMessageDto>>
    suspend fun sendMessage(message: ChatMessageDto): Result<String>
    suspend fun markAsRead(chatId: String, messageId: String): Result<Unit>
}

interface UserRepository {
    fun getUser(userId: String): Flow<UserDto?>
    fun getUsers(): Flow<List<UserDto>>
    fun searchUsers(query: String): Flow<List<UserDto>>
    suspend fun createUser(user: UserDto): Result<Unit>
    suspend fun updateUser(user: UserDto): Result<Unit>
    suspend fun updateFcmToken(userId: String, token: String): Result<Unit>
}

interface QrRepository {
    fun generateQrToken(userId: String, role: String): Result<String>
    fun consumeQrToken(token: String): Result<QrTokenDto>
    fun getQrToken(token: String): Flow<QrTokenDto?>
}

interface DriverLocationRepository {
    fun getDriverLocation(driverId: String): Flow<DriverLocationDto?>
    fun getNearbyDrivers(lat: Double, lng: Double, radiusKm: Double): Flow<List<DriverLocationDto>>
    suspend fun updateLocation(location: DriverLocationDto): Result<Unit>
}

interface PayoutRepository {
    fun getPayoutRequests(driverId: String): Flow<List<PayoutRequestDto>>
    suspend fun requestPayout(request: PayoutRequestDto): Result<String>
}