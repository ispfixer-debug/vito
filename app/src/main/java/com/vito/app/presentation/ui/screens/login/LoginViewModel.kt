package com.vito.app.presentation.ui.screens.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vito.app.data.repository.AuthRepository
import com.vito.app.domain.model.User
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

data class LoginUiState(
    val isLoading: Boolean = false,
    val error: String? = null,
    val isSuccess: Boolean = false
)

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val authRepository: AuthRepository
) : ViewModel() {
    
    private val _uiState = MutableStateFlow(LoginUiState())
    val uiState: StateFlow<LoginUiState> = _uiState.asStateFlow()
    
    val isLoggedIn = authRepository.isLoggedIn()
    
    fun login(username: String, password: String) {
        if (username.isBlank() || password.isBlank()) {
            _uiState.value = _uiState.value.copy(error = "Please enter username and password")
            return
        }
        
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, error = null)
            
            authRepository.login(username, password)
                .onSuccess {
                    _uiState.value = _uiState.value.copy(isLoading = false, isSuccess = true)
                }
                .onFailure { e ->
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        error = e.message ?: "Login failed"
                    )
                }
        }
    }
    
    fun logout() {
        viewModelScope.launch {
            authRepository.logout()
        }
    }
    
    fun clearError() {
        _uiState.value = _uiState.value.copy(error = null)
    }
}
