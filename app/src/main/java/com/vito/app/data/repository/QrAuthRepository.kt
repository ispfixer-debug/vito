package com.vito.app.data.repository

import com.vito.app.domain.model.User

/**
 * Repository interface for QR-based authentication
 */
interface QrAuthRepository {
    /**
     * Register a new user using a QR token
     * @param token The QR token from registration QR code
     * @return Result containing the authenticated User or failure
     */
    suspend fun registerWithToken(token: String): Result<User>
    
    /**
     * Link a new device using QR token
     * @param token The QR token from device linking QR code
     * @return Result indicating success or failure
     */
    suspend fun linkDeviceWithToken(token: String): Result<Unit>
    
    /**
     * Recover account using QR token
     * @param token The recovery QR token
     * @return Result indicating success or failure
     */
    suspend fun recoverAccountWithToken(token: String): Result<User>
}