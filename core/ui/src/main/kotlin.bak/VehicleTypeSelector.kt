package com.vito.core.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DirectionsCar
import androidx.compose.material.icons.filled.DirectionsCarFilled
import androidx.compose.material.icons.filled.LocalTaxi
import androidx.compose.material.icons.filled.TwoWheeler
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp

enum class VehicleType(
    val id: String,
    val displayName: String,
    val icon: ImageVector,
    val farePerKm: Double,
    val baseFare: Double,
    val minFare: Double,
    val description: String
) {
    BIKE(
        "bike", "Bike", Icons.Default.TwoWheeler,
        1.5, 3.0, 5.0, "Fastest for short trips"
    ),
    CAR(
        "car", "Car", Icons.Default.DirectionsCar,
        2.5, 5.0, 10.0, "Comfortable for 4"
    ),
    TAXI(
        "taxi", "Premium", Icons.Default.LocalTaxi,
        4.0, 8.0, 20.0, "Luxury experience"
    )
}

@Composable
fun VehicleTypeSelector(
    selectedType: VehicleType,
    distanceKm: Double,
    onTypeSelected: (VehicleType) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier) {
        Text(
            text = "Select Vehicle",
            style = MaterialTheme.typography.titleMedium
        )
        Spacer(modifier = Modifier.height(12.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            VehicleType.entries.forEach { type ->
                val isSelected = type == selectedType
                val estimatedFare = calculateFare(type, distanceKm)
                
                Surface(
                    modifier = Modifier
                        .weight(1f)
                        .clip(RoundedCornerShape(12.dp))
                        .clickable { onTypeSelected(type) },
                    shape = RoundedCornerShape(12.dp),
                    color = if (isSelected) 
                        MaterialTheme.colorScheme.primaryContainer 
                    else MaterialTheme.colorScheme.surfaceVariant,
                    onClick = { onTypeSelected(type) }
                ) {
                    Column(
                        modifier = Modifier.padding(12.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Icon(
                            imageVector = type.icon,
                            contentDescription = type.name,
                            modifier = Modifier.size(32.dp),
                            tint = if (isSelected) 
                                MaterialTheme.colorScheme.onPrimaryContainer 
                            else MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = type.name,
                            style = MaterialTheme.typography.labelMedium,
                            color = if (isSelected) 
                                MaterialTheme.colorScheme.onPrimaryContainer 
                            else MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        Text(
                            text = "$${String.format("%.2f", estimatedFare)}",
                            style = MaterialTheme.typography.titleSmall,
                            color = if (isSelected) 
                                MaterialTheme.colorScheme.onPrimaryContainer 
                            else MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }
        }
    }
}

fun calculateFare(vehicleType: VehicleType, distanceKm: Double): Double {
    val fare = vehicleType.baseFare + (distanceKm * vehicleType.farePerKm)
    return maxOf(fare, vehicleType.minFare)
}