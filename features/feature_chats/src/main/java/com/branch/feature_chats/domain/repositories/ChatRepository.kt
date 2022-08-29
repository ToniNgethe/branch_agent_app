package com.branch.feature_chats.domain.repositories

import com.branch.core_network.data.ResponseState
import com.branch.feature_chats.domain.models.Chat
import com.branch.feature_chats.domain.models.Message
import kotlinx.coroutines.flow.Flow

interface ChatRepository {
    suspend fun getChats(): ResponseState<String>
    fun listenForChatsUpdates(): Flow<List<Chat>>
    fun getMessagesByThreadId( threadId: String ): Flow<List<Message>>
}