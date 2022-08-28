package com.branch.feature_chats.data.dto


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class MessagesResponseDto(
    @SerialName("agent_id") val agentId: String?,
    @SerialName("body") val body: String?,
    @SerialName("id") val id: Int?,
    @SerialName("thread_id") val threadId: Int?,
    @SerialName("timestamp") val timestamp: String?,
    @SerialName("user_id") val userId: String?
)