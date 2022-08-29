package com.branch.feature_chats.api

import com.branch.feature_chats.data.api.MessageApi
import com.google.common.truth.Truth
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import kotlinx.coroutines.test.runTest
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.junit.After
import org.junit.Before
import org.junit.Test
import retrofit2.Retrofit

class MessageApiTest {
    private lateinit var mockWebServer: MockWebServer
    private lateinit var messageApi: MessageApi

    private val client = OkHttpClient.Builder().build()
    private val kotlinJson = Json {
        ignoreUnknownKeys = true
    }

    val sampleMessagesResponse = """
    [
      {
        "id": 5,
        "thread_id": 2,
        "user_id": "218",
        "body": "I said ill pay 5th esther camoon.. Infact you guys took a week to give me a loan and just cant wait 4days for me to pay back??",
        "timestamp": "2017-02-01T16:08:21.000Z",
        "agent_id": null
    }
    ]
    """.trimIndent()

    @Before
    fun setUp() {
        mockWebServer = MockWebServer()

        messageApi = Retrofit.Builder().baseUrl(mockWebServer.url("/")).client(client)
            .addConverterFactory(kotlinJson.asConverterFactory("application/json".toMediaType()))
            .build().create(MessageApi::class.java)
    }

    @After
    fun shutdownServer() {
        mockWebServer.shutdown()
    }

    @Test
    fun `should parse list of messages`() = runTest{
        // given
        val response = MockResponse().setBody(sampleMessagesResponse).setResponseCode(200)
        //when
        mockWebServer.enqueue(response)
        // act
        val apiResponse = messageApi.fetchMessages()

        Truth.assertThat( apiResponse ).isNotEmpty()
        Truth.assertThat( apiResponse.size == 1 ).isTrue()
    }

}