package com.branch.feature_splash.presentation

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.branch.core_database.domain.AppDataStore
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class SplashPageUiState(
    val userIsLoggedIn: Boolean? = false, val isLoading: Boolean? = false
)

@HiltViewModel
class SplashVm @Inject constructor(val appDataStore: AppDataStore) : ViewModel() {
    private val _splashScreenUiState = MutableStateFlow(SplashPageUiState())
    val splashScreenUiState = _splashScreenUiState.asStateFlow()

    init {
        checkUserSession()
    }

    private fun checkUserSession() {
        _splashScreenUiState.update { it.copy(isLoading = true, userIsLoggedIn = false) }
        viewModelScope.launch {
            appDataStore.getToken().collectLatest { token ->
                Log.e("---->$$$ " ,
                    token.toString())
                if (token != null) {
                    _splashScreenUiState.update {
                        it.copy(
                            userIsLoggedIn = true, isLoading = false
                        )
                    }
                } else {
                    _splashScreenUiState.update {
                        it.copy(
                            userIsLoggedIn = false, isLoading = false
                        )
                    }
                }
            }
        }
    }
}