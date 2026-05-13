package com.beepbeep.vito.app.ui.screens.car

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun VitoCarScreen() {
    var pickup by remember { mutableStateOf("") }
    var dropoff by remember { mutableStateOf("") }
    var category by remember { mutableStateOf("standard") }
    var fare by remember { mutableStateOf(0) }

    Column(Modifier.fillMaxSize()) {
        Box(Modifier.weight(1f).fillMaxWidth(), contentAlignment = Alignment.Center) {
            Text("Map Placeholder")
        }
        
        Card(Modifier.fillMaxWidth().padding(16.dp)) {
            Column(Modifier.padding(16.dp)) {
                OutlinedTextField(value = pickup, onValueChange = { pickup = it }, label = { Text("Pickup") }, modifier = Modifier.fillMaxWidth())
                Spacer(8.dp))
                OutlinedTextField(value = dropoff, onValueChange = { dropoff = it }, label = { Text("Drop-off") }, modifier = Modifier.fillMaxWidth())
                Spacer(16.dp))
                
                Row(Modifier.fillMaxWidth()) {
                    FilterChip(selected = category == "standard", onClick = { category = "standard" }, label = { Text("Standard") })
                    Spacer(8.dp))
                    FilterChip(selected = category == "premium", onClick = { category = "premium" }, label = { Text("Premium") })
                    Spacer(8.dp))
                    FilterChip(selected = category == "van", onClick = { category = "van" }, label = { Text("Van") })
                }
                
                Spacer(16.dp))
                
                if (fare > 0) {
                    Text("Est. fare: $$fare", style = MaterialTheme.typography.titleLarge)
                    Spacer(16.dp))
                }
                
                Button(onClick = { fare = 1250 }, modifier = Modifier.fillMaxWidth(), enabled = pickup.isNotBlank() && dropoff.isNotBlank()) {
                    Text("Ride Now")
                }
                
                Spacer(8.dp))
                
                OutlinedButton(onClick = { }, modifier = Modifier.fillMaxWidth()) {
                    Text("Schedule")
                }
            }
        }
    }
}
