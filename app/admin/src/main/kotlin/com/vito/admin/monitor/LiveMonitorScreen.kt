package com.vito.admin.monitor

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

data class LiveDriver(
    val id: String,
    val name: String,
    val lat: Double,
    val lng: Double,
    val isOnline: Boolean
)

data class LiveRide(
    val id: String,
    val status: String,
    val driverId: String?,
    val pickup: String,
    val dropoff: String
)

@Composable
fun LiveMonitorScreen(
    drivers: List<LiveDriver>,
    rides: List<LiveRide>,
    onRefresh: () -> Unit,
    modifier: Modifier = Modifier
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Live Monitor") },
                actions = {
                    IconButton(onClick = onRefresh) {
                        Icon(Icons.Default.Refresh, contentDescription = "Refresh")
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            // Stats
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                val onlineDrivers = drivers.count { it.isOnline }
                StatChip(label = "Online Drivers", value = onlineDrivers.toString())
                StatChip(label = "Active Rides", value = rides.size.toString())
            }

            // Map placeholder
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                color = MaterialTheme.colorScheme.surfaceVariant
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Text("Live Map (${drivers.size} drivers)")
                }
            }

            // Active rides list
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                Text("Active Rides", style = MaterialTheme.typography.titleMedium)
                Spacer(modifier = Modifier.height(8.dp))
                rides.take(5).forEach { ride ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 4.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            when (ride.status) {
                                "PENDING" -> Icons.Default.HourglassEmpty
                                "ACCEPTED" -> Icons.Default.DirectionsCar
                                "IN_PROGRESS" -> Icons.Default.PlayArrow
                                else -> Icons.Default.Check
                            },
                            contentDescription = null,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            "${ride.pickup} → ${ride.dropoff}",
                            style = MaterialTheme.typography.bodySmall
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun StatChip(label: String, value: String, modifier: Modifier = Modifier) {
    Card(modifier = modifier) {
        Column(
            modifier = Modifier.padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(value, style = MaterialTheme.typography.headlineMedium)
            Text(label, style = MaterialTheme.typography.labelSmall)
        }
    }
}