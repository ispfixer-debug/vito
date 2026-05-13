package com.vito.app.presentation.ui.screens.mart

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.vito.app.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProductDetailScreen(
    productId: String,
    onAddToCart: (Int) -> Unit = {},
    onBack: () -> Unit = {}
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
        },
        bottomBar = {
            Surface(tonalElevation = 3.dp) {
                Row(
                    modifier = Modifier.fillMaxWidth().padding(16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Quantity stepper
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        IconButton(onClick = { if (quantity > 1) quantity-- }) {
                            Icon(Icons.Filled.Remove, contentDescription = "Decrease")
                        }
                        Text("$quantity", style = MaterialTheme.typography.titleLarge)
                        IconButton(onClick = { quantity++ }) {
                            Icon(Icons.Filled.Add, contentDescription = "Increase")
                        }
                    }
                    
                    Button(onClick = { onAddToCart(quantity) }) {
                        Text(stringResource(R.string.add_to_cart))
                    }
                }
            }
        }
    ) { padding ->
        Column(
            modifier = Modifier.fillMaxSize().padding(padding).padding(16.dp)
        ) {
            // Image placeholder
            Card(
                modifier = Modifier.fillMaxWidth().height(250.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
            ) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Icon(Icons.Filled.Store, null, Modifier.size(64.dp), MaterialTheme.colorScheme.outline)
                }
            }
            
            Spacer(Modifier.height(16.dp))
            
            // Product info
            Text("Product Name", style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.Bold)
            Text("$19.99", style = MaterialTheme.typography.titleLarge, color = MaterialTheme.colorScheme.primary)
            
            Spacer(Modifier.height(16.dp))
            
            Text("Description", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
            Text("Product description goes here. This is a detailed description of the product.", style = MaterialTheme.typography.bodyMedium)
            
            Spacer(Modifier.height(16.dp))
            
            // Availability
            Surface(color = MaterialTheme.colorScheme.primaryContainer, shape = MaterialTheme.shapes.small) {
                Row(Modifier.padding(horizontal = 12.dp, vertical = 6.dp), verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Filled.Check, null, Modifier.size(16.dp), MaterialTheme.colorScheme.primary)
                    Spacer(Modifier.width(4.dp))
                    Text("In Stock", MaterialTheme.typography.labelMedium)
                }
            }
        }
    }
}