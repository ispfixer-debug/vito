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
fun ProductDetailScreen(
    onBack: () -> Unit = {},
    onAddToCart: () -> Unit = {}
) {
    var quantity by remember { mutableStateOf(1) }
    
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Product Details") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Filled.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp)
        ) {
            Text("Product Name", style = MaterialTheme.typography.headlineMedium)
            Spacer(Modifier.height(8.dp))
            Text("$9.99", style = MaterialTheme.typography.headlineSmall)
            Spacer(Modifier.height(16.dp))
            Text("Description of the product goes here.")
            Spacer(Modifier.weight(1f))
            Row(verticalAlignment = androidx.compose.ui.Alignment.CenterVertically) {
                IconButton(onClick = { if (quantity > 1) quantity-- }) {
                    Text("-")
                }
                Text("$quantity", modifier = Modifier.padding(horizontal = 16.dp))
                IconButton(onClick = { quantity++ }) {
                    Text("+")
                }
            }
            Spacer(Modifier.height(8.dp))
            Button(
                onClick = onAddToCart,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Add to Cart")
            }
        }
    }
}
