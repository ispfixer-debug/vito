package com.vito.app.data.mock

import com.vito.app.data.repository.QrAuthRepository
import com.vito.app.domain.model.User
import com.vito.app.domain.model.UserRole
import kotlinx.coroutines.delay
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MockQrAuthRepository @Inject constructor() : QrAuthRepository {

    private val validTokens = listOf(
        "CLIENT_TOKEN_001", "CLIENT_TOKEN_002",
        "DRIVER_TOKEN_001", "ADMIN_TOKEN_001",
        "RECOVERY_TOKEN_001", "LINK_TOKEN_001"
    )

    override suspend fun registerWithToken(token: String): Result<User> {
        return try {
            delay(500)
            when {
                token !in validTokens -> Result.failure(Exception("Invalid token"))
                token.startsWith("DRIVER") -> Result.success(
                    User(
                        id = "new_driver_${System.currentTimeMillis()}",
                        username = "newdriver",
                        alias = "NewDriver",
                        email = null,
                        phone = null,
                        photoUrl = null,
                        walletBalance = 0.0,
                        role = UserRole.DRIVER,
                        rating = 5.0
                    )
                )
                token.startsWith("ADMIN") -> Result.success(
                    User(
                        id = "new_admin_${System.currentTimeMillis()}",
                        username = "newadmin",
                        alias = "NewAdmin", 
                        email = null,
                        phone = null,
                        photoUrl = null,
                        walletBalance = 0.0,
                        role = UserRole.ADMIN,
                        rating = 5.0
                    )
                )
                else -> Result.success(
                    User(
                        id = "new_user_${System.currentTimeMillis()}",
                        username = "newuser",
                        alias = "NewUser",
                        email = null,
                        phone = null, 
                        photoUrl = null,
                        walletBalance = 25.0,
                        role = UserRole.CLIENT,
                        rating = 5.0
                    )
                )
            }
        } catch (e: Exception) {
            Result.failure(Exception("Registration failed: ${e.message}"))
        }
    }

    override suspend fun linkDeviceWithToken(token: String): Result<Unit> {
        return try {
            delay(500)
            if (token.startsWith("LINK") && token in validTokens) {
                Result.success(Unit)
            } else {
                Result.failure(Exception("Invalid device linking token"))
            }
        } catch (e: Exception) {
            Result.failure(Exception("Device linking failed: ${e.message}"))
        }
    }

    override suspend fun recoverAccountWithToken(token: String): Result<User> {
        return try {
            delay(500)
            when {
                token.startsWith("RECOVERY") && token in validTokens -> Result.success(
                    User(
                        id = "recovered_user_${System.currentTimeMillis()}",
                        username = "recovereduser",
                        alias = "RecoveredUser",
                        email = null,
                        phone = null,
                        photoUrl = null,
                        walletBalance = 25.0,
                        role = UserRole.CLIENT,
                        rating = 5.0
                    )
                )
                else -> Result.failure(Exception("Invalid recovery token"))
            }
        } catch (e: Exception) {
            Result.failure(Exception("Account recovery failed: ${e.message}"))
        }
    }
}
