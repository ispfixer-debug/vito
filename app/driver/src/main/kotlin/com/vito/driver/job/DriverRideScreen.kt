package com.vito.driver.job

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun DriverRideScreen(
    pickupLocation: String,
    dropoffLocation: String,
    fareAmount: Double,
    onArrived: () -> Unit,
    onStartTrip: () -> Unit,
    onComplete: () -> Unit,
    onCashPayment: () -> Unit,
    modifier: Modifier = Modifier
) {
    var status by remember { mutableStateOf("pending") }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        // Trip info
        Card(modifier = Modifier.fillMaxWidth()) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text("Trip Details", style = MaterialTheme.typography.titleMedium)
                Spacer(modifier = Modifier.height(8.dp))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.MyLocation, contentDescription = null, modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(pickupLocation)
                }
                Spacer(modifier = Modifier.height(4.dp))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.LocationOn, contentDescription = null, modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(dropoffLocation)
                }
                Spacer(modifier = Modifier.height(8.dp))
                Text("Fare: $${String.format("%.2f", fareAmount)}", style = MaterialTheme.typography.titleLarge)
            }
        }

        // Status buttons
        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            when (status) {
                "pending" -> {
                    Button(onClick = { 
                        onArrived()
                        status = "arrived"
                    }, modifier = Modifier.fillMaxWidth()) { Text("Arrived") }
                }
                "arrived" -> {
                    Button(onClick = { 
                        onStartTrip()
                        status = "in_progress"
                    }, modifier = Modifier.fillMaxWidth()) { Text("Start Trip") }
                }
                "in_progress" -> {
                    Button(onClick = { 
                        onComplete()
                        status = "completed"
                    }, modifier = Modifier.fillMaxWidth()) { Text("Complete Trip") }
                }
                "completed" -> {
                    OutlinedButton(onClick = onCashPayment, modifier = Modifier.fillMaxWidth()) {
                        Icon(Icons.Default.Payment, contentDescription = null)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Cash Payment")
                    }
                }
            }
        }
    }
}