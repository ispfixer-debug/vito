package com.vito.app.presentation.ui.screens.driver

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DirectionsCar
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.vito.app.data.mock.MockData

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun JobsScreen() {
    val jobs = MockData.generateMockTrips()
    
    Scaffold(topBar = { TopAppBar(title = { Text("Jobs") }) }) { padding ->
        Column(Modifier.fillMaxSize().padding(padding)) {
            LazyColumn(contentPadding = PaddingValues(16.dp)) {
                items(jobs.take(5)) { job ->
                    Card(Modifier.fillMaxWidth().padding(8.dp)) {
                        Column(Modifier.padding(16.dp)) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(Icons.Filled.DirectionsCar, null)
                                Spacer(Modifier.width(8.dp))
                                Text("${job.fare}")
                            }
                            Spacer(Modifier.height(8.dp))
                            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                                Button(onClick = { }) { Text("Accept") }
                                OutlinedButton(onClick = { }) { Text("Decline") }
                            }
                        }
                    }
                }
            }
        }
    }
}
