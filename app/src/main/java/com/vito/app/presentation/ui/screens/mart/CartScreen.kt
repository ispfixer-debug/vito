package com.vito.app.presentation.ui.screens.mart

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CartScreen(
    onBack: () -> Unit = {},
    onCheckout: () -> Unit = {}
) {
    var cartItems by remember { mutableStateOf<List<Pair<String, Double>>>(emptyList()) }
    
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Cart") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Filled.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        },
        bottomBar = {
            Surface(tonalElevation = 3.dp) {
                Column(Modifier.fillMaxWidth().padding(16.dp)) {
                    Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                        Text("Subtotal")
                        Text("$${cartItems.sumOf { it.second }}")
                    }
                    Spacer(Modifier.height(8.dp))
                    Button(onClick = onCheckout, modifier = Modifier.fillMaxWidth()) {
                        Text("Checkout")
                    }
                }
            }
        }
    ) { padding ->
        Column(modifier = Modifier.fillMaxSize().padding(padding)) {
            if (cartItems.isEmpty()) {
                Text("Your cart is empty", modifier = Modifier.padding(16.dp))
            } else {
                cartItems.forEach { (name, price) ->
                    ListItem(
                        headlineContent = { Text(name) },
                        supportingContent = { Text("$$price") }
                    )
                }
            }
        }
    }
}
