package com.branch.core_database.di

import android.content.Context
import androidx.room.Room
import androidx.room.RoomDatabase
import com.branch.core_database.data.AppDataStoreImpl
import com.branch.core_database.data.dao.MessagesDao
import com.branch.core_database.data.database.AppDatabase
import com.branch.core_database.domain.AppDataStore
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DataBaseModule {

    @Singleton
    @Provides
    fun provideDataStore(
        @ApplicationContext context: Context
    ): AppDataStore = AppDataStoreImpl(context)

    @Singleton
    @Provides
    fun provideDatabase(@ApplicationContext context: Context): AppDatabase =
        Room.databaseBuilder(context, AppDatabase::class.java, "branch").build()

    @Singleton
    @Provides
    fun provideMessagesDao(appDatabase: AppDatabase): MessagesDao = appDatabase.provideMessagesDao()
}