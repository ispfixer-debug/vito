package com.vito.app.presentation.ui.screens.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vito.app.data.repository.AuthRepository
import com.vito.app.data.repository.UserRepository
import com.vito.app.domain.model.User
import com.vito.app.domain.model.UserRole
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

data class PState(
    val balance: Double = 25.0,
    val appLock: Boolean = false,
    val role: UserRole = UserRole.CLIENT,
    val paymentMethod: String = "Cash"
)

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val auth: AuthRepository,
    private val user: UserRepository
) : ViewModel() {
    private val _s = MutableStateFlow(PState())
    val s: StateFlow<PState> = _s.asStateFlow()

    private val _currentUser = MutableStateFlow<User?>(null)
    val currentUser: StateFlow<User?> = _currentUser.asStateFlow()

    init {
        viewModelScope.launch {
            auth.getCurrentUser().collect { user ->
                _currentUser.value = user
            }
        }
        viewModelScope.launch {
            user.getWalletBalance().collect { balance ->
                _s.value = _s.value.copy(balance = balance)
            }
        }
    }

    fun setRole(r: UserRole) {
        viewModelScope.launch {
            user.updateRole(r)
            _s.value = _s.value.copy(role = r)
        }
    }

    fun toggleLock() {
        _s.value = _s.value.copy(appLock = !_s.value.appLock)
    }

    fun topUp(a: Double) {
        viewModelScope.launch {
            user.topUpWallet(a)
            _s.value = _s.value.copy(balance = _s.value.balance + a)
        }
    }

    fun setPaymentMethod(method: String) {
        _s.value = _s.value.copy(paymentMethod = method)
    }
    
    fun deleteAccount() {
        viewModelScope.launch {
            user.deleteAccount()
            auth.logout()
        }
    }

    fun logout() {
        viewModelScope.launch { auth.logout() }
    }
}
