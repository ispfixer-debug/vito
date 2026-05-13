/**
 * Vito Core Domain Models
 */
package com.beepbeep.vito.core.model

import kotlinx.serialization.Serializable
import kotlinx.datetime.Instant

// ============================================================================
// USER MODEL
// ============================================================================

@Serializable
data class VitoUser(
    val uid: String = "",
    val alias: String = "",
    val photoUrl: String? = null,
    val role: UserRole = UserRole.CLIENT,
    val deviceId: String = "",
    val walletBalance: Int = 0,
    val stripeCustomerId: String? = null,
    val isVerified: Boolean = false,
    val documents: DriverDocuments? = null,
    val vehicle: Vehicle? = null,
    val bankAccountId: String? = null,
    val fcmToken: String? = null,
    val accountStatus: AccountStatus = AccountStatus.ACTIVE,
    val createdAt: Instant? = null
)

enum class UserRole { CLIENT, DRIVER, ADMIN }

@Serializable
data class DriverDocuments(
    val drivingLicence: String? = null,
    val registration: String? = null,
    val insurance: String? = null,
    val status: DocumentStatus = DocumentStatus.PENDING
)

enum class DocumentStatus { PENDING, APPROVED, REJECTED }

enum class AccountStatus { ACTIVE, SUSPENDED, DELETED }

@Serializable
data class Vehicle(
    val type: VehicleType = VehicleType.CAR,
    val model: String = "",
    val colour: String = "",
    val plate: String = ""
)

enum class VehicleType { CAR, BIKE, AUTO, VAN }

// ============================================================================
// RIDE MODEL
// ============================================================================

@Serializable
data class Ride(
    val id: String = "",
    val clientId: String = "",
    val driverId: String? = null,
    val pickup: Location = Location(),
    val dropoff: Location = Location(),
    val stops: List<Location> = emptyList(),
    val vehicleCategory: VehicleCategory = VehicleCategory.STANDARD,
    val scheduledTime: Instant? = null,
    val promoCode: String? = null,
    val fareEstimate: Int = 0,
    val status: RideStatus = RideStatus.REQUESTED,
    val createdAt: Instant? = null
)

@Serializable
data class Location(
    val lat: Double = 0.0,
    val lng: Double = 0.0,
    val address: String = "",
    val name: String? = null
)

enum class VehicleCategory { STANDARD, PREMIUM, VAN }

enum class RideStatus { REQUESTED, SCHEDULED, ACCEPTED, ARRIVING, STARTED, COMPLETED, CANCELLED }

// ============================================================================
// PACKAGE MODEL
// ============================================================================

@Serializable
data class Package(
    val id: String = "",
    val senderId: String = "",
    val driverId: String? = null,
    val pickup: Location = Location(),
    val dropoff: Location = Location(),
    val recipientAlias: String? = null,
    val size: PackageSize = PackageSize.SMALL,
    val weightGrams: Int = 0,
    val fragile: Boolean = false,
    val description: String = "",
    val photoUrl: String? = null,
    val hasInsurance: Boolean = false,
    val fareEstimate: Int = 0,
    val status: PackageStatus = PackageStatus.CREATED,
    val signatureUrl: String? = null,
    val proofPhotoUrl: String? = null,
    val createdAt: Instant? = null
)

enum class PackageSize { SMALL, MEDIUM, LARGE }

enum class PackageStatus { CREATED, PICKING_UP, PICKED_UP, DELIVERING, DELIVERED, CANCELLED }

// ============================================================================
// MART ORDER MODEL
// ============================================================================

@Serializable
data class MartOrder(
    val id: String = "",
    val clientId: String = "",
    val driverId: String? = null,
    val items: List<MartOrderItem> = emptyList(),
    val deliveryAddress: Location = Location(),
    val subtotal: Int = 0,
    val deliveryFee: Int = 0,
    val discount: Int = 0,
    val total: Int = 0,
    val status: MartOrderStatus = MartOrderStatus.PLACED,
    val createdAt: Instant? = null
)

@Serializable
data class MartOrderItem(
    val productId: String = "",
    val name: String = "",
    val price: Int = 0,
    val quantity: Int = 1,
    val imageUrl: String? = null
)

@Serializable
data class MartProduct(
    val id: String = "",
    val name: String = "",
    val description: String = "",
    val price: Int = 0,
    val category: String = "",
    val stock: Int = 0,
    val imageUrl: String? = null
)

enum class MartOrderStatus { PLACED, PREPARING, DRIVER_PICKING_UP, DELIVERING, DELIVERED, CANCELLED }

// ============================================================================
// CHAT MODEL
// ============================================================================

@Serializable
data class Chat(
    val id: String = "",
    val participants: Map<String, Boolean> = emptyMap(),
    val lastMessage: String = "",
    val lastMessageAt: Instant? = null,
    val status: ChatStatus = ChatStatus.ACTIVE,
    val tripId: String? = null
)

@Serializable
data class ChatMessage(
    val id: String = "",
    val chatId: String = "",
    val senderId: String = "",
    val text: String = "",
    val imageUrl: String? = null,
    val timestamp: Instant? = null,
    val isRead: Boolean = false
)

enum class ChatStatus { ACTIVE, COMPLETED }

// ============================================================================
// QR TOKEN
// ============================================================================

@Serializable
data class QrToken(
    val token: String = "",
    val role: UserRole = UserRole.CLIENT,
    val createdBy: String = "",
    val createdAt: Instant? = null,
    val expiresAt: Instant? = null,
    val used: Boolean = false,
    val deviceId: String? = null
)

// ============================================================================
// DRIVER LOCATION
// ============================================================================

@Serializable
data class DriverLocation(
    val driverId: String = "",
    val lat: Double = 0.0,
    val lng: Double = 0.0,
    val heading: Double? = null,
    val online: Boolean = false,
    val timestamp: Instant? = null
)

// ============================================================================
// PAYMENT
// ============================================================================

@Serializable
data class FareEstimate(
    val estimatedFare: Int = 0,
    val duration: Int = 0,
    val distance: Double = 0.0
)

@Serializable
data class PromoCode(
    val code: String = "",
    val discountType: DiscountType = DiscountType.PERCENTAGE,
    val discountValue: Int = 0,
    val maxUses: Int = 0,
    val usesCount: Int = 0,
    val validUntil: Instant? = null
)

enum class DiscountType { PERCENTAGE, FIXED }