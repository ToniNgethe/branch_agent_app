package com.branch.feature_chats.di

import com.branch.feature_chats.data.api.MessageApi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object ChatsModule {

    @Provides
    @Singleton
    fun provideMessageApi(retrofit: Retrofit): MessageApi = retrofit.create(MessageApi::class.java)
}