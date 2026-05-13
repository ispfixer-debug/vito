package com.vito.app.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.vito.app.data.local.entity.PackageEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface PackageDao {
    @Query("SELECT * FROM packages WHERE userId = :userId ORDER BY createdAt DESC")
    fun getPackages(userId: String): Flow<List<PackageEntity>>
    
    @Query("SELECT * FROM packages WHERE id = :packageId")
    suspend fun getPackage(packageId: String): PackageEntity?
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(pkg: PackageEntity)
    
    @Update
    suspend fun update(pkg: PackageEntity)
}