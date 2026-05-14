package com.vito.core.common

object FirestoreCollections {
    const val USERS = "users_v2"
    const val QR_TOKENS = "qr_tokens"
    const val RIDES = "rides"
    const val PACKAGES = "packages"
    const val MART_ORDERS = "mart_orders"
    const val MART_PRODUCTS = "mart_products"
    const val CHATS = "chats"
    const val DRIVER_LOCATIONS = "driver_locations"
    const val AUDIT_LOGS = "audit_logs"
    const val PAYOUT_REQUESTS = "payout_requests"
}

object DeepLinks {
    const val SCHEME = "vito"
    const val REGISTER_HOST = "register"
    const val REGISTER_URI = "$SCHEME://$REGISTER_HOST"
}

object VehicleType {
    const val STANDARD = "STANDARD"
    const val PREMIUM = "PREMIUM"
    const val BIKE = "BIKE"
    const val AUTO = "AUTO"
}

object RideStatus {
    const val PENDING = "PENDING"
    const val ACCEPTED = "ACCEPTED"
    const val ARRIVED = "ARRIVED"
    const val IN_PROGRESS = "IN_PROGRESS"
    const val COMPLETED = "COMPLETED"
    const val CANCELLED = "CANCELLED"
}

object PackageStatus {
    const val PENDING = "PENDING"
    const val ACCEPTED = "ACCEPTED"
    const val PICKED_UP = "PICKED_UP"
    const val DELIVERED = "DELIVERED"
    const val CANCELLED = "CANCELLED"
}

object OrderStatus {
    const val PLACED = "PLACED"
    const val PREPARING = "PREPARING"
    const val DISPATCHED = "DISPATCHED"
    const val DELIVERED = "DELIVERED"
    const val CANCELLED = "CANCELLED"
}

object PaymentMethod {
    const val CASH = "CASH"
    const val CARD = "CARD"
    const val WALLET = "WALLET"
    const val GOOGLE_PAY = "GOOGLE_PAY"
}