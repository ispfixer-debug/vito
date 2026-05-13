package com.vito.app.presentation.ui.screens.send

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
fun SendTrackingScreen(
    packageId: String = "",
    onMessageDriver: () -> Unit = {},
    onBack: () -> Unit = {}
) {
    var status by remember { mutableStateOf("driver_pickup") }
    
    val statusSteps = listOf(
        "placed" to stringResource(R.string.placed),
        "preparing" to stringResource(R.string.completed) + " (Prep)",
        "driver_pickup" to "Driver Pickup",
        "delivering" to stringResource(R.string.in_progress),
        "delivered" to stringResource(R.string.completed)
    )
    
    val currentStep = statusSteps.indexOfFirst { it.first == status }
    
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(stringResource(R.string.send)) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Filled.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier.fillMaxSize().padding(padding).padding(16.dp)
        ) {
            // Map placeholder
            Card(
                modifier = Modifier.fillMaxWidth().weight(1f),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
            ) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Icon(Icons.Filled.LocalShipping, null, Modifier.size(64.dp), MaterialTheme.colorScheme.outline)
                }
            }
            
            // Status timeline
            Card(modifier = Modifier.fillMaxWidth()) {
                Column(Modifier.padding(16.dp)) {
                    statusSteps.forEachIndexed { index, step ->
                        val isActive = index <= currentStep
                        val isCompleted = index < currentStep
                        
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                if (isCompleted) Icons.Filled.CheckCircle 
                                else if (isActive) Icons.Filled.LocalShipping
                                else Icons.Filled.RadioButtonUnchecked,
                                null,
                                modifier = Modifier.size(24.dp),
                                tint = when {
                                    isCompleted -> MaterialTheme.colorScheme.primary
                                    isActive -> MaterialTheme.colorScheme.primary
                                    else -> MaterialTheme.colorScheme.outline
                                }
                            )
                            Spacer(Modifier.width(16.dp))
                            Text(
                                step.second,
                                color = if (isActive) MaterialTheme.colorScheme.onSurface 
                                       else MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f)
                            )
                        }
                        
                        if (index < statusSteps.lastIndex) {
                            Box(Modifier.padding(start = 12.dp).width(2.dp).height(24.dp).background(
                                if (isCompleted) MaterialTheme.colorScheme.primary 
                                else MaterialTheme.colorScheme.outline.copy(alpha = 0.3f)
                            ))
                        }
                    }
                    
                    HorizontalDivider(Modifier.padding(vertical = 16.dp))
                    
                    // Proof of delivery (when delivered)
                    if (status == "delivered") {
                        Text("Proof of Delivery", MaterialTheme.typography.titleSmall)
                        Spacer(Modifier.height(8.dp))
                        Row {
                            // Photo
                            Card(Modifier.size(80.dp)) {
                                Box(Modifier.fillMaxSize(), Alignment.Center) {
                                    Icon(Icons.Filled.Photo, null, MaterialTheme.colorScheme.outline)
                                }
                            }
                            Spacer(Modifier.width(8.dp))
                            // Signature
                            Card(Modifier.size(80.dp)) {
                                Box(Modifier.fillMaxSize(), Alignment.Center) {
                                    Icon(Icons.Filled.Draw, null, MaterialTheme.colorScheme.outline)
                                }
                            }
                        }
                        
                        HorizontalDivider(Modifier.padding(vertical = 16.dp))
                    }
                    
                    // Message Driver button
                    OutlinedButton(
                        onClick = onMessageDriver,
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