package com.vito.app.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.vito.app.data.local.entity.TripEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface TripDao {
    @Query("SELECT * FROM trips WHERE userId = :userId ORDER BY createdAt DESC")
    fun getTrips(userId: String): Flow<List<TripEntity>>
    
    @Query("SELECT * FROM trips WHERE id = :tripId")
    suspend fun getTrip(tripId: String): TripEntity?
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(trip: TripEntity)
    
    @Update
    suspend fun update(trip: TripEntity)
    
    @Query("DELETE FROM trips WHERE id = :tripId")
    suspend fun delete(tripId: String)
}