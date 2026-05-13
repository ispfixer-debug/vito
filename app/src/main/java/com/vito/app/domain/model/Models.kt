package com.vito.app.domain.model

data class RideRequest(
    val pickupLocation: String,
    val pickupLat: Double,
    val pickupLng: Double,
    val dropoffLocation: String,
    val dropoffLat: Double,
    val dropoffLng: Double,
    val vehicleType: String = "standard",
    val stops: List<Stop> = emptyList()
)

data class Stop(val location: String, val lat: Double, val lng: Double)

data class RideFare(
    val fare: Double,
    val eta: Int, // minutes
    val distance: Double // km
)

data class DriverInfo(
    val id: String,
    val name: String,
    val photoUrl: String?,
    val vehicleInfo: String,
    val rating: Double
)

data class RideStatus(
    val status: RideStatusType,
    val driverInfo: DriverInfo?,
    val eta: Int?,
    val driverLat: Double?,
    val driverLng: Double?
)

enum class RideStatusType {
    IDLE,
    SEARCHING,
    ASSIGNED,
    DRIVER_ARRIVING,
    IN_PROGRESS,
    COMPLETED,
    CANCELLED
}

data class PackageRequest(
    val senderLocation: String,
    val senderLat: Double,
    val senderLng: Double,
    val recipientLocation: String,
    val recipientLat: Double,
    val recipientLng: Double,
    val recipientName: String?,
    val recipientPhone: String?,
    val packageSize: String,
    val weight: Double,
    val isFragile: Boolean,
    val description: String?,
    val insurance: Boolean
)

data class Product(
    val id: String,
    val name: String,
    val description: String?,
    val price: Double,
    val category: String,
    val imageUrls: List<String>,
    val isAvailable: Boolean = true
)

data class CartItem(
    val productId: String,
    val product: Product,
    val quantity: Int
) {
    val totalPrice: Double get() = product.price * quantity
}

data class User(
    val id: String,
    val username: String,
    val alias: String,
    val email: String?,
    val phone: String?,
    val photoUrl: String?,
    val walletBalance: Double,
    val role: UserRole,
    val rating: Double
)

enum class UserRole {
    CLIENT,
    DRIVER,
    ADMIN
}

data class Message(
    val id: String,
    val chatId: String,
    val senderId: String,
    val senderName: String,
    val text: String?,
    val imageUrl: String?,
    val timestamp: Long,
    val isFromCurrentUser: Boolean
)
data class TripHistoryItem(
    val id: String, 
    val pickup: String, 
    val dropoff: String, 
    val fare: Double, 
    val status: String, 
    val timestamp: Long, 
    val driverName: String?, 
    val tripStatus: String,
    val eta: Int = 0
)

data class MartOrder(
    val id: String,
    val items: List<CartItem>,
    val totalAmount: Double,
    val status: String,
    val timestamp: Long
)
