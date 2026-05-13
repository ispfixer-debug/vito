package com.vito.app.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.vito.app.data.local.entity.MartOrderEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface MartOrderDao {
    @Query("SELECT * FROM mart_orders WHERE userId = :userId ORDER BY createdAt DESC")
    fun getOrders(userId: String): Flow<List<MartOrderEntity>>
    
    @Query("SELECT * FROM mart_orders WHERE id = :orderId")
    suspend fun getOrder(orderId: String): MartOrderEntity?
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(order: MartOrderEntity)
}