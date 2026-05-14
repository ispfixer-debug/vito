package com.vito.core.data.repository.impl

import com.vito.core.data.model.ChatMessageDto
import com.vito.core.data.repository.ChatRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import java.util.UUID
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ChatRepositoryImpl @Inject constructor() : ChatRepository {
    private val messages = MutableStateFlow<List<ChatMessageDto>>(emptyList())

    override fun getMessages(chatId: String): Flow<List<ChatMessageDto>> {
        return MutableStateFlow(messages.value.filter { it.chatId == chatId })
    }

    override suspend fun sendMessage(message: ChatMessageDto): Result<String> = try {
        val messageId = "msg_${UUID.randomUUID()}"
        val newMessage = message.copy(id = messageId)
        messages.value = messages.value + newMessage
        Result.success(messageId)
    } catch (e: Exception) {
        Result.failure(e)
    }

    override suspend fun markAsRead(chatId: String, messageId: String): Result<Unit> = try {
        messages.value = messages.value.map {
            if (it.id == messageId) it.copy(isRead = true)
            else it
        }
        Result.success(Unit)
    } catch (e: Exception) {
        Result.failure(e)
    }
}