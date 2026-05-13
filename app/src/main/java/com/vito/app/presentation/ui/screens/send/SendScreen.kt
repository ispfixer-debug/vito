package com.vito.app.presentation.ui.screens.send

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun SendScreen() {
    var size by remember { mutableStateOf("Small") }
    
    Column(Modifier.fillMaxSize().padding(16.dp)) {
        Text("Send Package", style = MaterialTheme.typography.headlineMedium)
        Spacer(Modifier.height(16.dp))
        OutlinedTextField(value = "", onValueChange = {}, label = { Text("Pickup") }, modifier = Modifier.fillMaxWidth())
        Spacer(Modifier.height(8.dp))
        OutlinedTextField(value = "", onValueChange = {}, label = { Text("Delivery") }, modifier = Modifier.fillMaxWidth())
        Spacer(Modifier.height(16.dp))
        Text("Size")
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) { listOf("S", "M", "L").forEach { FilterChip(selected = it == size, onClick = { size = it }, label = { Text(it) }) } }
        Spacer(Modifier.weight(1f))
        Button(onClick = { }, modifier = Modifier.fillMaxWidth().height(56.dp)) { Text("Send") }
    }
}
