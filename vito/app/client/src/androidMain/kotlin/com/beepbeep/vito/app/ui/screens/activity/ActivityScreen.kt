package com.beepbeep.vito.app.ui.screens.activity

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun ActivityScreen() {
    var selectedTab by remember { mutableStateOf(0) }
    
    Column(Modifier.fillMaxSize()) {
        TabRow(selectedTabIndex = selectedTab) {
            Tab(selected = selectedTab == 0, onClick = { selectedTab = 0 }, text = { Text("All") })
            Tab(selected = selectedTab == 1, onClick = { selectedTab = 1 }, text = { Text("Rides") })
            Tab(selected = selectedTab == 2, onClick = { selectedTab = 2 }, text = { Text("Sends") })
            Tab(selected = selectedTab == 3, onClick = { selectedTab = 3 }, text = { Text("Mart") })
        }
        
        Column(Modifier.padding(16.dp)) {
            when (selectedTab) {
                0, 1 -> Text("Ride: Cairo → Giza - $5.00 - Completed")
                0, 2 -> Text("Package: Maadi → Zamalek - Delivered")
                0, 3 -> Text("Order: Coca Cola x2 - Delivered")
            }
        }
    }
}
