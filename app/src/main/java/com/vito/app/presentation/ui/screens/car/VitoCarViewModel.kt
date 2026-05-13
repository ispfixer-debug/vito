package com.vito.app.presentation.ui.screens.car

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vito.app.data.mock.MockData
import com.vito.app.data.mock.MockLocation
import com.vito.app.data.repository.RideRepository
import com.vito.app.domain.model.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.random.Random

sealed class RideUiState {
    data object Idle : RideUiState()
    data class FareEstimate(val fare: Double, val eta: Int) : RideUiState()
    data class Booking(val rideId: String, val fare: Double) : RideUiState()
    data class InProgress(val status: RideStatusType, val driverInfo: DriverInfo?, val eta: Int?) : RideUiState()
    data class Completed(val rideId: String) : RideUiState()
}

@HiltViewModel
class VitoCarViewModel @Inject constructor(
    private val rideRepository: RideRepository
) : ViewModel() {
    private val random = Random(System.currentTimeMillis())

    private val _uiState = MutableStateFlow<RideUiState>(RideUiState.Idle)
    val uiState: StateFlow<RideUiState> = _uiState.asStateFlow()

    private val _pickupLocation = MutableStateFlow(MockData.mockPickupLocations[0])
    val pickupLocation = _pickupLocation.asStateFlow()

    private val _dropoffLocation = MutableStateFlow<MockLocation?>(null)
    val dropoffLocation = _dropoffLocation.asStateFlow()

    private val _selectedVehicleType = MutableStateFlow("Standard")
    val selectedVehicleType = _selectedVehicleType.asStateFlow()

    val vehicleTypes = listOf("Standard", "Premium", "Van", "Executive")

    fun setPickupLocation(location: MockLocation) { _pickupLocation.value = location }
    fun setDropoffLocation(location: MockLocation) { _dropoffLocation.value = location }
    fun selectVehicleType(type: String) { _selectedVehicleType.value = type }

    fun requestRide() {
        val dropoff = _dropoffLocation.value ?: return
        viewModelScope.launch {
            _uiState.value = RideUiState.FareEstimate(
                random.nextDouble() * 20 + 10,
                random.nextInt(5, 15)
            )
            delay(1000)
            val rideRequest = RideRequest(
                pickupLocation = _pickupLocation.value.address,
                pickupLat = _pickupLocation.value.lat,
                pickupLng = _pickupLocation.value.lng,
                dropoffLocation = dropoff.address,
                dropoffLat = dropoff.lat,
                dropoffLng = dropoff.lng,
                vehicleType = _selectedVehicleType.value
            )
            rideRepository.requestRide(rideRequest).onSuccess { fare ->
                _uiState.value = RideUiState.Booking(
                    "ride_${random.nextInt(1000, 9999)}",
                    fare.fare
                )
            }
        }
    }

    fun confirmRide() {
        val state = _uiState.value
        if (state is RideUiState.Booking) {
            viewModelScope.launch {
                _uiState.value = RideUiState.InProgress(
                    RideStatusType.DRIVER_ARRIVING,
                    DriverInfo("driver1", "John Smith", "https://randomuser.me/api/portraits/men/1.jpg", "Toyota Camry (ABC-1234)", 4.8),
                    5
                )
            }
        }
    }

    fun cancelRide() {
        _uiState.value = RideUiState.Idle
        _dropoffLocation.value = null
    }
}
