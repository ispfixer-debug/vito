package com.vito.app.data.repository

import com.vito.app.domain.model.User
import com.vito.app.domain.model.UserRole
import kotlinx.coroutines.flow.Flow

interface UserRepository {
    fun getCurrentUser(): Flow<User?>
    suspend fun updateAlias(alias: String): Result<Unit>
    suspend fun updateRole(role: UserRole): Result<Unit>
    suspend fun topUpWallet(amount: Double): Result<Unit>
    fun getWalletBalance(): Flow<Double>
    suspend fun addCard(): Result<String> // Returns fake setup intent client secret
    suspend fun deleteAccount(): Result<Unit>
}