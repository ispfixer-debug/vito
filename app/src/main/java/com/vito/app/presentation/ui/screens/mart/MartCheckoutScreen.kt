package com.vito.app.presentation.ui.screens.mart

import androidx.compose.foundation.layout.*
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
fun MartCheckoutScreen(
    onPlaceOrder: () -> Unit = {},
    onBack: () -> Unit = {}
) {
    var selectedAddress by remember { mutableStateOf("Home - 123 Main St") }
    var selectedPayment by remember { mutableStateOf("card") }
    
    val addresses = listOf("Home - 123 Main St", "Work - 456 Office Blvd")
    val paymentMethods = listOf(
        "cash" to stringResource(R.string.cash),
        "card" to stringResource(R.string.card),
        "gpay" to stringResource(R.string.google_pay)
    )
    
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(stringResource(R.string.checkout)) },
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
            // Delivery Address
            Text(
                stringResource(R.string.dropoff),
                style = MaterialTheme.typography.titleMedium
            )
            Spacer(Modifier.height(8.dp))
            Card(modifier = Modifier.fillMaxWidth()) {
                Column(Modifier.padding(16.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Filled.Place, null, tint = MaterialTheme.colorScheme.primary)
                        Spacer(Modifier.width(12.dp))
                        Text(selectedAddress, modifier = Modifier.weight(1f))
                        IconButton(onClick = { /* Change address */ }) {
                            Icon(Icons.Filled.Edit, null)
                        }
                    }
                }
            }
            
            Spacer(Modifier.height(24.dp))
            
            // Payment Method
            Text(
                stringResource(R.string.payment_method),
                style = MaterialTheme.typography.titleMedium
            )
            Spacer(Modifier.height(8.dp))
            paymentMethods.forEach { (key, label) ->
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    RadioButton(
                        selected = selectedPayment == key,
                        onClick = { selectedPayment = key }
                    )
                    Text(label)
                }
            }
            
            Spacer(Modifier.weight(1f))
            
            // Order Summary
            Card(modifier = Modifier.fillMaxWidth()) {
                Column(Modifier.padding(16.dp)) {
                    Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                        Text("Subtotal")
                        Text("$25.97")
                    }
                    Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                        Text(stringResource(R.string.delivery))
                        Text("$3.99")
                    }
                    HorizontalDivider(Modifier.padding(vertical = 8.dp))
                    Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                        Text("Total", style = MaterialTheme.typography.titleMedium)
                        Text("$29.96", style = MaterialTheme.typography.titleMedium)
                    }
                }
            }
            
            Spacer(Modifier.height(16.dp))
            
            // Place Order Button
            Button(
                onClick = onPlaceOrder,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp)
            ) {
                Text(stringResource(R.string.checkout))
            }
        }
    }
}