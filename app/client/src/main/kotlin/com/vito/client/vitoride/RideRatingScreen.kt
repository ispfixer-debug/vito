package com.vito.client.vitoride

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.vito.core.ui.components.PaymentMethod
import com.vito.core.ui.components.PaymentSelector

@Composable
fun RideRatingScreen(
    driverName: String,
    fareAmount: Double,
    onRate: (Int) -> Unit,
    onTip: (Double) -> Unit,
    onPaymentMethodSelected: (PaymentMethod) -> Unit,
    onFinish: () -> Unit,
    modifier: Modifier = Modifier
) {
    var rating by remember { mutableIntStateOf(0) }
    var tipAmount by remember { mutableStateOf(0.0) }
    var selectedPayment by remember { mutableStateOf(PaymentMethod.CARD) }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "Rate your ride",
            style = MaterialTheme.typography.headlineMedium
        )

        Spacer(modifier = Modifier.height(32.dp))

        Text(
            text = driverName,
            style = MaterialTheme.typography.titleLarge
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Star rating
        Row {
            for (i in 1..5) {
                IconButton(onClick = { rating = i }) {
                    Icon(
                        imageVector = Icons.Default.Star,
                        contentDescription = "$i stars",
                        tint = if (i <= rating)
                            MaterialTheme.colorScheme.primary
                        else MaterialTheme.colorScheme.outlineVariant,
                        modifier = Modifier.size(40.dp)
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(32.dp))

        // Fare
        Text(
            text = "Fare: $${String.format("%.2f", fareAmount)}",
            style = MaterialTheme.typography.titleMedium
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Tip
        Text("Add tip", style = MaterialTheme.typography.titleSmall)
        Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            listOf(0.0, 1.0, 2.0, 5.0).forEach { amount ->
                FilterChip(
                    selected = tipAmount == amount,
                    onClick = { tipAmount = amount },
                    label = { Text("$$amount") }
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Payment method
        PaymentSelector(
            selectedMethod = selectedPayment,
            onMethodSelected = { selectedPayment = it }
        )

        Spacer(modifier = Modifier.height(32.dp))

        Button(
            onClick = onFinish,
            modifier = Modifier.fillMaxWidth(),
            enabled = rating > 0
        ) {
            Text("Done")
        }
    }
}