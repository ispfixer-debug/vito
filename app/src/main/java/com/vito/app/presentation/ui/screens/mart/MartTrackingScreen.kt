package com.vito.app.presentation.ui.screens.mart

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.RadioButtonUnchecked
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MartTrackingScreen(
    onComplete: () -> Unit = {}
) {
    var status by remember { mutableStateOf("preparing") }
    val steps = listOf("Placed", "Preparing", "Driver Pickup", "Delivering", "Delivered")
    
    Scaffold(topBar = { TopAppBar(title = { Text("Order Tracking") }) }) { padding ->
        Column(modifier = Modifier.fillMaxSize().padding(padding).padding(16.dp)) {
            steps.forEachIndexed { index, step ->
                val isCompleted = steps.indexOf(status) > index
                val isCurrent = status == step
                
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        if (isCompleted) Icons.Filled.CheckCircle else Icons.Filled.RadioButtonUnchecked,
                        contentDescription = null,
                        tint = when {
                            isCompleted -> MaterialTheme.colorScheme.primary
                            isCurrent -> MaterialTheme.colorScheme.primary
                            else -> MaterialTheme.colorScheme.outline
                        }
                    )
                    Spacer(Modifier.width(12.dp))
                    Text(step, color = when {
                        isCompleted || isCurrent -> MaterialTheme.colorScheme.onSurface
                        else -> MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f)
                    })
                }
            }
            Spacer(Modifier.weight(1f))
            Button(onClick = {
                val idx = steps.indexOf(status)
                if (idx < steps.lastIndex) status = steps[idx + 1]
            }, modifier = Modifier.fillMaxWidth()) {
                Text("Next Step")
            }
        }
    }
}
