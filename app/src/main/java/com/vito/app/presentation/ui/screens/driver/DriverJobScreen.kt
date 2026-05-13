package com.vito.app.presentation.ui.screens.driver

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.vito.app.R
import com.vito.app.domain.model.RideStatusType

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DriverJobScreen(
    jobId: String = "",
    onArrived: () -> Unit = {},
    onStartTrip: () -> Unit = {},
    onCompleteTrip: () -> Unit = {},
    onMessage: () -> Unit = {},
    onCancel: () -> Unit = {}
) {
    var status by remember { mutableStateOf("arriving") }
    
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(stringResource(R.string.jobs)) },
                navigationIcon = {
                    IconButton(onClick = onCancel) {
                        Icon(Icons.Filled.Close, contentDescription = stringResource(R.string.cancel))
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
        ) {
            // Map placeholder
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surfaceVariant
                )
            ) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Icon(
                            Icons.Filled.Map,
                            null,
                            modifier = Modifier.size(64.dp),
                            tint = MaterialTheme.colorScheme.outline
                        )
                        Text(
                            when (status) {
                                "arriving" -> stringResource(R.string.driver_arriving)
                                "picked_up" -> stringResource(R.string.pickup_passenger)
                                else -> stringResource(R.string.in_progress)
                            },
                            style = MaterialTheme.typography.bodyLarge
                        )
                    }
                }
            }
            
            Spacer(Modifier.height(16.dp))
            
            // Action buttons based on status
            when (status) {
                "arriving" -> {
                    Button(
                        onClick = {
                            onArrived()
                            status = "picked_up"
                        },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Icon(Icons.Filled.Flag, null)
                        Spacer(Modifier.width(8.dp))
                        Text(stringResource(R.string.arrived))
                    }
                }
                "picked_up" -> {
                    Button(
                        onClick = {
                            onStartTrip()
                            status = "in_progress"
                        },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Icon(Icons.Filled.PlayArrow, null)
                        Spacer(Modifier.width(8.dp))
                        Text(stringResource(R.string.start_trip))
                    }
                }
                "in_progress" -> {
                    Button(
                        onClick = {
                            onCompleteTrip()
                            status = "completed"
                        },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Icon(Icons.Filled.CheckCircle, null)
                        Spacer(Modifier.width(8.dp))
                        Text(stringResource(R.string.complete_trip))
                    }
                }
            }
            
            Spacer(Modifier.height(16.dp))
            
            // Message button
            OutlinedButton(
                onClick = onMessage,
                modifier = Modifier.fillMaxWidth()
            ) {
                Icon(Icons.Filled.Chat, null)
                Spacer(Modifier.width(8.dp))
                Text(stringResource(R.string.message_driver))
            }
        }
    }
}