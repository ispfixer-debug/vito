package com.vito.app.data.repository

import com.vito.app.domain.model.*
import kotlinx.coroutines.flow.Flow

interface RideRepository {
    suspend fun requestRide(request: RideRequest): Result<RideFare>
    fun getRideStatus(rideId: String): Flow<RideStatus>
    suspend fun cancelRide(rideId: String): Result<Unit>
    suspend fun submitRating(rideId: String, rating: Int, tip: Double?): Result<Unit>
    fun getRideHistory(): Flow<List<TripHistoryItem>>
}
