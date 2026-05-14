package com.vito.core.data.repository.impl

import com.vito.core.data.model.RideDto
import com.vito.core.data.repository.RideRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import java.util.UUID
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class RideRepositoryImpl @Inject constructor() : RideRepository {
    private val rides = MutableStateFlow<List<RideDto>>(emptyList())

    override fun getRide(rideId: String): Flow<RideDto?> {
        return MutableStateFlow(rides.value.find { it.id == rideId })
    }

    override fun getUserRides(userId: String): Flow<List<RideDto>> {
        return MutableStateFlow(rides.value.filter { it.userId == userId })
    }

    override fun getDriverRides(driverId: String): Flow<List<RideDto>> {
        return MutableStateFlow(rides.value.filter { it.driverId == driverId })
    }

    override fun getAvailableRides(): Flow<List<RideDto>> {
        return MutableStateFlow(rides.value.filter { it.status == "PENDING" })
    }

    override suspend fun requestRide(ride: RideDto): Result<String> = try {
        val rideId = "ride_${UUID.randomUUID()}"
        val newRide = ride.copy(id = rideId)
        rides.value = rides.value + newRide
        Result.success(rideId)
    } catch (e: Exception) {
        Result.failure(e)
    }

    override suspend fun acceptRide(rideId: String, driverId: String): Result<Unit> = try {
        rides.value = rides.value.map {
            if (it.id == rideId) it.copy(driverId = driverId, status = "ACCEPTED")
            else it
        }
        Result.success(Unit)
    } catch (e: Exception) {
        Result.failure(e)
    }

    override suspend fun updateRideStatus(rideId: String, status: String): Result<Unit> = try {
        rides.value = rides.value.map {
            if (it.id == rideId) it.copy(status = status)
            else it
        }
        Result.success(Unit)
    } catch (e: Exception) {
        Result.failure(e)
    }

    override suspend fun completeRide(rideId: String, actualPrice: Double): Result<Unit> = try {
        rides.value = rides.value.map {
            if (it.id == rideId) it.copy(status = "COMPLETED", actualPrice = actualPrice)
            else it
        }
        Result.success(Unit)
    } catch (e: Exception) {
        Result.failure(e)
    }

    override suspend fun cancelRide(rideId: String): Result<Unit> = try {
        rides.value = rides.value.map {
            if (it.id == rideId) it.copy(status = "CANCELLED")
            else it
        }
        Result.success(Unit)
    } catch (e: Exception) {
        Result.failure(e)
    }

    override suspend fun rateRide(rideId: String, rating: Int, review: String?, tip: Double): Result<Unit> = try {
        rides.value = rides.value.map {
            if (it.id == rideId) it.copy(rating = rating, review = review, tip = tip)
            else it
        }
        Result.success(Unit)
    } catch (e: Exception) {
        Result.failure(e)
    }

    override fun getCurrentUserId(): String? = null
}