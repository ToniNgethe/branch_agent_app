package com.branch.feature_auth.domain.repository

import com.branch.core_network.data.ResponseState

interface LoginRepo {
    suspend fun loginUser(userName: String, password: String): ResponseState<String>
}