package com.vito.app.data.repository

import com.vito.app.domain.model.Message
import kotlinx.coroutines.flow.Flow

interface ChatRepository {
    fun getMessages(chatId: String): Flow<List<Message>>
    suspend fun sendMessage(chatId: String, text: String, imageUrl: String?): Result<Message>
    suspend fun markAsRead(chatId: String): Result<Unit>
}
