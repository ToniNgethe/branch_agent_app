package com.branch.feature_chats.data.reposotories

import com.branch.core_database.data.dao.MessagesDao
import com.branch.core_network.data.ResponseState
import com.branch.core_utils.utils.AppDispatchers
import com.branch.feature_chats.data.api.MessageApi
import com.branch.feature_chats.data.dto.MessageRequestDto
import com.branch.feature_chats.data.mappers.toChat
import com.branch.feature_chats.data.mappers.toMessage
import com.branch.feature_chats.data.mappers.toMessageEntity
import com.branch.feature_chats.domain.models.Chat
import com.branch.feature_chats.domain.models.Message
import com.branch.feature_chats.domain.repositories.ChatRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.withContext
import javax.inject.Inject

class ChatRepositoryImpl @Inject constructor(
    val msgApi: MessageApi, val messagesDao: MessagesDao, val appDispatchers: AppDispatchers
) : ChatRepository {

    /**
     * We are trying to fetch messages from the API, if the messages are not empty, we delete all the
     * messages in the database and insert the new messages
     *
     * @return ResponseState<String>
     */
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


    /**
     * We fetch all messages from the database, group them by threadId, sort them by timestamp, and
     * then emit a list of chats
     */
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
                val sortedChats = chats.sortedWith(compareByDescending { it.oTimeStamp }).toList()
                emit(sortedChats)
            }
        }
    }.flowOn(appDispatchers.io())

    /**
     * Emits new update
     *
     * @param threadId The id of the thread to get messages from
     */
    override fun getMessagesByThreadId(threadId: String): Flow<List<Message>> = flow {
        messagesDao.getMessagesByThread(threadId).collect { msgEntities ->
            val messages = msgEntities.sortedBy { it.timeStamp }.map { it.toMessage() }
            emit(messages)
        }
    }

    /**
     * > This function creates a message in the database and then updates the chat list
     *
     * @param threadId The id of the thread you want to send the message to
     * @param message The message to be sent
     * @param agentId The id of the agent that is currently logged in.
     * @return ResponseState<String>
     */
    override suspend fun createMessage(
        threadId: String, message: String, agentId: String
    ): ResponseState<String> {
        return try {
            msgApi.createMessage(
                MessageRequestDto(
                    threadId, message
                )
            )
            getChats()
            ResponseState.Success("Message sent")
        } catch (e: Exception) {
            ResponseState.Error("Unable to send message at this time")
        }
    }
}