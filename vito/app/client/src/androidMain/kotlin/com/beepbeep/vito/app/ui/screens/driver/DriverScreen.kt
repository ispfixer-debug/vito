package com.beepbeep.vito.app.ui.screens.driver

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun DriverScreen() {
    var online by remember { mutableStateOf(false) }
    
    Column(Modifier.fillMaxSize().padding(16.dp)) {
        Text("Driver Dashboard", style = MaterialTheme.typography.headlineLarge)
        Spacer(16.dp))
        
        Row { Text("Online"); Switch(checked = online, onCheckedChange = { online = it }) }
        
        Spacer(24.dp))
        
        Text("My Earnings", style = MaterialTheme.typography.titleLarge)
        Card(Modifier.fillMaxWidth()) {
            Column(Modifier.padding(16.dp)) {
                Text("Today: $45.00")
                Text("Week: $280.00")
                Text("Month: $1,200.00")
            }
        }
        
        Spacer(24.dp))
        
        Button(onClick = { }, modifier = Modifier.fillMaxWidth()) { Text("Cash Out") }
    }
}
