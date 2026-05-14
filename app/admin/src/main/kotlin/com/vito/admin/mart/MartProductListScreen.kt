package com.vito.admin.mart

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

data class Product(
    val id: String,
    val name: String,
    val price: Double,
    val category: String,
    val isAvailable: Boolean
)

@Composable
fun MartProductListScreen(
    products: List<Product>,
    onEditProduct: (String) -> Unit,
    onAddProduct: () -> Unit,
    onToggleAvailability: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Manage Products") },
                actions = {
                    IconButton(onClick = onAddProduct) {
                        Icon(Icons.Default.Add, contentDescription = "Add")
                    }
                }
            )
        }
    ) { padding ->
        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            modifier = modifier
                .fillMaxSize()
                .padding(padding),
            contentPadding = PaddingValues(16.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(products) { product ->
                ProductCard(
                    product = product,
                    onEdit = { onEditProduct(product.id) },
                    onToggle = { onToggleAvailability(product.id) }
                )
            }
        }
    }
}

@Composable
fun ProductCard(
    product: Product,
    onEdit: () -> Unit,
    onToggle: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(modifier = modifier) {
        Column(
            modifier = Modifier.padding(12.dp),
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(product.name, style = MaterialTheme.typography.titleSmall)
                Switch(
                    checked = product.isAvailable,
                    onCheckedChange = { onToggle() }
                )
            }
            Text("$${product.price}", style = MaterialTheme.typography.bodyMedium)
            Text(product.category, style = MaterialTheme.typography.labelSmall)
            Row {
                IconButton(onClick = onEdit, modifier = Modifier.size(32.dp)) {
                    Icon(Icons.Default.Edit, contentDescription = "Edit", modifier = Modifier.size(16.dp))
                }
            }
        }
    }
}