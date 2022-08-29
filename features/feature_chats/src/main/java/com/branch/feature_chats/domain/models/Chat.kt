package com.branch.feature_chats.domain.models

data class Chat(
    val id: Int,
    val userId: String?,
    val latestMessage: String?,
    val agentId: String?,
    val timeStamp: String?,
    val oTimeStamp: String?,
    val threadId: Int?
)