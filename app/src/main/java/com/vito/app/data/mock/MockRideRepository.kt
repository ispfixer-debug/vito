package com.vito.app.data.mock

import com.vito.app.data.repository.RideRepository
import com.vito.app.domain.model.*
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.random.Random

@Singleton
class MockRideRepository @Inject constructor() : RideRepository {
    private var currentRideId: String? = null
    private var rideStatus: RideStatusType = RideStatusType.IDLE
    private var driverLat = 0.0
    private var driverLng = 0.0
    private val random = Random(System.currentTimeMillis())

    override suspend fun requestRide(request: RideRequest): Result<RideFare> {
        delay(500)

        val fare = RideFare(
            fare = random.nextDouble() * 20 + 10,
            eta = random.nextInt(5, 15),
            distance = random.nextDouble() * 20 + 5
        )

        currentRideId = "ride_${System.currentTimeMillis()}"
        return Result.success(fare)
    }

    override fun getRideStatus(rideId: String): Flow<RideStatus> = flow {
        if (currentRideId != null) {
            val statuses = listOf(
                RideStatusType.ASSIGNED,
                RideStatusType.DRIVER_ARRIVING,
                RideStatusType.IN_PROGRESS,
                RideStatusType.COMPLETED
            )

            val driver = MockData.mockDrivers.random(random)
            emit(
                RideStatus(
                    status = statuses.random(random),
                    driverInfo = DriverInfo(
                        id = driver.id,
                        name = driver.name,
                        photoUrl = driver.photoUrl,
                        vehicleInfo = driver.vehicle,
                        rating = driver.rating
                    ),
                    eta = random.nextInt(1, 10),
                    driverLat = driverLat,
                    driverLng = driverLng
                )
            )
        } else {
            emit(RideStatus(RideStatusType.IDLE, null, null, null, null))
        }
    }

    override suspend fun cancelRide(rideId: String): Result<Unit> {
        delay(300)
        currentRideId = null
        return Result.success(Unit)
    }

    override suspend fun submitRating(rideId: String, rating: Int, tip: Double?): Result<Unit> {
        delay(300)
        return Result.success(Unit)
    }

    override fun getRideHistory(): Flow<List<TripHistoryItem>> = flow {
        emit(MockData.generateMockTrips().map { t -> com.vito.app.domain.model.TripHistoryItem(t.id, t.pickup, t.dropoff, t.fare, t.status, t.timestamp, t.driverName, t.tripStatus, t.eta) })
    }
}
