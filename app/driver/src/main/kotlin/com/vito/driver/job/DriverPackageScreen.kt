package com.vito.driver.job

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun DriverPackageScreen(
    recipientName: String,
    recipientPhone: String,
    packageDescription: String,
    onPickupPhoto: () -> Unit,
    onDeliveryPhoto: () -> Unit,
    onSignatureCapture: () -> Unit,
    onComplete: () -> Unit,
    modifier: Modifier = Modifier
) {
    var step by remember { mutableIntStateOf(0) }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Card(modifier = Modifier.fillMaxWidth()) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text("Package Details", style = MaterialTheme.typography.titleMedium)
                Text("To: $recipientName")
                Text("Phone: $recipientPhone")
                Text("Desc: $packageDescription")
            }
        }

        when (step) {
            0 -> {
                OutlinedButton(onClick = { step = 1 }, modifier = Modifier.fillMaxWidth()) {
                    Icon(Icons.Default.CameraAlt, contentDescription = null)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Capture Pickup Photo")
                }
            }
            1 -> {
                OutlinedButton(onClick = { step = 2 }, modifier = Modifier.fillMaxWidth()) {
                    Icon(Icons.Default.CameraAlt, contentDescription = null)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Capture Delivery Photo")
                }
            }
            2 -> {
                OutlinedButton(onClick = { step = 3 }, modifier = Modifier.fillMaxWidth()) {
                    Icon(Icons.Default.Draw, contentDescription = null)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Capture Signature")
                }
            }
            3 -> {
                Button(onClick = onComplete, modifier = Modifier.fillMaxWidth()) {
                    Text("Complete Delivery")
                }
            }
        }
    }
}