package com.vito.core.data.repository.impl

import com.vito.core.data.model.QrTokenDto
import com.vito.core.data.repository.QrRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import java.util.UUID
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class QrRepositoryImpl @Inject constructor() : QrRepository {
    private val tokens = MutableStateFlow<Map<String, QrTokenDto>>(emptyMap())

    override fun generateQrToken(userId: String, role: String): Result<String> = try {
        val token = "qr_${UUID.randomUUID()}"
        val qrToken = QrTokenDto(
            token = token,
            userId = userId,
            role = role,
            createdAt = System.currentTimeMillis(),
            expiresAt = System.currentTimeMillis() + 3600000 // 1 hour
        )
        tokens.value = tokens.value + (token to qrToken)
        Result.success(token)
    } catch (e: Exception) {
        Result.failure(e)
    }

    override fun consumeQrToken(token: String): Result<QrTokenDto> = try {
        val qrToken = tokens.value[token]
        if (qrToken == null) {
            Result.failure(Exception("Invalid token"))
        } else if (qrToken.expiresAt < System.currentTimeMillis()) {
            Result.failure(Exception("Token expired"))
        } else if (qrToken.isUsed) {
            Result.failure(Exception("Token already used"))
        } else {
            // Mark as used
            tokens.value = tokens.value + (token to qrToken.copy(isUsed = true))
            Result.success(qrToken)
        }
    } catch (e: Exception) {
        Result.failure(e)
    }

    override fun getQrToken(token: String): Flow<QrTokenDto?> {
        return MutableStateFlow(tokens.value[token])
    }
}