package com.branch.feature_auth.data.dto


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class LoginResponseDto(
    @SerialName("auth_token") val authToken: String?
)