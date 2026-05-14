package com.vito.core.domain.model

import androidx.compose.runtime.Composable

enum class Vehicle(
    val id: String,
    val displayName: String,
    val capacity: Int,
    val pricePerKm: Double
) {
    STANDARD("standard", "Standard", 4, 1.5),
    PREMIUM("premium", "Premium", 3, 2.0),
    LARGE("large", "Large", 6, 2.5)
}

typealias VehicleType = Vehicle

@Composable
fun VehicleTypeSelector(
    selected: VehicleType,
    onSelect: (VehicleType) -> Unit
) {}

@Composable
fun DriverCard(
    driver: Driver,
    onAccept: () -> Unit
) {}

data class Driver(
    val id: String,
    val name: String,
    val rating: Float,
    val vehicleType: VehicleType
)
