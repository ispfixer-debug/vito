package com.vito.app.presentation.ui.screens.mart

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.vito.app.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CartScreen(
    onCheckout: () -> Unit = {},
    onBack: () -> Unit = {}
) {
    var cartItems by remember { 
        mutableStateOf(listOf(
                CartItemUi("1", "Product 1", 2, 19.99),
                CartItemUi("2", "Product 2", 1, 12.50),
                CartItemUi("3", "Product 3", 3, 5.99)
            )) 
    }
    val subtotal = cartItems.sumOf { it.price * it.quantity }
    val deliveryFee = 3.99
    val total = subtotal + deliveryFee
    
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(stringResource(R.string.cart)) },
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
                        Text("Subtotal", MaterialTheme.typography.bodyMedium)
                        Text("$${String.format("%.2f", subtotal)}", MaterialTheme.typography.bodyMedium)
                    }
                    Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                        Text(stringResource(R.string.delivery), MaterialTheme.typography.bodyMedium)
                        Text("$$deliveryFee", MaterialTheme.typography.bodyMedium)
                    }
                    HorizontalDivider(Modifier.padding(vertical = 8.dp))
                    Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                        Text("Total", MaterialTheme.typography.titleLarge)
                        Text("$${String.format("%.2f", total)}", MaterialTheme.typography.titleLarge)
                    }
                    Spacer(Modifier.height(16.dp))
                    Button(
                        onClick = onCheckout,
                        modifier = Modifier.fillMaxWidth().height(56.dp)
                    ) {
                        Text(stringResource(R.string.proceed_to_checkout))
                    }
                }
            }
        }
    ) { padding ->
        if (cartItems.isEmpty()) {
            Box(modifier = Modifier.fillMaxSize().padding(padding), contentAlignment = Alignment.Center) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Icon(Icons.Filled.ShoppingCart, null, Modifier.size(64.dp), MaterialTheme.colorScheme.outline)
                    Spacer(Modifier.height(16.dp))
                    Text("Cart is empty", MaterialTheme.typography.bodyLarge, MaterialTheme.colorScheme.outline)
                }
            }
        } else {
            LazyColumn(
                modifier = Modifier.fillMaxSize().padding(padding),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(cartItems, key = { it.id }) { item ->
                    SwipeToDismissBox(
                        state = rememberSwipeToDismissBoxState(
                            confirmValueChange = { dismiss ->
                                if (dismiss == SwipeToDismissBoxValue.EndToStart) {
                                    cartItems = cartItems.filter { it.id != item.id }
                                    true
                                } else false
                            }
                        ),
                        backgroundContent = {
                            Box(
                                modifier = Modifier.fillMaxSize().background(MaterialTheme.colorScheme.errorContainer).padding(16.dp),
                                contentAlignment = Alignment.CenterEnd
                            ) {
                                Icon(Icons.Filled.Delete, null, MaterialTheme.colorScheme.error)
                            }
                        },
                        enableDismissFromStartToEnd = false
                    ) {
                        CartItemCard(
                            item = item,
                            onRemove = { cartItems = cartItems.filter { it.id != item.id } },
                            onQuantityChange = { newQty ->
                                cartItems = cartItems.map { 
                                    if (it.id == item.id) it.copy(quantity = newQty) else it 
                                }
                            }
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun CartItemCard(
    item: CartItemUi,
    onRemove: () -> Unit = {},
    onQuantityChange: (Int) -> Unit = {}
) {
    Card(modifier = Modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(item.name, MaterialTheme.typography.titleMedium)
                Text("$${item.price} each", MaterialTheme.typography.bodyMedium)
            }
            
            Row(verticalAlignment = Alignment.CenterVertically) {
                IconButton(onClick = { if (item.quantity > 1) onQuantityChange(item.quantity - 1) }) {
                    Icon(Icons.Filled.Remove, contentDescription = null, Modifier.size(20.dp))
                }
                Text("${item.quantity}", MaterialTheme.typography.titleMedium)
                IconButton(onClick = { onQuantityChange(item.quantity + 1) }) {
                    Icon(Icons.Filled.Add, contentDescription = null, Modifier.size(20.dp))
                }
            }
        }
    }
}

data class CartItemUi(
    val id: String,
    val name: String,
    val quantity: Int,
    val price: Double
)