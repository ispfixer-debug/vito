package com.vito.app.presentation.ui.screens.driver

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EarningsScreen(viewModel: DriverViewModel) {
    val uiState by viewModel.uiState.collectAsState()
    
    Scaffold(topBar = { TopAppBar(title = { Text("Earnings") }) }) { padding ->
        Column(
            modifier = Modifier.fillMaxSize().padding(padding).padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            val e = uiState.earnings
            Card(Modifier.fillMaxWidth()) {
                Column(Modifier.padding(16.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                    Text("Today's Earnings", style = MaterialTheme.typography.labelLarge)
                    Text("$${String.format("%.2f", e.today)}", style = MaterialTheme.typography.headlineLarge)
                }
            }
            Spacer(Modifier.height(16.dp))
            Card(Modifier.fillMaxWidth()) {
                Column(Modifier.padding(16.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                    Text("This Week", style = MaterialTheme.typography.labelLarge)
                    Text("$${String.format("%.2f", e.week)}", style = MaterialTheme.typography.headlineLarge)
                }
            }
            Spacer(Modifier.height(16.dp))
            Card(Modifier.fillMaxWidth()) {
                Column(Modifier.padding(16.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                    Text("This Month", style = MaterialTheme.typography.labelLarge)
                    Text("$${String.format("%.2f", e.month)}", style = MaterialTheme.typography.headlineLarge)
                }
            }
            Spacer(Modifier.weight(1f))
            Button(
                onClick = { /* Cash out */ },
                modifier = Modifier.fillMaxWidth().height(56.dp)
            ) { Text("Cash Out") }
        }
    }
}
