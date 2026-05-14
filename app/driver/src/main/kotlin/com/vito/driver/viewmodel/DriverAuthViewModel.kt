package com.vito.driver.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vito.core.data.model.UserDto
import com.vito.core.data.repository.AuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

data class DriverAuthState(
    val isLoggedIn: Boolean = false,
    val isOnline: Boolean = false,
    val isLoading: Boolean = false,
    val error: String? = null,
    val user: UserDto? = null
)

@HiltViewModel
class DriverAuthViewModel @Inject constructor(
    private val authRepository: AuthRepository
) : ViewModel() {

    private val _authState = MutableStateFlow(DriverAuthState())
    val authState: StateFlow<DriverAuthState> = _authState.asStateFlow()

    init {
        viewModelScope.launch {
            authRepository.currentUser.collect { user ->
                _authState.update { it.copy(user = user, isLoggedIn = user != null) }
            }
        }
    }

    fun loginWithDemo() {
        viewModelScope.launch {
            _authState.update { it.copy(isLoading = true, error = null) }
            authRepository.signInWithCustomToken("demo").fold(
                onSuccess = { user ->
                    val driverUser = user.copy(role = "DRIVER")
                    _authState.update { it.copy(isLoading = false, user = driverUser, isLoggedIn = true) }
                },
                onFailure = { e ->
                    _authState.update { it.copy(isLoading = false, error = e.message) }
                }
            )
        }
    }

    fun setOnline(online: Boolean) {
        _authState.update { it.copy(isOnline = online) }
    }

    fun logout() {
        viewModelScope.launch {
            authRepository.signOut()
            _authState.update { DriverAuthState() }
        }
    }
}