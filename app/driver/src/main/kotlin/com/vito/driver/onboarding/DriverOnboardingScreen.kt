package com.vito.driver.onboarding

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DriverOnboardingScreen(
    vehiclePlate: String,
    vehicleType: String,
    carPhotoUri: String?,
    platePhotoUri: String?,
    onPlateChange: (String) -> Unit,
    onVehicleTypeChange: (String) -> Unit,
    onCarPhotoCapture: () -> Unit,
    onPlatePhotoCapture: () -> Unit,
    onSubmit: () -> Unit,
    modifier: Modifier = Modifier
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Become a Driver") }
            )
        }
    ) { padding ->
        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text(
                "Complete your driver profile",
                style = MaterialTheme.typography.headlineSmall
            )

            // Vehicle type
            Text("Vehicle Type", style = MaterialTheme.typography.titleMedium)
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                listOf("Car", "Bike", "Motorcycle").forEach { type ->
                    FilterChip(
                        selected = vehicleType == type,
                        onClick = { onVehicleTypeChange(type) },
                        label = { Text(type) },
                        modifier = Modifier.weight(1f)
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            // License plate
            OutlinedTextField(
                value = vehiclePlate,
                onValueChange = onPlateChange,
                label = { Text("License Plate") },
                placeholder = { Text("e.g., ABC-1234") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )

            // Car photo
            Text("Car Photo", style = MaterialTheme.typography.titleMedium)
            OutlinedButton(
                onClick = onCarPhotoCapture,
                modifier = Modifier.fillMaxWidth()
            ) {
                if (carPhotoUri != null) {
                    Icon(Icons.Default.Check, contentDescription = null)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Photo captured")
                } else {
                    Icon(Icons.Default.CameraAlt, contentDescription = null)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Take Photo")
                }
            }

            // Plate photo
            Text("License Plate Photo", style = MaterialTheme.typography.titleMedium)
            OutlinedButton(
                onClick = onPlatePhotoCapture,
                modifier = Modifier.fillMaxWidth()
            ) {
                if (platePhotoUri != null) {
                    Icon(Icons.Default.Check, contentDescription = null)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Photo captured")
                } else {
                    Icon(Icons.Default.CameraAlt, contentDescription = null)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Take Photo")
                }
            }

            Spacer(modifier = Modifier.weight(1f))

            // Submit
            Button(
                onClick = onSubmit,
                modifier = Modifier.fillMaxWidth(),
                enabled = vehiclePlate.isNotEmpty() && carPhotoUri != null && platePhotoUri != null
            ) {
                Text("Submit for Approval")
            }
        }
    }
}