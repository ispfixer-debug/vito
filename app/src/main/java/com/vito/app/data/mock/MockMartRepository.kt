package com.vito.app.data.mock

import com.vito.app.data.repository.MartRepository
import com.vito.app.domain.model.*
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MockMartRepository @Inject constructor() : MartRepository {
    private val cart = MutableStateFlow<List<CartItem>>(emptyList())

    override fun getProducts(): Flow<List<Product>> = flow {
        delay(300)
        emit(MockData.mockProducts.map { p -> Product(p.id, p.name, p.description, p.price, p.category, p.imageUrls, true) })
    }

    override fun getCart(): Flow<List<CartItem>> = cart

    override suspend fun addToCart(product: Product): Result<Unit> {
        val current = cart.value.toMutableList()
        val existing = current.find { it.productId == product.id }
        if (existing != null) {
            val idx = current.indexOf(existing)
            current[idx] = CartItem(product.id, product, existing.quantity + 1)
        } else {
            current.add(CartItem(product.id, product, 1))
        }
        cart.value = current
        return Result.success(Unit)
    }

    override suspend fun removeFromCart(productId: String): Result<Unit> {
        cart.value = cart.value.filter { it.productId != productId }
        return Result.success(Unit)
    }

    override suspend fun createOrder(): Result<String> {
        delay(500)
        return Result.success("order_${System.currentTimeMillis()}")
    }

    override fun getOrderHistory(): Flow<List<MartOrder>> = flow {
        emit(emptyList())
    }
}
