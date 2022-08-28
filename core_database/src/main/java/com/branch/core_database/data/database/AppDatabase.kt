package com.branch.core_database.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.branch.core_database.data.dao.MessagesDao
import com.branch.core_database.data.entities.MessageEntity

@Database(version = 1, entities = [MessageEntity::class])
abstract class AppDatabase : RoomDatabase() {
    abstract fun provideMessagesDao(): MessagesDao
}