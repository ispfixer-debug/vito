package com.vito.app.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.vito.app.data.local.entity.UserEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface UserDao {
    @Query("SELECT * FROM users WHERE id = :userId")
    fun getUser(userId: String): Flow<UserEntity?>
    
    @Query("SELECT * FROM users WHERE id = :userId")
    suspend fun getUserSync(userId: String): UserEntity?
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(user: UserEntity)
    
    @Query("UPDATE users SET role = :role WHERE id = :userId")
    suspend fun updateRole(userId: String, role: String)
    
    @Query("UPDATE users SET walletBalance = :balance WHERE id = :userId")
    suspend fun updateWalletBalance(userId: String, balance: Double)
}