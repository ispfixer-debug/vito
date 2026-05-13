package com.vito.app.presentation.ui.screens.car

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.vito.app.data.mock.MockData

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun VitoCarScreen(viewModel: VitoCarViewModel = hiltViewModel()) {
    val uiState by viewModel.uiState.collectAsState()
    val pickup by viewModel.pickupLocation.collectAsState()
    val dropoff by viewModel.dropoffLocation.collectAsState()
    val selectedVehicle by viewModel.selectedVehicleType.collectAsState()
    
    var showPickupSheet by remember { mutableStateOf(false) }
    var showDropoffSheet by remember { mutableStateOf(false) }
    
    Box(modifier = Modifier.fillMaxSize()) {
        Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
            Card(modifier = Modifier.fillMaxWidth()) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(modifier = Modifier.fillMaxWidth().clickable { showPickupSheet = true }, verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Filled.Place, null, tint = MaterialTheme.colorScheme.primary)
                        Spacer(Modifier.width(8.dp))
                        Text(pickup.address, modifier = Modifier.weight(1f))
                    }
                    HorizontalDivider(Modifier.padding(vertical = 8.dp))
                    Row(modifier = Modifier.fillMaxWidth().clickable { showDropoffSheet = true }, verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Filled.Place, null, tint = MaterialTheme.colorScheme.error)
                        Spacer(Modifier.width(8.dp))
                        Text(dropoff?.address ?: "Where to?", modifier = Modifier.weight(1f))
                    }
                }
            }
            
            Spacer(Modifier.height(16.dp))
            
            LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                items(viewModel.vehicleTypes) { vehicle ->
                    FilterChip(selected = vehicle == selectedVehicle, onClick = { viewModel.selectVehicleType(vehicle) }, label = { Text(vehicle) })
                }
            }
            
            Spacer(Modifier.weight(1f))
            
            when (val state = uiState) {
                is RideUiState.Idle -> { dropoff?.let { Button(onClick = { viewModel.requestRide() }, modifier = Modifier.fillMaxWidth().height(56.dp)) { Text("Get Fare") } } }
                is RideUiState.FareEstimate -> { Column(horizontalAlignment = Alignment.CenterHorizontally) { Text("$${String.format("%.2f", state.fare)}", style = MaterialTheme.typography.headlineLarge); Button(onClick = { viewModel.confirmRide() }, modifier = Modifier.fillMaxWidth()) { Text("Confirm") } } }
                is RideUiState.InProgress -> { Column(horizontalAlignment = Alignment.CenterHorizontally) { state.driverInfo?.let { Text(it.name) }; OutlinedButton(onClick = { viewModel.cancelRide() }, modifier = Modifier.fillMaxWidth()) { Text("Cancel") } } }
                else -> {}
            }
        }
    }
    
    if (showPickupSheet) {
        ModalBottomSheet(onDismissRequest = { showPickupSheet = false }) {
            LazyColumn(Modifier.padding(16.dp)) { items(MockData.mockPickupLocations) { location -> ListItem(headlineContent = { Text(location.address) }, leadingContent = { Icon(Icons.Filled.Place, null) }, modifier = Modifier.clickable { viewModel.setPickupLocation(location); showPickupSheet = false }) } }
        }
    }
    
    if (showDropoffSheet) {
        ModalBottomSheet(onDismissRequest = { showDropoffSheet = false }) {
            LazyColumn(Modifier.padding(16.dp)) { items(MockData.mockDropoffLocations) { location -> ListItem(headlineContent = { Text(location.address) }, leadingContent = { Icon(Icons.Filled.Place, null) }, modifier = Modifier.clickable { viewModel.setDropoffLocation(location); showDropoffSheet = false }) } }
        }
    }
}
