package com.vito.app.data.mock

import com.vito.app.data.repository.ChatRepository
import com.vito.app.domain.model.Message
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MockChatRepository @Inject constructor() : ChatRepository {
    override fun getMessages(chatId: String): Flow<List<Message>> = flow {
        delay(300)
        emit(emptyList())
    }

    override suspend fun sendMessage(chatId: String, text: String, imageUrl: String?): Result<Message> {
        return Result.success(Message("msg1", chatId, "user1", "You", text, imageUrl, System.currentTimeMillis(), true))
    }

    override suspend fun markAsRead(chatId: String): Result<Unit> {
        return Result.success(Unit)
    }
}
