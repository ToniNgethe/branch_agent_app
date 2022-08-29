package com.branch.feature_chats.presentation.chats

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.branch.core_network.data.ResponseState
import com.branch.feature_chats.domain.models.Chat
import com.branch.feature_chats.domain.repositories.ChatRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class ChatUiState(
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val chats: List<Chat> = emptyList()
)

@HiltViewModel
class ChatVm @Inject constructor(private val chatRepository: ChatRepository) : ViewModel() {

    private val _chatUiState = MutableStateFlow(ChatUiState())
    val chatUiState = _chatUiState.asStateFlow()

    init {
        fetchMessages()
        listenToDbUpdates()
    }

    private fun listenToDbUpdates() {
        viewModelScope.launch {
            chatRepository.listenForChatsUpdates().collectLatest { chats ->
                if (chats.isNotEmpty()) {
                    _chatUiState.update {
                        it.copy(
                            isLoading = false, errorMessage = null, chats = chats
                        )
                    }
                }
            }
        }
    }

    private fun fetchMessages() {
        viewModelScope.launch {
            _chatUiState.update { it.copy(isLoading = true) }
            when (chatRepository.getChats()) {
                is ResponseState.Success -> {
                }
                is ResponseState.Error -> {
                    _chatUiState.update {
                        it.copy(
                            isLoading = true, errorMessage = it.errorMessage
                        )
                    }
                }
            }
        }
    }
}