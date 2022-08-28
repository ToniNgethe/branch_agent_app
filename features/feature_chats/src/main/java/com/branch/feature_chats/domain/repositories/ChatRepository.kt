package com.branch.feature_chats.domain.repositories

import com.branch.core_network.data.ResponseState
import com.branch.feature_chats.domain.models.Chat
import kotlinx.coroutines.flow.Flow

interface ChatRepository {
   suspend fun getChats(): ResponseState<String>
    fun listenForChatsUpdates(): Flow<List<Chat>>
}