package com.vito.app.presentation.ui.screens.chat

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vito.app.data.repository.ChatRepository
import com.vito.app.domain.model.Message
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.util.UUID
import javax.inject.Inject

@HiltViewModel
class ChatViewModel @Inject constructor(
    private val chatRepository: ChatRepository
) : ViewModel() {
    
    private val _messages = MutableStateFlow<List<Message>>(emptyList())
    val messages: StateFlow<List<Message>> = _messages.asStateFlow()
    
    private var currentChatId: String = "default"
    private var currentUserId: String = "user1"
    
    fun initialize(chatId: String, userId: String) {
        currentChatId = chatId
        currentUserId = userId
        
        viewModelScope.launch {
            chatRepository.getMessages(chatId).collect { messages ->
                _messages.value = messages
            }
        }
    }
    
    fun sendMessage(text: String, imageUrl: String? = null) {
        viewModelScope.launch {
            val message = Message(
                id = UUID.randomUUID().toString(),
                chatId = currentChatId,
                senderId = currentUserId,
                senderName = "You",
                text = text,
                imageUrl = imageUrl,
                timestamp = System.currentTimeMillis(),
                isFromCurrentUser = true
            )
            chatRepository.sendMessage(currentChatId, text, imageUrl)
        }
    }
    
    init {
        // Load initial messages
        initialize("default", "user1")
    }
}