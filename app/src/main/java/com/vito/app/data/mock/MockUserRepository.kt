package com.vito.app.data.mock

import com.vito.app.data.repository.UserRepository
import com.vito.app.domain.model.User
import com.vito.app.domain.model.UserRole
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MockUserRepository @Inject constructor(
    private val mockAuthRepository: MockAuthRepository
) : UserRepository {
    private val _walletBalance = MutableStateFlow(25.0)
    private val _currentRole = MutableStateFlow(UserRole.CLIENT)
    
    override fun getCurrentUser(): Flow<User?> = mockAuthRepository.getCurrentUser()
    
    override suspend fun updateAlias(alias: String): Result<Unit> {
        delay(300)
        return Result.success(Unit)
    }
    
    override suspend fun updateRole(role: UserRole): Result<Unit> {
        _currentRole.value = role
        return Result.success(Unit)
    }
    
    override suspend fun topUpWallet(amount: Double): Result<Unit> {
        delay(500)
        _walletBalance.value += amount
        return Result.success(Unit)
    }
    
    override fun getWalletBalance(): Flow<Double> = _walletBalance.asStateFlow()
    
    override suspend fun addCard(): Result<String> {
        delay(500)
        // Return fake setup intent client secret
        return Result.success("seti_mock_secret_123456")
    }
    
    override suspend fun deleteAccount(): Result<Unit> {
        delay(500)
        return Result.success(Unit)
    }
}