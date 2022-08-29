package com.branch.feature_auth.data.presentation

import app.cash.turbine.test
import com.branch.core_network.data.ResponseState
import com.branch.feature_auth.data.CustomCoroutineRule
import com.branch.feature_auth.domain.repository.LoginRepo
import com.branch.feature_auth.presentation.LoginVm
import com.google.common.truth.Truth
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class LoginVmTest {
    private lateinit var loginVm: LoginVm
    private lateinit var loginRepo: LoginRepo

    @get:Rule
    val customCoroutineRule = CustomCoroutineRule(dispatcher = StandardTestDispatcher())

    @Before
    fun setUp() {
        loginRepo = mockk()

        loginVm = LoginVm(loginRepo)
    }

    @Test
    fun `should return an error when username is empty`() = runTest {
        // given
        // when
        coEvery { loginRepo.loginUser("", "") } returns ResponseState.Success("Login successfully")
        // act
        loginVm.loginUser("", "")

        Truth.assertThat(loginVm.loginUiState.value.onError).isEqualTo("Username is required")
    }

    @Test
    fun `should return an error when password is empty`() = runTest {
        // given
        // when
        coEvery { loginRepo.loginUser("", "") } returns ResponseState.Success("Login successfully")
        // act
        loginVm.loginUser("toni@gmail.com", "")

        Truth.assertThat(loginVm.loginUiState.value.onError).isEqualTo("Password is required")
    }

    @Test
    fun `should display loading when request is being made`() = runTest {
        // given
        // when
        coEvery {
            loginRepo.loginUser(
                "toni@gmail.com", "toni toni"
            )
        } returns ResponseState.Success("Login successfully")

        // act
        loginVm.loginUser("toni@gmail.com", "toni toni")

        loginVm.loginUiState.test {
            Truth.assertThat(awaitItem().isLoading).isEqualTo(true)
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `should return success if login request is a success`() = runTest {
        // given
        // when
        coEvery {
            loginRepo.loginUser(
                "toni@gmail.com", "toni toni"
            )
        } returns ResponseState.Success("Login successfully")

        // act
        loginVm.loginUser("toni@gmail.com", "toni toni")

        loginVm.loginUiState.test {
            Truth.assertThat(awaitItem().isLoading).isEqualTo(true)
            val res  = awaitItem()
            Truth.assertThat(res.isLoading).isEqualTo(false)
            Truth.assertThat(res.onSuccess).isEqualTo("Login successfully")
            cancelAndIgnoreRemainingEvents()
        }
    }
}