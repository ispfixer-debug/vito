package com.vito.core.data.repository.impl

import com.vito.core.data.model.UserDto
import com.vito.core.data.repository.AuthRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AuthRepositoryImpl @Inject constructor() : AuthRepository {
    private val _currentUser = MutableStateFlow<UserDto?>(null)

    override val currentUser: Flow<UserDto?> = _currentUser

    override suspend fun signInWithCustomToken(token: String): Result<UserDto> = try {
        val user = UserDto(
            uid = "demo_client_${System.currentTimeMillis()}",
            role = "CLIENT",
            alias = "Demo User",
            phoneNumber = "+1234567890"
        )
        _currentUser.value = user
        Result.success(user)
    } catch (e: Exception) {
        Result.failure(e)
    }

    override suspend fun signInWithPhone(phone: String, code: String): Result<UserDto> = try {
        val user = UserDto(
            uid = "demo_client_${System.currentTimeMillis()}",
            role = "CLIENT",
            alias = "Demo User",
            phoneNumber = phone
        )
        _currentUser.value = user
        Result.success(user)
    } catch (e: Exception) {
        Result.failure(e)
    }

    override suspend fun signOut() {
        _currentUser.value = null
    }

    override fun getCurrentUserId(): String? = _currentUser.value?.uid
}