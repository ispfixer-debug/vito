package com.vito.app.presentation.ui.screens.admin

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.vito.app.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AuditLogScreen(onBack: () -> Unit = {}) {
    val logs = remember {
        listOf(
            LogItem("User login", "user1", "2024-01-15 10:30"),
            LogItem("Ride completed", "ride_123", "2024-01-15 10:25"),
            LogItem("Payment received", "$15.00", "2024-01-15 10:20"),
            LogItem("Driver online", "driver1", "2024-01-15 10:15"),
            LogItem("User signup", "user2", "2024-01-15 10:00")
        )
    }
    
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(stringResource(R.string.audit)) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Filled.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { padding ->
        LazyColumn(
            modifier = Modifier.fillMaxSize().padding(padding),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(logs) { log ->
                Card(modifier = Modifier.fillMaxWidth()) {
                    Row(modifier = Modifier.padding(16.dp)) {
                        Text(log.action, Modifier.weight(1f))
                        Text(log.details)
                        Text(log.timestamp, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.outline)
                    }
                }
            }
        }
    }
}

data class LogItem(val action: String, val details: String, val timestamp: String)