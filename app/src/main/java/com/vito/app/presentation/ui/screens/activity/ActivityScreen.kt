package com.vito.app.presentation.ui.screens.activity

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LocalTaxi
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.vito.app.data.mock.MockData

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ActivityScreen() {
    Scaffold(topBar = { TopAppBar(title = { Text("Activity") }) }) { padding ->
        Column(Modifier.fillMaxSize().padding(padding)) {
            TabRow(selectedTabIndex = 0) {
                listOf("All", "Rides", "Sends").forEach { Tab(selected = it == "All", onClick = {}, text = { Text(it) }) }
            }
            LazyColumn(contentPadding = PaddingValues(16.dp)) {
                items(MockData.generateMockTrips()) { trip ->
                    Card(Modifier.fillMaxWidth().padding(8.dp)) {
                        Row(Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Filled.LocalTaxi, null, Modifier.size(40.dp))
                            Spacer(Modifier.width(16.dp))
                            Column { 
                                Text(trip.pickup)
                                Text(trip.dropoff) 
                                Text("$${trip.fare}") 
                            }
                        }
                    }
                }
            }
        }
    }
}
