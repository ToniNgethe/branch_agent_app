package com.branch.feature_chats.data.reposotories

import com.branch.core_database.data.dao.MessagesDao
import com.branch.core_network.data.ResponseState
import com.branch.core_utils.utils.AppDispatchers
import com.branch.feature_chats.data.api.MessageApi
import com.branch.feature_chats.data.mappers.toChat
import com.branch.feature_chats.data.mappers.toMessage
import com.branch.feature_chats.data.mappers.toMessageEntity
import com.branch.feature_chats.domain.models.Chat
import com.branch.feature_chats.domain.models.Message
import com.branch.feature_chats.domain.repositories.ChatRepository
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.withContext
import javax.inject.Inject

class ChatRepositoryImpl @Inject constructor(
    val msgApi: MessageApi, val messagesDao: MessagesDao, val appDispatchers: AppDispatchers
) : ChatRepository {

    override suspend fun getChats(): ResponseState<String> {
        return try {
            val messages = msgApi.fetchMessages()
            if (messages.isNotEmpty()) {
                withContext(appDispatchers.io()) {
                    messagesDao.deleteAllMessages()
                    val messageEntities = messages.map { it.toMessageEntity() }.toList()
                    messagesDao.insertAll(messageEntities)
                }
            }
            ResponseState.Success("Messages fetched")
        } catch (e: Exception) {
            ResponseState.Error("Unable to fetch chats at the moment!Kindly try again later")
        }
    }


    override fun listenForChatsUpdates(): Flow<List<Chat>> = flow {
        val messages = messagesDao.fetchAllMessages()
        messages.collect { msgEntities ->
            if (msgEntities.isNotEmpty()) {
                val chats = mutableListOf<Chat>()
                // group by date
                val data = msgEntities.groupBy { it.threadId }
                for (threadId in data.keys) {

                    val sortedMessages =
                        data[threadId]?.sortedWith(compareByDescending { it.timeStamp })
                    chats.add(sortedMessages!![0].toChat())
                }
                emit(chats)
            }
        }
    }.flowOn(appDispatchers.io())

    override fun getMessagesByThreadId(threadId: String): Flow<List<Message>> = flow {
        messagesDao.getMessagesByThread(threadId).collect { msgEntities ->
            val messages =
                msgEntities.map { it.toMessage() }.sortedWith(compareByDescending { it.timeStamp })
            emit(messages)
        }
    }
}