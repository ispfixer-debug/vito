package com.vito.client.vitoride

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.vito.core.domain.model.RideStatus
import com.vito.core.domain.model.StatusTimeline
import com.vito.core.domain.model.getDefaultTimelineEvents
import com.vito.core.domain.model.DriverCard
import com.vito.core.domain.model.SosButton

@Composable
fun RideTrackingScreen(
    rideId: String,
    status: RideStatus,
    driverName: String?,
    driverPlate: String?,
    driverRating: Double?,
    pickupLocation: String,
    dropoffLocation: String,
    onSosClick: () -> Unit,
    onChatClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Scaffold(
        floatingActionButton = {
            SosButton(onClick = onSosClick)
        }
    ) { padding ->
        Box(
            modifier = modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp)
            ) {
                // Map placeholder
                Surface(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f),
                    color = MaterialTheme.colorScheme.surfaceVariant,
                    shape = MaterialTheme.shapes.medium
                ) {
                    Box(
                        contentAlignment = Alignment.Center
                    ) {
                        Text("Live Map")
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Status timeline
                StatusTimeline(
                    events = getDefaultTimelineEvents(),
                    currentStatus = status
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Driver card (if assigned)
                driverName?.let { name ->
                    DriverCard(
                        name = name,
                        photoUrl = null,
                        rating = driverRating ?: 4.5,
                        vehiclePlate = driverPlate ?: "ABC-123",
                        vehicleType = "Car",
                        onCall = { },
                        onMessage = onChatClick
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Location info
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        Icons.Default.MyLocation,
                        contentDescription = null,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(pickupLocation)
                }

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        Icons.Default.LocationOn,
                        contentDescription = null,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(dropoffLocation)
                }

                Spacer(modifier = Modifier.height(8.dp))

                // Contact driver button
                OutlinedButton(
                    onClick = onChatClick,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Icon(Icons.Default.Chat, contentDescription = null)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Message Driver")
                }
            }
        }
    }
}