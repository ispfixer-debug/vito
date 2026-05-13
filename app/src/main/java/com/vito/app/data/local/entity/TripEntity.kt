package com.vito.app.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "trips")
data class TripEntity(
    @PrimaryKey
    val id: String,
    val userId: String,
    val driverId: String?,
    val driverName: String?,
    val driverPhotoUrl: String?,
    val vehicleInfo: String?,
    val pickupLocation: String,
    val pickupLat: Double,
    val pickupLng: Double,
    val dropoffLocation: String,
    val dropoffLat: Double,
    val dropoffLng: Double,
    val fare: Double,
    val status: String,
    val createdAt: Long,
    val completedAt: Long?,
    val rating: Int?,
    val tip: Double?,
    val stops: String?
)