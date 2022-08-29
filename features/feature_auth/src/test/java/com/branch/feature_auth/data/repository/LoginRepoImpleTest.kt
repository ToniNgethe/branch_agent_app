package com.branch.feature_auth.data.repository

import com.branch.core_database.domain.AppDataStore
import com.branch.core_network.data.ResponseState
import com.branch.feature_auth.data.CustomCoroutineRule
import com.branch.feature_auth.data.api.LoginService
import com.branch.feature_auth.data.dto.LoginRequestDto
import com.branch.feature_auth.data.dto.LoginResponseDto
import com.branch.feature_auth.domain.repository.LoginRepo
import com.google.common.truth.Truth
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class LoginRepoImpleTest {
    private lateinit var loginRepo: LoginRepo
    private lateinit var loginService: LoginService
    private lateinit var appDataStore: AppDataStore

    @get:Rule
    val customCoroutineRule = CustomCoroutineRule()

    @Before
    fun setUp() {
        loginService = mockk()
        appDataStore = mockk()

        loginRepo = LoginRepoImple(loginService, appDataStore)
    }

    @Test
    fun `should return a success message given the login request was a success`() = runTest {
        // given
        val loginRequest = LoginRequestDto("toni@gmail.com", "password")
        val loginResponseDto = LoginResponseDto(authToken = "token")
        //when
        coEvery { loginService.loginUser(loginRequest) } returns loginResponseDto
        coEvery { appDataStore.saveToken("token") } returns Unit

        //act
        val resp = loginRepo.loginUser(loginRequest.username, loginRequest.password)

        Truth.assertThat((resp as ResponseState.Success).data).isEqualTo("Login was a success")
    }

    @Test
    fun `should return an error when credentials are invalid`() = runTest {
        // given
        val loginRequest = LoginRequestDto("toni@gmail.com", "password")
        val loginResponseDto = LoginResponseDto(authToken = "token")
        //when
        coEvery { loginService.loginUser(loginRequest) } throws Exception("Something went wrong")

        //act
        val resp = loginRepo.loginUser(loginRequest.username, loginRequest.password)

        Truth.assertThat(resp).isInstanceOf(ResponseState.Error::class.java)
    }

}