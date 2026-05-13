package com.vito.app.presentation.ui.screens.send

import androidx.compose.foundation.background
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
fun SendTrackingScreen(onComplete: () -> Unit = {}) {
    var status by remember { mutableStateOf("driver_pickup") }

    val statusSteps = listOf(
        "placed" to stringResource(R.string.placed),
        "preparing" to "Preparing",
        "driver_pickup" to "Driver Pickup",
        "delivering" to stringResource(R.string.delivering),
        "delivered" to stringResource(R.string.completed)
    )

    Scaffold(
        topBar = { TopAppBar(title = { Text(stringResource(R.string.tracking)) }) }
    ) { padding ->
        Column(
            modifier = Modifier.fillMaxSize().padding(padding).padding(16.dp)
        ) {
            statusSteps.forEachIndexed { index, (stepKey, stepLabel) ->
                val isActive = status == stepKey
                val isCompleted = statusSteps.indexOfFirst { it.first == status } > index

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = if (isCompleted) Icons.Filled.CheckCircle
                                   else if (isActive) Icons.Filled.RadioButtonChecked
                                   else Icons.Filled.RadioButtonUnchecked,
                        contentDescription = null,
                        tint = if (isActive || isCompleted) MaterialTheme.colorScheme.primary
                              else MaterialTheme.colorScheme.outline
                    )
                    Spacer(Modifier.width(12.dp))
                    Text(
                        text = stepLabel,
                        color = if (isActive || isCompleted) MaterialTheme.colorScheme.onSurface
                               else MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f)
                    )
                }

                if (index < statusSteps.lastIndex) {
                    Box(
                        modifier = Modifier
                            .padding(start = 12.dp)
                            .width(2.dp)
                            .height(24.dp)
                            .background(
                                if (isCompleted) MaterialTheme.colorScheme.primary
                                else MaterialTheme.colorScheme.outline.copy(alpha = 0.3f)
                            )
                    )
                }
            }

            HorizontalDivider(Modifier.padding(vertical = 16.dp))

            if (status == "delivered") {
                Text(
                    text = stringResource(R.string.proof_of_delivery),
                    style = MaterialTheme.typography.titleSmall
                )
                Spacer(Modifier.height(8.dp))
                Row {
                    Card(Modifier.size(80.dp)) {
                        Box(
                            modifier = Modifier.fillMaxSize(),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Filled.Photo,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.outline
                            )
                        }
                    }
                    Spacer(Modifier.width(8.dp))
                    Card(Modifier.size(80.dp)) {
                        Box(
                            modifier = Modifier.fillMaxSize(),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Filled.Draw,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.outline
                            )
                        }
                    }
                }
            }

            Spacer(Modifier.weight(1f))

            if (status != "delivered") {
                Row(modifier = Modifier.fillMaxWidth()) {
                    OutlinedButton(
                        onClick = { },
                        modifier = Modifier.weight(1f)
                    ) {
                        Text(stringResource(R.string.cancel))
                    }
                    Spacer(Modifier.width(8.dp))
                    Button(
                        onClick = {
                            val currentIndex = statusSteps.indexOfFirst { it.first == status }
                            if (currentIndex < statusSteps.lastIndex) {
                                status = statusSteps[currentIndex + 1].first
                            }
                        },
                        modifier = Modifier.weight(1f)
                    ) {
                        Text(stringResource(R.string.next))
                    }
                }
            } else {
                Button(
                    onClick = onComplete,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(stringResource(R.string.done))
                }
            }
        }
    }
}
