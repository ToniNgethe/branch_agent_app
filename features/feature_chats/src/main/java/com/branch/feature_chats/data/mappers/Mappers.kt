package com.branch.feature_chats.data.mappers

import com.branch.core_database.data.entities.MessageEntity
import com.branch.feature_chats.data.dto.MessagesResponseDto
import com.branch.feature_chats.domain.models.Chat

fun MessageEntity.toChat(): Chat = Chat(
    id = id!!,
    userId = userId, latestMessage = body, timeStamp = timeStamp, agentId = agentId, threadId = threadId
)

fun MessagesResponseDto.toMessageEntity(): MessageEntity = MessageEntity(
    id = id,
    threadId = threadId,
    userId = userId,
    body = body,
    timeStamp = timestamp,
    agentId = agentId
)