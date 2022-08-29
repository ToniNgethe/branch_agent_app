package com.branch.feature_chats.data.api

import com.branch.feature_chats.data.dto.MessageRequestDto
import com.branch.feature_chats.data.dto.MessagesResponseDto
import kotlinx.serialization.json.JsonObject
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface MessageApi {

    @GET("messages")
    suspend fun fetchMessages(): List<MessagesResponseDto>


    @POST("messages")
    suspend fun createMessage(@Body request: MessageRequestDto): JsonObject
}