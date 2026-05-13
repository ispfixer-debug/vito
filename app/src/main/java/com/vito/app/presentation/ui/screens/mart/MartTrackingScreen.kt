package com.vito.app.presentation.ui.screens.mart

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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MartTrackingScreen(
    orderId: String = "",
    onMessage: () -> Unit = {},
    onBack: () -> Unit = {}
) {
    var status by remember { mutableStateOf("preparing") }
    
    val statusSteps = listOf(
        "placed" to stringResource(R.string.placed),
        "preparing" to "Preparing",
        "driver_pickup" to "Driver Pickup",
        "delivering" to stringResource(R.string.in_progress),
        "delivered" to stringResource(R.string.completed)
    )
    
    val currentStep = statusSteps.indexOfFirst { it.first == status }
    
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Order Tracking") },
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
                            Icons.Filled.LocalShipping,
                            null,
                            modifier = Modifier.size(64.dp),
                            tint = MaterialTheme.colorScheme.outline
                        )
                        Text(
                            "Order $orderId",
                            style = MaterialTheme.typography.titleMedium
                        )
                    }
                }
            }
            
            // Status Timeline
            Card(
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(Modifier.padding(16.dp)) {
                    statusSteps.forEachIndexed { index, step ->
                        val isActive = index <= currentStep
                        val isCompleted = index < currentStep
                        
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Icon(
                                when {
                                    isCompleted -> Icons.Filled.CheckCircle
                                    isActive -> Icons.Filled.LocalShipping
                                    else -> Icons.Filled.RadioButtonUnchecked
                                },
                                null,
                                tint = when {
                                    isCompleted -> MaterialTheme.colorScheme.primary
                                    isActive -> MaterialTheme.colorScheme.primary
                                    else -> MaterialTheme.colorScheme.outline
                                }
                            )
                            Spacer(Modifier.width(12.dp))
                            Text(
                                step.second,
                                color = if (isActive) MaterialTheme.colorScheme.onSurface
                                        else MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f)
                            )
                        }
                        
                        if (index < statusSteps.lastIndex) {
                            Box(
                                modifier = Modifier
                                    .padding(start = 12.dp)
                                    .width(2.dp)
                                    .height(16.dp)
                                    .background(
                                        if (isCompleted) MaterialTheme.colorScheme.primary
                                        else MaterialTheme.colorScheme.outline.copy(alpha = 0.3f)
                                    )
                            )
                        }
                    }
                    
                    HorizontalDivider(Modifier.padding(vertical = 16.dp))
                    
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
    }
}