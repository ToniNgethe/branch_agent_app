package com.branch.core_database.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.branch.core_database.data.entities.MessageEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface MessagesDao {
    @Insert
    suspend fun insertAll(messages: List<MessageEntity>)

    @Query("SELECT * FROM messages")
    fun fetchAllMessages(): Flow<List<MessageEntity>>

    @Query("SELECT * FROM messages WHERE threadId=:threadId")
    fun getMessagesByThread(threadId: String): Flow<List<MessageEntity>>

    @Query("DELETE FROM messages")
    suspend fun deleteAllMessages()
}