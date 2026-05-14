package com.vito.client.vitosend

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.vito.core.domain.model.StatusTimeline
import com.vito.core.domain.model.RideStatus
import com.vito.core.domain.model.getDefaultTimelineEvents

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SendTrackingScreen(
    packageId: String,
    status: RideStatus,
    onContactDriver: () -> Unit,
    modifier: Modifier = Modifier
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Track Delivery") }
            )
        }
    ) { padding ->
        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp)
        ) {
            // Map placeholder
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                color = MaterialTheme.colorScheme.surfaceVariant
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Text("Live Map")
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Status timeline
            StatusTimeline(
                events = getDefaultTimelineEvents(),
                status = status
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Contact button
            OutlinedButton(
                onClick = onContactDriver,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Contact Driver")
            }
        }
    }
}