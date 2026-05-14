package com.vito.core.data.repository.impl

import com.vito.core.data.model.UserDto
import com.vito.core.data.repository.UserRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import java.util.UUID
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UserRepositoryImpl @Inject constructor() : UserRepository {
    private val users = MutableStateFlow<List<UserDto>>(emptyList())

    override fun getUser(userId: String): Flow<UserDto?> {
        return MutableStateFlow(users.value.find { it.uid == userId })
    }

    override fun getUsers(): Flow<List<UserDto>> = users

    override fun searchUsers(query: String): Flow<List<UserDto>> {
        return MutableStateFlow(users.value.filter {
            it.alias?.contains(query, ignoreCase = true) == true ||
            it.phoneNumber?.contains(query) == true
        })
    }

    override suspend fun createUser(user: UserDto): Result<Unit> = try {
        users.value = users.value + user
        Result.success(Unit)
    } catch (e: Exception) {
        Result.failure(e)
    }

    override suspend fun updateUser(user: UserDto): Result<Unit> = try {
        users.value = users.value.map {
            if (it.uid == user.uid) user else it
        }
        Result.success(Unit)
    } catch (e: Exception) {
        Result.failure(e)
    }

    override suspend fun updateFcmToken(userId: String, token: String): Result<Unit> = try {
        users.value = users.value.map {
            if (it.uid == userId) it.copy(fcmToken = token) else it
        }
        Result.success(Unit)
    } catch (e: Exception) {
        Result.failure(e)
    }
}