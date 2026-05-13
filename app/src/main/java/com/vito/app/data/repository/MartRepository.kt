package com.vito.app.data.repository

import com.vito.app.domain.model.*
import kotlinx.coroutines.flow.Flow

interface MartRepository {
    fun getProducts(): Flow<List<Product>>
    fun getCart(): Flow<List<CartItem>>
    suspend fun addToCart(product: Product): Result<Unit>
    suspend fun removeFromCart(productId: String): Result<Unit>
    suspend fun createOrder(): Result<String>
    fun getOrderHistory(): Flow<List<MartOrder>>
}
