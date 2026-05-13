package com.vito.app.presentation.ui.screens.admin

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DashboardScreen() {
    var selectedTab by remember { mutableStateOf("Dashboard") }
    val tabs = listOf("Dashboard", "Users", "Drivers", "Finance", "Audit")
    
    Scaffold(
        topBar = { TopAppBar(title = { Text("Admin") }) },
        bottomBar = {
            NavigationBar {
                tabs.forEach { tab ->
                    NavigationBarItem(
                        selected = selectedTab == tab,
                        onClick = { selectedTab = tab },
                        icon = {
                            Icon(
                                when (tab) {
                                    "Dashboard" -> Icons.Filled.Dashboard
                                    "Users" -> Icons.Filled.People
                                    "Drivers" -> Icons.Filled.DirectionsCar
                                    "Finance" -> Icons.Filled.AttachMoney
                                    "Audit" -> Icons.Filled.History
                                    else -> Icons.Filled.Home
                                },
                                tab
                            )
                        },
                        label = { Text(tab) }
                    )
                }
            }
        }
    ) { padding ->
        Column(
            modifier = Modifier.fillMaxSize().padding(padding).padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            when (selectedTab) {
                "Dashboard" -> {
                    Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                        Card(Modifier.weight(1f)) {
                            Column(Modifier.padding(16.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                                Text("Active Rides", style = MaterialTheme.typography.labelLarge)
                                Text("24", style = MaterialTheme.typography.headlineLarge)
                            }
                        }
                        Card(Modifier.weight(1f)) {
                            Column(Modifier.padding(16.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                                Text("Drivers", style = MaterialTheme.typography.labelLarge)
                                Text("12", style = MaterialTheme.typography.headlineLarge)
                            }
                        }
                    }
                    Spacer(Modifier.height(16.dp))
                    Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                        Card(Modifier.weight(1f)) {
                            Column(Modifier.padding(16.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                                Text("Orders", style = MaterialTheme.typography.labelLarge)
                                Text("56", style = MaterialTheme.typography.headlineLarge)
                            }
                        }
                        Card(Modifier.weight(1f)) {
                            Column(Modifier.padding(16.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                                Text("Users", style = MaterialTheme.typography.labelLarge)
                                Text("128", style = MaterialTheme.typography.headlineLarge)
                            }
                        }
                    }
                }
                "Users", "Drivers", "Finance", "Audit" -> {
                    Text(selectedTab, style = MaterialTheme.typography.headlineMedium)
                }
            }
        }
    }
}
