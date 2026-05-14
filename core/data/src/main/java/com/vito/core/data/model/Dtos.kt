package com.vito.core.data.model

data class UserDto(
    val uid: String = "",
    val alias: String = "",
    val photoUrl: String = "",
    val role: String = "",          // CLIENT | DRIVER | ADMIN
    val phoneNumber: String = "",
    val email: String = "",
    val rating: Double = 0.0,
    val totalRatings: Int = 0,
    val isActive: Boolean = true,
    val createdAt: Long = System.currentTimeMillis(),
    val fcmToken: String = ""
)

data class QrTokenDto(
    val token: String = "",
    val userId: String = "",
    val role: String = "",
    val expiresAt: Long = 0,
    val isUsed: Boolean = false,
    val createdAt: Long = System.currentTimeMillis()
)

data class RideDto(
    val id: String = "",
    val userId: String = "",
    val driverId: String = "",
    val status: String = "",         // PENDING | ACCEPTED | ARRIVED | IN_PROGRESS | COMPLETED | CANCELLED
    val pickupLat: Double = 0.0,
    val pickupLng: Double = 0.0,
    val pickupAddress: String = "",
    val dropoffLat: Double = 0.0,
    val dropoffLng: Double = 0.0,
    val dropoffAddress: String = "",
    val stops: List<StopDto> = emptyList(),
    val vehicleType: String = "STANDARD",
    val estimatedPrice: Double = 0.0,
    val actualPrice: Double = 0.0,
    val distanceKm: Double = 0.0,
    val estimatedMinutes: Int = 0,
    val promoCode: String? = null,
    val discount: Double = 0.0,
    val paymentMethod: String = "CASH", // CASH | CARD | WALLET | GOOGLE_PAY
    val rating: Int? = null,
    val review: String? = null,
    val tip: Double = 0.0,
    val createdAt: Long = System.currentTimeMillis(),
    val acceptedAt: Long? = null,
    val arrivedAt: Long? = null,
    val startedAt: Long? = null,
    val completedAt: Long? = null
)

data class StopDto(
    val lat: Double = 0.0,
    val lng: Double = 0.0,
    val address: String = "",
    val label: String = ""
)

data class PackageDto(
    val id: String = "",
    val userId: String = "",
    val driverId: String = "",
    val status: String = "",         // PENDING | ACCEPTED | PICKED_UP | DELIVERED | CANCELLED
    val senderName: String = "",
    val senderPhone: String = "",
    val senderAddress: String = "",
    val recipientName: String = "",
    val recipientPhone: String = "",
    val recipientAddress: String = "",
    val packageDescription: String = "",
    val weightKg: Double = 0.0,
    val estimatedPrice: Double = 0.0,
    val actualPrice: Double = 0.0,
    val paymentMethod: String = "CASH",
    val signatureUrl: String? = null,
    val photoUrl: String? = null,
    val rating: Int? = null,
    val review: String? = null,
    val tip: Double = 0.0,
    val createdAt: Long = System.currentTimeMillis(),
    val acceptedAt: Long? = null,
    val pickedUpAt: Long? = null,
    val deliveredAt: Long? = null
)

data class MartProductDto(
    val id: String = "",
    val name: String = "",
    val description: String = "",
    val price: Double = 0.0,
    val imageUrl: String = "",
    val category: String = "",
    val isAvailable: Boolean = true,
    val stock: Int = 0,
    val createdAt: Long = System.currentTimeMillis()
)

data class MartOrderDto(
    val id: String = "",
    val userId: String = "",
    val items: List<MartOrderItemDto> = emptyList(),
    val status: String = "",         // PLACED | PREPARING | DISPATCHED | DELIVERED | CANCELLED
    val totalPrice: Double = 0.0,
    val deliveryAddress: String = "",
    val deliveryLat: Double = 0.0,
    val deliveryLng: Double = 0.0,
    val paymentMethod: String = "CASH",
    val rating: Int? = null,
    val review: String? = null,
    val createdAt: Long = System.currentTimeMillis(),
    val deliveredAt: Long? = null
)

data class MartOrderItemDto(
    val productId: String = "",
    val productName: String = "",
    val quantity: Int = 0,
    val price: Double = 0.0
)

data class ChatMessageDto(
    val id: String = "",
    val chatId: String = "",
    val senderId: String = "",
    val senderRole: String = "", // CLIENT | DRIVER
    val message: String = "",
    val timestamp: Long = System.currentTimeMillis(),
    val isRead: Boolean = false
)

data class DriverLocationDto(
    val driverId: String = "",
    val lat: Double = 0.0,
    val lng: Double = 0.0,
    val bearing: Float = 0f,
    val timestamp: Long = System.currentTimeMillis(),
    val isOnline: Boolean = true
)

data class PayoutRequestDto(
    val id: String = "",
    val driverId: String = "",
    val amount: Double = 0.0,
    val status: String = "",  // PENDING | PROCESSED | REJECTED
    val bankAccount: String = "",
    val createdAt: Long = System.currentTimeMillis(),
    val processedAt: Long? = null
)