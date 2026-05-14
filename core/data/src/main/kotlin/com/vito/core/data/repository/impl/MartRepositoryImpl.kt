package com.vito.core.data.repository.impl

import com.vito.core.data.model.MartProductDto
import com.vito.core.data.model.MartOrderDto
import com.vito.core.data.model.MartOrderItemDto
import com.vito.core.data.repository.MartRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import java.util.UUID
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MartRepositoryImpl @Inject constructor() : MartRepository {
    private val products = MutableStateFlow<List<MartProductDto>>(
        listOf(
            MartProductDto(id = "p1", name = "Water (1L)", price = 1.00, category = "Beverages", imageUrl = ""),
            MartProductDto(id = "p2", name = "Chips", price = 2.50, category = "Snacks", imageUrl = ""),
            MartProductDto(id = "p3", name = "Coffee", price = 3.50, category = "Beverages", imageUrl = ""),
            MartProductDto(id = "p4", name = "Sandwich", price = 5.00, category = "Food", imageUrl = "")
        )
    )
    private val orders = MutableStateFlow<List<MartOrderDto>>(emptyList())

    override fun getProducts(): Flow<List<MartProductDto>> = products

    override fun getProduct(productId: String): Flow<MartProductDto?> {
        return MutableStateFlow(products.value.find { it.id == productId })
    }

    override fun getCategoryProducts(category: String): Flow<List<MartProductDto>> {
        return MutableStateFlow(products.value.filter { it.category == category })
    }

    override fun getOrders(userId: String): Flow<List<MartOrderDto>> {
        return MutableStateFlow(orders.value.filter { it.userId == userId })
    }

    override suspend fun createOrder(order: MartOrderDto): Result<String> = try {
        val orderId = "order_${UUID.randomUUID()}"
        val newOrder = order.copy(id = orderId)
        orders.value = orders.value + newOrder
        Result.success(orderId)
    } catch (e: Exception) {
        Result.failure(e)
    }

    override suspend fun updateOrderStatus(orderId: String, status: String): Result<Unit> = try {
        orders.value = orders.value.map {
            if (it.id == orderId) it.copy(status = status)
            else it
        }
        Result.success(Unit)
    } catch (e: Exception) {
        Result.failure(e)
    }

    override suspend fun rateOrder(orderId: String, rating: Int, review: String?): Result<Unit> = try {
        orders.value = orders.value.map {
            if (it.id == orderId) it.copy(rating = rating, review = review)
            else it
        }
        Result.success(Unit)
    } catch (e: Exception) {
        Result.failure(e)
    }

    override fun getCurrentUserId(): String? = null
}