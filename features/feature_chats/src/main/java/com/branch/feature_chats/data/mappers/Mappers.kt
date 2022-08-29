package com.branch.feature_chats.data.mappers

import com.branch.core_database.data.entities.MessageEntity
import com.branch.core_utils.utils.TimeUtils
import com.branch.feature_chats.data.dto.MessagesResponseDto
import com.branch.feature_chats.domain.models.Chat
import com.branch.feature_chats.domain.models.Message

fun MessageEntity.toChat(): Chat = Chat(
    id = id!!,
    userId = userId,
    latestMessage = body,
    timeStamp = TimeUtils.formatDate(
        timeStamp,
        "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'",
        "E ,MMM yyyy HH:mm a"
    ),
    oTimeStamp = timeStamp,
    agentId = agentId,
    threadId = threadId
)

fun MessagesResponseDto.toMessageEntity(): MessageEntity = MessageEntity(
    id = id,
    threadId = threadId,
    userId = userId,
    body = body,
    timeStamp = timestamp,
    agentId = agentId
)

fun MessageEntity.toMessage(): Message = Message(
    id = id,
    userId = userId,
    message = body,
    timeStamp = TimeUtils.formatDate(timeStamp, "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", "E,MMM yyyy HH:mm"),
    agentId = agentId
)