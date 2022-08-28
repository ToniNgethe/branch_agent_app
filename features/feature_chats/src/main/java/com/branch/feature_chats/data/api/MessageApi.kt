package com.branch.feature_chats.data.api

import com.branch.feature_chats.data.dto.MessagesResponseDto
import retrofit2.http.GET

interface MessageApi {

    @GET("messages")
    suspend fun fetchMessages(): List<MessagesResponseDto>
}