package com.vito.driver.qr

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.vito.core.ui.components.GenerateQrCode

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DriverQrGenerateScreen(
    paymentToken: String,
    onGenerateNew: () -> Unit,
    modifier: Modifier = Modifier
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Payment QR") }
            )
        }
    ) { padding ->
        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                "Show this QR to receive cash payment",
                style = MaterialTheme.typography.titleMedium
            )
            Spacer(modifier = Modifier.height(24.dp))
            GenerateQrCode(content = paymentToken)
            Spacer(modifier = Modifier.height(24.dp))
            Button(onClick = onGenerateNew) {
                Text("Generate New Token")
            }
        }
    }
}