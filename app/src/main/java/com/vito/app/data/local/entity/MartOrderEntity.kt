package com.vito.app.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "mart_orders")
data class MartOrderEntity(
    @PrimaryKey
    val id: String,
    val userId: String,
    val driverId: String?,
    val driverName: String?,
    val items: String,
    val totalAmount: Double,
    val deliveryFee: Double,
    val deliveryAddress: String,
    val deliveryLat: Double,
    val deliveryLng: Double,
    val status: String,
    val createdAt: Long,
    val completedAt: Long?
)