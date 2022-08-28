package com.branch.feature_auth.data.repository

import com.branch.core_database.domain.AppDataStore
import com.branch.core_network.data.ResponseState
import com.branch.feature_auth.data.api.LoginService
import com.branch.feature_auth.data.dto.LoginRequestDto
import com.branch.feature_auth.domain.repository.LoginRepo
import retrofit2.HttpException
import javax.inject.Inject

class LoginRepoImple @Inject constructor(
    private val loginService: LoginService, val appDataStore: AppDataStore
) : LoginRepo {
    override suspend fun loginUser(userName: String, password: String): ResponseState<String> {
        return try {
            val response = loginService.loginUser(LoginRequestDto(userName, password))
            if (response.authToken != null) {
                // store token
                appDataStore.saveToken(response.authToken)
            }
            ResponseState.Success("Login was a success")
        } catch (e: HttpException) {
            ResponseState.Error("Username or password is invalid.")
        } catch (e: Exception) {
            e.printStackTrace()
            ResponseState.Error(
                "Something went wrong with your request! Kindly try again"
            )
        }
    }
}