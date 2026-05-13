package com.vito.app.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "messages")
data class MessageEntity(
    @PrimaryKey
    val id: String,
    val chatId: String,
    val senderId: String,
    val senderName: String,
    val text: String?,
    val imageUrl: String?,
    val timestamp: Long,
    val isRead: Boolean = false
)