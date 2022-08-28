package com.branch.feature_auth.data.api

import com.branch.feature_auth.data.dto.LoginRequestDto
import com.branch.feature_auth.data.dto.LoginResponseDto
import retrofit2.http.Body
import retrofit2.http.POST

interface LoginService {
    @POST("login")
    suspend fun loginUser(@Body requestDto: LoginRequestDto): LoginResponseDto
}