package com.vito.app

import com.vito.app.data.mock.MockAuthRepository
import com.vito.app.data.mock.MockQrAuthRepository
import com.vito.app.data.mock.MockMartRepository
import com.vito.app.data.mock.MockRideRepository
import com.vito.app.domain.model.User
import kotlinx.coroutines.test.runTest
import org.junit.Assert.*
import org.junit.Test

/**
 * Unit tests for Vito repositories
 */
class VitoUnitTests {

    @Test
    fun `MockAuthRepository login success`() = runTest {
        val repo = MockAuthRepository()
        val result = repo.login("oussama", "oussama")
        
        assertTrue(result.isSuccess)
        result.getOrNull()?.let { user ->
            assertEquals("user1", user.id)
            assertEquals("Oussama", user.alias)
            assertEquals("client", user.role)
        }
    }
    
    @Test
    fun `MockAuthRepository login failure`() = runTest {
        val repo = MockAuthRepository()
        val result = repo.login("wrong", "wrong")
        assertTrue(result.isFailure)
    }

    @Test
    fun `MockQrAuthRepository register success`() = runTest {
        val repo = MockQrAuthRepository()
        val result = repo.registerWithToken("CLIENT_TOKEN_001")
        assertTrue(result.isSuccess)
    }
    
    @Test
    fun `MockQrAuthRepository invalid token`() = runTest {
        val repo = MockQrAuthRepository()
        val result = repo.registerWithToken("INVALID")
        assertTrue(result.isFailure)
    }

    @Test
    fun `MockRideRepository get fare estimate`() = runTest {
        val repo = MockRideRepository()
        val estimate = repo.getFareEstimate(40.7128, -74.0060, 40.7580, -73.9855, "standard")
        assertNotNull(estimate)
        assertTrue(estimate!! > 0)
    }

    @Test
    fun `MockMartRepository get products`() = runTest {
        val repo = MockMartRepository()
        val products = repo.getProducts()
        assertTrue(products.isNotEmpty())
    }

    @Test
    fun `User model properties`() {
        val user = User(id = "test", alias = "Test", role = "client")
        assertEquals("test", user.id)
    }
}