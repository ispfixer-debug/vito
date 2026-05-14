package com.vito.client.vitoride

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.vito.core.domain.model.VehicleType
import com.vito.core.domain.model.VehicleTypeSelector
import com.vito.core.domain.model.RideStatus
import com.vito.core.domain.model.StatusTimeline
import com.vito.core.domain.model.getDefaultTimelineEvents
import com.vito.core.domain.model.DriverCard

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun VitoRideHomeScreen(
    onRequestRide: (VehicleType) -> Unit
) {
    var selectedVehicle by remember { mutableStateOf(VehicleType.STANDARD) }
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Request a Ride")
        VehicleTypeSelector(
            selected = selectedVehicle,
            onSelect = { selectedVehicle = it }
        )
    }
}
