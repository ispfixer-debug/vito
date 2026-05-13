package com.vito.app.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "packages")
data class PackageEntity(
    @PrimaryKey
    val id: String,
    val userId: String,
    val driverId: String?,
    val driverName: String?,
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
    val photoUrl: String?,
    val insurance: Boolean,
    val fare: Double,
    val status: String,
    val createdAt: Long,
    val completedAt: Long?,
    val proofOfDeliveryUrl: String?,
    val signatureUrl: String?
)