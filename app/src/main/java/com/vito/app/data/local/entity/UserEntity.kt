package com.vito.app.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "users")
data class UserEntity(
    @PrimaryKey
    val id: String,
    val username: String,
    val alias: String,
    val email: String?,
    val phone: String?,
    val photoUrl: String?,
    val walletBalance: Double = 0.0,
    val role: String = "client",
    val isBlocked: Boolean = false,
    val rating: Double = 5.0
)