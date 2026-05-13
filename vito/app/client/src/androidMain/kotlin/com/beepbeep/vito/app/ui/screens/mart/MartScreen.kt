package com.beepbeep.vito.app.ui.screens.mart

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun MartScreen() {
    var selectedCategory by remember { mutableStateOf("All") }
    val categories = listOf("All", "Drinks", "Snacks", "Groceries")
    var cartCount by remember { mutableStateOf(0) }

    Column(Modifier.fillMaxSize()) {
        // Category chips
        Row(Modifier.padding(8.dp).fillMaxWidth()) {
            categories.forEach { cat ->
                FilterChip(selected = selectedCategory == cat, onClick = { selectedCategory = cat }, label = { Text(cat) })
                Spacer(4.dp))
            }
        }
        
        // Products grid (simplified list)
        Column(Modifier.weight(1f).padding(16.dp)) {
            Text("Coca Cola - $1.50")
            Text("Water - $0.50")
            Text("Chips - $2.00")
            Text("Chocolate - $2.50")
        }
        
        // Cart button
        FloatingActionButton(onClick = { }, modifier = Modifier.padding(16.dp)) {
            Text("Cart ($cartCount)")
        }
    }
}
