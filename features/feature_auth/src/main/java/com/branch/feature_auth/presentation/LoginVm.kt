package com.branch.feature_auth.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.branch.core_network.data.ResponseState
import com.branch.feature_auth.domain.repository.LoginRepo
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class LoginUiState(
    val isLoading: Boolean = false, val onError: String? = null, val onSuccess: String? = null
)

@HiltViewModel
class LoginVm @Inject constructor(private val loginRepo: LoginRepo) : ViewModel() {
    private val _loginUiState = MutableStateFlow(LoginUiState())
    val loginUiState = _loginUiState.asStateFlow()

    fun loginUser(userName: String, password: String) {
        if (userName.isEmpty()) {
            _loginUiState.update { it.copy(onError = "Username is required") }
            return
        }
        if (password.isEmpty()) {
            _loginUiState.update { it.copy(onError = "Password is required") }
            return
        }
        _loginUiState.update { it.copy(isLoading = true, onError = null) }
        viewModelScope.launch {
            val response = loginRepo.loginUser(userName, password)
            when (response) {
                is ResponseState.Success -> {
                    _loginUiState.update {
                        it.copy(
                            onError = null, isLoading = false, onSuccess = response.data
                        )
                    }
                }
                is ResponseState.Error -> {
                    _loginUiState.update {
                        it.copy(
                            onError = response.error, isLoading = false, onSuccess = null
                        )
                    }
                }
            }
        }
    }
}