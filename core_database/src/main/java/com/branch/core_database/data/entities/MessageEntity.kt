package com.branch.core_database.data.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity( tableName = "messages" )
data class MessageEntity(
    @PrimaryKey
    val id: Int? = null,
    val threadId: Int?,
    val userId : String?,
    val body: String?,
    val timeStamp: String?,
    val agentId: String?
)