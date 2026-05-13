package com.vito.app.data.mock

import com.vito.app.data.repository.AuthRepository
import com.vito.app.domain.model.User
import com.vito.app.domain.model.UserRole
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MockAuthRepository @Inject constructor() : AuthRepository {
    private val _isLoggedIn = MutableStateFlow(false)
    private val _currentUser = MutableStateFlow<User?>(null)

    override suspend fun login(username: String, password: String): Result<User> {
        delay(500)
        return if (username == "oussama" && password == "oussama") {
            val user = User(
                id = "user1",
                username = username,
                alias = "Oussama",
                email = "oussama@example.com",
                phone = null,
                photoUrl = "https://randomuser.me/api/portraits/men/10.jpg",
                walletBalance = 25.0,
                role = UserRole.CLIENT,
                rating = 5.0
            )
            _currentUser.value = user
            _isLoggedIn.value = true
            Result.success(user)
        } else {
            Result.failure(Exception("Invalid credentials"))
        }
    }

    override suspend fun logout() {
        _isLoggedIn.value = false
        _currentUser.value = null
    }

    override fun isLoggedIn(): StateFlow<Boolean> = _isLoggedIn.asStateFlow()

    override fun getCurrentUser(): StateFlow<User?> = _currentUser.asStateFlow()
}
