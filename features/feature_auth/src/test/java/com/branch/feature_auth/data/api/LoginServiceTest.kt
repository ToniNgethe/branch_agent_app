package com.branch.feature_auth.data.api

import com.branch.feature_auth.data.dto.LoginRequestDto
import com.branch.feature_auth.data.dto.LoginResponseDto
import com.google.common.truth.Truth
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.junit.After
import org.junit.Before
import org.junit.Test
import retrofit2.Retrofit


@OptIn(ExperimentalCoroutinesApi::class)
class LoginServiceTest {
    private lateinit var mockWebServer: MockWebServer
    private lateinit var loginService: LoginService

    private val client = OkHttpClient.Builder().build()
    private val kotlinJson = Json {
        ignoreUnknownKeys = true
    }

    val successfulResponse = """
   {
    "auth_token": "XsGMP4zHyhfu63WDWQv3cA"
    }
    """.trimIndent()

    @OptIn(ExperimentalSerializationApi::class)
    @Before
    fun setUp() {
        mockWebServer = MockWebServer()

        loginService = Retrofit.Builder().baseUrl(mockWebServer.url("/")).client(client)
            .addConverterFactory(kotlinJson.asConverterFactory("application/json".toMediaType()))
            .build().create(LoginService::class.java)
    }

    @After
    fun shutdownServer() {
        mockWebServer.shutdown()
    }

    @Test
    fun `should return parse correct response given the request is a succces`() = runTest {
        // given
        val response = MockResponse().setBody(successfulResponse).setResponseCode(200)
        //when
        mockWebServer.enqueue(response)
        // act
        val apiResponse = loginService.loginUser(LoginRequestDto("toni@gmail.com", ""))

        Truth.assertThat(apiResponse).isInstanceOf(LoginResponseDto::class.java)
        Truth.assertThat(apiResponse.authToken).isEqualTo("XsGMP4zHyhfu63WDWQv3cA")
    }

}