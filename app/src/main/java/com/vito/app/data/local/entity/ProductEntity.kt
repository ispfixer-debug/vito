package com.vito.app.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "products")
data class ProductEntity(
    @PrimaryKey
    val id: String,
    val name: String,
    val description: String?,
    val price: Double,
    val category: String,
    val imageUrls: String,
    val isAvailable: Boolean = true
)