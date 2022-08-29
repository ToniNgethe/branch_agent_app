package com.branch.feature_chats.presentation.chat_messages

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.branch.core_network.data.ResponseState
import com.branch.feature_chats.domain.models.Message
import com.branch.feature_chats.domain.repositories.ChatRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class ChatMessageUiState(
    val isLoading: Boolean = false,
    val messages: List<Message> = emptyList(),
    val error: String? = null,
    val success: String? = null
)

@HiltViewModel
class ChatMessageVm @Inject constructor(val repository: ChatRepository) : ViewModel() {
    private val _chatMessageUiState = MutableStateFlow(ChatMessageUiState())
    val chatMessageUiState = _chatMessageUiState.asStateFlow()

    fun getMessages(threadId: String) {
        viewModelScope.launch {
            repository.getMessagesByThreadId(threadId).collectLatest { msgs ->
                _chatMessageUiState.update { it.copy(messages = msgs, isLoading = false) }
            }
        }
    }

    fun sendMessage(threadId: String, message: String, agentId: String) {
        if (message.isEmpty()) {
            _chatMessageUiState.update {
                it.copy(
                    isLoading = false,
                    error = "Message is required to reply"
                )
            }
            return
        }
        _chatMessageUiState.update { it.copy(isLoading = true, success = null) }
        viewModelScope.launch {
            val response = repository.createMessage(threadId, message, agentId)
            when (response) {
                is ResponseState.Success -> {
                    _chatMessageUiState.update {
                        it.copy(
                            isLoading = false,
                            error = null,
                            success = response.data
                        )
                    }
                }
                is ResponseState.Error -> {
                    _chatMessageUiState.update {
                        it.copy(
                            isLoading = false,
                            error = response.error
                        )
                    }
                }
            }
        }
    }
}