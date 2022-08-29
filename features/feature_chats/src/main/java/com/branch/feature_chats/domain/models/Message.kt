package com.branch.feature_chats.domain.models

data class Message(
    val id: Int?,
    val userId: String?,
    val message: String?,
    val agentId: String?,
    val timeStamp: String?
)