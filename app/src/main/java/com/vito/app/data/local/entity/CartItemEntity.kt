package com.vito.app.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "cart_items")
data class CartItemEntity(
    @PrimaryKey
    val productId: String,
    val name: String,
    val price: Double,
    val quantity: Int,
    val imageUrl: String?
)