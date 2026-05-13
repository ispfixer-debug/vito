package com.vito.app.presentation.ui.screens.qr

import android.Manifest
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun QrScannerScreen(
    onBack: () -> Unit = {},
    onQrScanned: ((String) -> Unit)? = null
) {
    var scannedResult by remember { mutableStateOf<String?>(null) }
    
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Scan QR") },
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
                .padding(padding),
            horizontalAlignment = androidx.compose.ui.Alignment.CenterHorizontally
        ) {
            Spacer(Modifier.weight(1f))
            Text("QR Scanner")
            Spacer(Modifier.height(16.dp))
            Card {
                Box(Modifier.size(250.dp).padding(32.dp)) {
                    Text("Scan area", Modifier.align(androidx.compose.ui.Alignment.Center))
                }
            }
            Spacer(Modifier.height(16.dp))
            scannedResult?.let { result ->
                Text("Result: $result")
            }
            Spacer(Modifier.weight(1f))
            Text("Point camera at QR code", modifier = Modifier.padding(16.dp))
        }
    }
}
