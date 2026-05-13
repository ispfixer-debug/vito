package com.vito.app.presentation.ui.screens.car

import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.vito.app.R
import com.vito.app.domain.model.RideStatusType

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TripTrackingScreen(
    viewModel: VitoCarViewModel = hiltViewModel(),
    onMessageDriver: () -> Unit = {},
    onSos: () -> Unit = {}
) {
    val uiState by viewModel.uiState.collectAsState()
    
    val currentStatus = when (val state = uiState) {
        is RideUiState.InProgress -> state.status
        else -> RideStatusType.DRIVER_ARRIVING
    }
    
    val statusSteps = listOf(
        StatusStep(stringResource(R.string.driver_arriving), RideStatusType.DRIVER_ARRIVING),
        StatusStep(stringResource(R.string.pickup_passenger), RideStatusType.IN_PROGRESS),
        StatusStep(stringResource(R.string.dropoff), RideStatusType.COMPLETED)
    )
    
    // Find current step index
    val currentStepIndex = statusSteps.indexOfFirst { it.status == currentStatus }
    
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(stringResource(R.string.ride)) }
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = onSos,
                containerColor = MaterialTheme.colorScheme.error
            ) {
                Icon(Icons.Filled.Warning, contentDescription = stringResource(R.string.sos), tint = Color.White)
            }
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
                    // Animated dots showing route
                    Row {
                        repeat(3) { index ->
                            val infiniteTransition = rememberInfiniteTransition(label = "dots_$index")
                            val alpha by infiniteTransition.animateFloat(
                                initialValue = 0.3f,
                                targetValue = 1f,
                                animationSpec = infiniteRepeatable(
                                    animation = tween(1000, delayMillis = index * 300),
                                    repeatMode = RepeatMode.Reverse
                                ),
                                label = "dot_alpha"
                            )
                            Icon(
                                Icons.Filled.Place,
                                contentDescription = null,
                                modifier = Modifier
                                    .size(32.dp)
                                    .alpha(alpha),
                                tint = MaterialTheme.colorScheme.primary
                            )
                            if (index < 2) Spacer(modifier = Modifier.width(8.dp))
                        }
                    }
                }
            }
            
            // Bottom status card
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = MaterialTheme.shapes.large
            ) {
                Column(
                    modifier = Modifier.padding(16.dp)
                ) {
                    // Status timeline
                    statusSteps.forEachIndexed { index, step ->
                        val isActive = index <= currentStepIndex
                        val isCompleted = index < currentStepIndex
                        
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            // Step indicator
                            Box(
                                modifier = Modifier.size(24.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                if (isCompleted) {
                                    Icon(
                                        Icons.Filled.CheckCircle,
                                        contentDescription = null,
                                        tint = MaterialTheme.colorScheme.primary,
                                        modifier = Modifier.size(24.dp)
                                    )
                                } else if (isActive) {
                                    // Pulsing active indicator
                                    val infiniteTransition = rememberInfiniteTransition(label = "pulse")
                                    val alpha by infiniteTransition.animateFloat(
                                        initialValue = 0.5f,
                                        targetValue = 1f,
                                        animationSpec = infiniteRepeatable(
                                            animation = tween(800),
                                            repeatMode = RepeatMode.Reverse
                                        ),
                                        label = "pulse_alpha"
                                    )
                                    Icon(
                                        Icons.Filled.RadioButtonChecked,
                                        contentDescription = null,
                                        tint = MaterialTheme.colorScheme.primary,
                                        modifier = Modifier
                                            .size(24.dp)
                                            .alpha(alpha)
                                    )
                                } else {
                                    Icon(
                                        Icons.Filled.RadioButtonUnchecked,
                                        contentDescription = null,
                                        tint = MaterialTheme.colorScheme.outline,
                                        modifier = Modifier.size(24.dp)
                                    )
                                }
                            }
                            
                            Spacer(modifier = Modifier.width(16.dp))
                            
                            Text(
                                text = step.label,
                                style = MaterialTheme.typography.bodyLarge,
                                color = if (isActive) {
                                    MaterialTheme.colorScheme.onSurface
                                } else {
                                    MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f)
                                }
                            )
                        }
                        
                        // Connector line
                        if (index < statusSteps.lastIndex) {
                            Box(
                                modifier = Modifier
                                    .padding(start = 12.dp)
                                    .width(2.dp)
                                    .height(24.dp)
                                    .background(
                                        if (isCompleted) {
                                            MaterialTheme.colorScheme.primary
                                        } else {
                                            MaterialTheme.colorScheme.outline.copy(alpha = 0.3f)
                                        }
                                    )
                            )
                        }
                    }
                    
                    HorizontalDivider(modifier = Modifier.padding(vertical = 16.dp))
                    
                    // Message Driver button
                    Button(
                        onClick = onMessageDriver,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Icon(Icons.Filled.Chat, contentDescription = null)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(stringResource(R.string.message_driver))
                    }
                }
            }
        }
    }
}

data class StatusStep(
    val label: String,
    val status: RideStatusType
)