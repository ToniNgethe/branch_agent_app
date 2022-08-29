package com.branch.feature_chats.data.dto

import kotlinx.serialization.SerialName

@kotlinx.serialization.Serializable
data class MessageRequestDto(
    @SerialName("thread_id") val threadId: String, val body: String
)