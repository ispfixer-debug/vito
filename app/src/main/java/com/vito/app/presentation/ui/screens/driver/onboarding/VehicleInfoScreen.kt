package com.vito.app.presentation.ui.screens.driver.onboarding

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.vito.app.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun VehicleInfoScreen(
    onSubmit: (String, String, String, String) -> Unit = { _, _, _, _ -> },
    onBack: () -> Unit = {}
) {
    var vehicleType by remember { mutableStateOf("") }
    var vehicleModel by remember { mutableStateOf("") }
    var vehicleYear by remember { mutableStateOf("") }
    var licensePlate by remember { mutableStateOf("") }
    var vehicleTypeExpanded by remember { mutableStateOf(false) }
    
    val vehicleTypes = listOf("Car", "Van", "Motorcycle", "Bike")
    val carModels = listOf("Toyota", "Honda", "Hyundai", "Ford", "Chevrolet", "Nissan")
    
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(stringResource(R.string.vehicle)) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Filled.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp)
                .verticalScroll(rememberScrollState())
        ) {
            // Vehicle Type
            Text(
                stringResource(R.string.package_size),
                style = MaterialTheme.typography.titleSmall
            )
            Spacer(Modifier.height(8.dp))
            ExposedDropdownMenuBox(
                expanded = vehicleTypeExpanded,
                onExpandedChange = { vehicleTypeExpanded = it }
            ) {
                OutlinedTextField(
                    vehicleType,
                    {},
                    readOnly = true,
                    label = { Text("Vehicle Type") },
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(vehicleTypeExpanded) },
                    modifier = Modifier.fillMaxWidth().menuAnchor()
                )
                ExposedDropdownMenu(
                    expanded = vehicleTypeExpanded,
                    onDismissRequest = { vehicleTypeExpanded = false }
                ) {
                    vehicleTypes.forEach { type ->
                        DropdownMenuItem(
                            text = { Text(type) },
                            onClick = {
                                vehicleType = type
                                vehicleTypeExpanded = false
                            }
                        )
                    }
                }
            }
            
            Spacer(Modifier.height(16.dp))
            
            // Vehicle Model
            OutlinedTextField(
                value = vehicleModel,
                onValueChange = { vehicleModel = it },
                label = { Text("Make / Model") },
                modifier = Modifier.fillMaxWidth()
            )
            
            Spacer(Modifier.height(16.dp))
            
            // Year
            OutlinedTextField(
                value = vehicleYear,
                onValueChange = { vehicleYear = it },
                label = { Text("Year") },
                modifier = Modifier.fillMaxWidth()
            )
            
            Spacer(Modifier.height(16.dp))
            
            // License Plate
            OutlinedTextField(
                value = licensePlate,
                onValueChange = { licensePlate = it },
                label = { Text("License Plate") },
                modifier = Modifier.fillMaxWidth()
            )
            
            Spacer(Modifier.height(32.dp))
            
            Button(
                onClick = { onSubmit(vehicleType, vehicleModel, vehicleYear, licensePlate) },
                modifier = Modifier.fillMaxWidth(),
                enabled = vehicleType.isNotEmpty() && vehicleModel.isNotEmpty() && 
                         vehicleYear.isNotEmpty() && licensePlate.isNotEmpty()
            ) {
                Text(stringResource(R.string.submit))
            }
        }
    }
}