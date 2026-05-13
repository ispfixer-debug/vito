package com.vito.app.presentation.ui.screens.driver

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vito.app.data.mock.MockData
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

data class DriverJob(
    val id: String,
    val type: String,
    val pickup: String,
    val dropoff: String,
    val fare: Double,
    val eta: Int,
    val timeLeft: Int = 30
)

data class DriverUiState(
    val jobs: List<DriverJob> = emptyList(),
    val earnings: DriverEarnings = DriverEarnings(0.0, 0.0, 0.0),
    val currentJob: DriverJob? = null,
    val status: String = "idle"
)

data class DriverEarnings(
    val today: Double,
    val week: Double,
    val month: Double
)

@HiltViewModel
class DriverViewModel @Inject constructor() : ViewModel() {
    private val _uiState = MutableStateFlow(DriverUiState(
        jobs = MockData.generateMockTrips().map { trip ->
            DriverJob(trip.id, "Ride", trip.pickup, trip.dropoff, trip.fare, trip.eta)
        },
        earnings = DriverEarnings(50.0, 350.0, 1500.0)
    ))
    val uiState: StateFlow<DriverUiState> = _uiState.asStateFlow()

    fun acceptJob(job: DriverJob) {
        _uiState.value = _uiState.value.copy(currentJob = job, status = "accepted")
    }

    fun declineJob(job: DriverJob) {
        _uiState.value = _uiState.value.copy(
            jobs = _uiState.value.jobs.filter { it.id != job.id }
        )
    }

    fun updateStatus(status: String) {
        _uiState.value = _uiState.value.copy(status = status)
    }
}
