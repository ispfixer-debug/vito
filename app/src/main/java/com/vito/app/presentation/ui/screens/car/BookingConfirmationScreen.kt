package com.vito.app.presentation.ui.screens.car

import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import com.vito.app.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BookingConfirmationScreen(
    rideId: String,
    viewModel: VitoCarViewModel = hiltViewModel(),
    onMessageDriver: () -> Unit = {},
    onCancel: () -> Unit = {},
    onShareTrip: () -> Unit = {},
    onSos: () -> Unit = {}
) {
    val uiState by viewModel.uiState.collectAsState()
    
    // ETA countdown
    var etaSeconds by remember { mutableStateOf(180) } // 3 minutes
    LaunchedEffect(Unit) {
        while (etaSeconds > 0) {
            kotlinx.coroutines.delay(1000)
            etaSeconds--
        }
    }
    
    // Pulsing animation for ETA
    val infiniteTransition = rememberInfiniteTransition(label = "eta")
    val alpha by infiniteTransition.animateFloat(
        initialValue = 1f,
        targetValue = 0.5f,
        animationSpec = infiniteRepeatable(
            animation = tween(500),
            repeatMode = RepeatMode.Reverse
        ),
        label = "alpha"
    )
    
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(stringResource(R.string.ride)) },
                navigationIcon = {
                    IconButton(onClick = onCancel) {
                        Icon(Icons.Filled.ArrowBack, contentDescription = stringResource(R.string.cancel))
                    }
                }
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
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Map placeholder for driver location
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
                    // Driver marker animation
                    Icon(
                        Icons.Filled.LocalTaxi,
                        contentDescription = null,
                        modifier = Modifier.size(48.dp),
                        tint = MaterialTheme.colorScheme.primary.copy(alpha = alpha)
                    )
                }
            }
            
            Spacer(modifier = Modifier.height(16.dp))
            
            // Driver card
            Card(
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier.padding(16.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        // Driver photo
                        AsyncImage(
                            model = "https://randomuser.me/api/portraits/men/1.jpg",
                            contentDescription = stringResource(R.string.profile),
                            modifier = Modifier
                                .size(64.dp)
                                .clip(CircleShape),
                            contentScale = ContentScale.Crop
                        )
                        
                        Spacer(modifier = Modifier.width(16.dp))
                        
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = "John Smith", // Mock driver name
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                text = "Toyota Camry (ABC-1234)",
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(
                                    Icons.Filled.Star,
                                    contentDescription = null,
                                    modifier = Modifier.size(16.dp),
                                    tint = Color(0xFFFFC107)
                                )
                                Spacer(modifier = Modifier.width(4.dp))
                                Text("4.8", style = MaterialTheme.typography.bodyMedium)
                            }
                        }
                        
                        // ETA badge
                        Surface(
                            shape = CircleShape,
                            color = MaterialTheme.colorScheme.primaryContainer
                        ) {
                            Text(
                                text = "${etaSeconds / 60}:${String.format("%02d", etaSeconds % 60)}",
                                modifier = Modifier.padding(12.dp),
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }
            }
            
            Spacer(modifier = Modifier.height(16.dp))
            
            // Action buttons row
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                // Message Driver
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    IconButton(onClick = onMessageDriver) {
                        Icon(Icons.Filled.Chat, contentDescription = stringResource(R.string.message_driver))
                    }
                    Text(
                        stringResource(R.string.message_driver),
                        style = MaterialTheme.typography.labelSmall
                    )
                }
                
                // Cancel
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    OutlinedIconButton(
                        onClick = onCancel,
                        colors = IconButtonDefaults.outlinedIconButtonColors(
                            contentColor = MaterialTheme.colorScheme.error
                        )
                    ) {
                        Icon(Icons.Filled.Close, contentDescription = stringResource(R.string.cancel))
                    }
                    Text(
                        stringResource(R.string.cancel),
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.error
                    )
                }
                
                // Share Trip
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    IconButton(onClick = onShareTrip) {
                        Icon(Icons.Filled.Share, contentDescription = stringResource(R.string.share_trip))
                    }
                    Text(
                        stringResource(R.string.share_trip),
                        style = MaterialTheme.typography.labelSmall
                    )
                }
            }
        }
    }
}