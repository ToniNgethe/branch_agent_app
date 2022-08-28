package com.branch.feature_auth.data.dto

@kotlinx.serialization.Serializable
data class LoginRequestDto(
    val username: String, val password: String
)