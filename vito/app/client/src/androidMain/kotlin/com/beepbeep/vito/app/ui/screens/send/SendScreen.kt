package com.beepbeep.vito.app.ui.screens.send

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun SendScreen() {
    var pickup by remember { mutableStateOf("") }
    var dropoff by remember { mutableStateOf("") }
    var size by remember { mutableStateOf("small") }
    var weight by remember { mutableStateOf("") }
    var fragile by remember { mutableStateOf(false) }
    var description by remember { mutableStateOf("") }

    Column(Modifier.fillMaxSize().padding(16.dp)) {
        OutlinedTextField(value = pickup, onValueChange = { pickup = it }, label = { Text("Pickup") }, modifier = Modifier.fillMaxWidth())
        Spacer(8.dp))
        OutlinedTextField(value = dropoff, onValueChange = { dropoff = it }, label = { Text("Drop-off") }, modifier = Modifier.fillMaxWidth())
        Spacer(16.dp))
        
        Text("Size", style = MaterialTheme.typography.titleMedium)
        Row {
            FilterChip(selected = size == "small", onClick = { size = "small" }, label = { Text("S") })
            Spacer(8.dp))
            FilterChip(selected = size == "medium", onClick = { size = "medium" }, label = { Text("M") })
            Spacer(8.dp))
            FilterChip(selected = size == "large", onClick = { size = "large" }, label = { Text("L") })
        }
        Spacer(16.dp))
        
        OutlinedTextField(value = weight, onValueChange = { weight = it }, label = { Text("Weight (kg)") }, modifier = Modifier.fillMaxWidth())
        Spacer(16.dp))
        
        Row { Text("Fragile"); Switch(checked = fragile, onCheckedChange = { fragile = it }) }
        Spacer(16.dp))
        
        OutlinedTextField(value = description, onValueChange = { description = it }, label = { Text("Description") }, modifier = Modifier.fillMaxWidth(), minLines = 3)
        Spacer(24.dp))
        
        Button(onClick = { }, modifier = Modifier.fillMaxWidth(), enabled = pickup.isNotBlank() && dropoff.isNotBlank()) {
            Text("Send Package")
        }
    }
}
