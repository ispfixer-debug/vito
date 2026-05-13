package com.vito.app.presentation.ui.screens.car

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.vito.app.R
import com.vito.app.data.mock.MockLocation
import com.vito.app.domain.model.Stop

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MultiStopEditor(
    stops: List<Stop>,
    onAddStop: (Stop) -> Unit,
    onRemoveStop: (Int) -> Unit,
    onReorder: (List<Stop>) -> Unit,
    onDismiss: () -> Unit
) {
    val availableLocations = remember { 
        listOf(
            MockLocation("Downtown", 40.7128, -74.0060),
            MockLocation("Airport", 40.6413, -73.7781),
            MockLocation("Times Square", 40.7580, -73.9855),
            MockLocation("Central Park", 40.7829, -73.9654),
            MockLocation("Brooklyn", 40.6782, -73.9442)
        )
    }
    
    var showAddDialog by remember { mutableStateOf(false) }
    
    ModalBottomSheet(onDismissRequest = onDismiss) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    stringResource(R.string.add_stops),
                    style = MaterialTheme.typography.titleLarge
                )
                if (stops.size < 3) {
                    IconButton(onClick = { showAddDialog = true }) {
                        Icon(Icons.Filled.Add, contentDescription = "Add stop")
                    }
                }
            }
            
            Spacer(Modifier.height(16.dp))
            
            if (stops.isEmpty()) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(100.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        "No stops added",
                        color = MaterialTheme.colorScheme.outline
                    )
                }
            } else {
                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(stops.size) { index ->
                        Card(
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Row(
                                modifier = Modifier
                                    .padding(16.dp)
                                    .fillMaxWidth(),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                // Drag handle
                                Icon(
                                    Icons.Filled.DragHandle,
                                    contentDescription = null,
                                    tint = MaterialTheme.colorScheme.outline
                                )
                                Spacer(Modifier.width(12.dp))
                                // Stop number
                                Surface(
                                    shape = MaterialTheme.shapes.small,
                                    color = MaterialTheme.colorScheme.primaryContainer
                                ) {
                                    Text(
                                        "${index + 1}",
                                        modifier = Modifier.padding(8.dp)
                                    )
                                }
                                Spacer(Modifier.width(12.dp))
                                // Address
                                Text(
                                    stops[index].location,
                                    modifier = Modifier.weight(1f)
                                )
                                // Delete button
                                IconButton(onClick = { onRemoveStop(index) }) {
                                    Icon(
                                        Icons.Filled.Close,
                                        contentDescription = "Remove",
                                        tint = MaterialTheme.colorScheme.error
                                    )
                                }
                            }
                        }
                    }
                }
            }
            
            Spacer(Modifier.height(32.dp))
        }
    }
    
    // Add stop dialog
    if (showAddDialog) {
        AlertDialog(
            onDismissRequest = { showAddDialog = false },
            title = { Text(stringResource(R.string.add_stops)) },
            text = {
                LazyColumn {
                    items(availableLocations) { location ->
                        ListItem(
                            headlineContent = { Text(location.address) },
                            leadingContent = { Icon(Icons.Filled.Place, null) },
                            modifier = Modifier.clickable {
                                onAddStop(Stop(location.address, location.lat, location.lng))
                                showAddDialog = false
                            }
                        )
                    }
                }
            },
            confirmButton = {
                TextButton(onClick = { showAddDialog = false }) {
                    Text(stringResource(R.string.cancel))
                }
            }
        )
    }
}