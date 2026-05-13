package com.vito.app.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.vito.app.data.local.entity.CartItemEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface CartDao {
    @Query("SELECT * FROM cart_items")
    fun getCartItems(): Flow<List<CartItemEntity>>
    
    @Query("SELECT SUM(price * quantity) FROM cart_items")
    fun getCartTotal(): Flow<Double?>
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(item: CartItemEntity)
    
    @Query("DELETE FROM cart_items WHERE productId = :productId")
    suspend fun delete(productId: String)
    
    @Query("DELETE FROM cart_items")
    suspend fun clearCart()
}