package com.branch.feature_chats.di

import com.branch.core_database.data.dao.MessagesDao
import com.branch.core_utils.utils.AppDispatchers
import com.branch.feature_chats.data.api.MessageApi
import com.branch.feature_chats.data.reposotories.ChatRepositoryImpl
import com.branch.feature_chats.domain.repositories.ChatRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {

    @Provides
    @Singleton
    fun provideChatRepository(messageApi: MessageApi, messagesDao: MessagesDao, appDispatchers: AppDispatchers): ChatRepository =
        ChatRepositoryImpl(messageApi, messagesDao, appDispatchers)

}