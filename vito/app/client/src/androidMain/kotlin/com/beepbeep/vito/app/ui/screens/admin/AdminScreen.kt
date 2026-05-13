package com.beepbeep.vito.app.ui.screens.admin

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun AdminScreen() {
    var selectedTab by remember { mutableStateOf(0) }
    val tabs = listOf("Dashboard", "QR Codes", "Users", "Documents", "Finance")
    
    Column(Modifier.fillMaxSize()) {
        TabRow(selectedTabIndex = selectedTab) {
            tabs.forEachIndexed { index, title ->
                Tab(selected = selectedTab == index, onClick = { selectedTab = index }, text = { Text(title) })
            }
        }
        
        Column(Modifier.padding(16.dp)) {
            when (selectedTab) {
                0 -> {
                    Text("Active Rides: 15")
                    Text("Active Deliveries: 8")
                    Text("Online Drivers: 23")
                    Text("Pending Orders: 12")
                }
                1 -> Text("QR Management Screen")
                2 -> Text("User Management Screen")
                3 -> Text("Document Verification Screen")
                4 -> Text("Finance Screen")
            }
        }
    }
}
