package com.vito.app.data.repository

import com.vito.app.domain.model.User
import kotlinx.coroutines.flow.StateFlow

interface AuthRepository {
    suspend fun login(username: String, password: String): Result<User>
    suspend fun logout()
    fun isLoggedIn(): StateFlow<Boolean>
    fun getCurrentUser(): StateFlow<User?>
}
