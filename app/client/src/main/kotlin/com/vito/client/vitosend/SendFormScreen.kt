package com.vito.client.vitosend

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SendFormScreen(
    recipientName: String,
    recipientPhone: String,
    pickupAddress: String,
    deliveryAddress: String,
    packageDescription: String,
    packageWeight: String,
    isOpenDelivery: Boolean,
    onRecipientNameChange: (String) -> Unit,
    onRecipientPhoneChange: (String) -> Unit,
    onPickupAddressChange: (String) -> Unit,
    onDeliveryAddressChange: (String) -> Unit,
    onPackageDescriptionChange: (String) -> Unit,
    onPackageWeightChange: (String) -> Unit,
    onOpenDeliveryToggle: (Boolean) -> Unit,
    onSendPackage: () -> Unit,
    modifier: Modifier = Modifier
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("VitoSend") }
            )
        }
    ) { padding ->
        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // Recipient info
            Text("Recipient Details", style = MaterialTheme.typography.titleMedium)
            OutlinedTextField(
                value = recipientName,
                onValueChange = onRecipientNameChange,
                label = { Text("Name") },
                leadingIcon = { Icon(Icons.Default.Person, contentDescription = null) },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )
            OutlinedTextField(
                value = recipientPhone,
                onValueChange = onRecipientPhoneChange,
                label = { Text("Phone") },
                leadingIcon = { Icon(Icons.Default.Phone, contentDescription = null) },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Addresses
            Text("Addresses", style = MaterialTheme.typography.titleMedium)
            OutlinedTextField(
                value = pickupAddress,
                onValueChange = onPickupAddressChange,
                label = { Text("Pickup Address") },
                leadingIcon = { Icon(Icons.Default.Inventory, contentDescription = null) },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )
            OutlinedTextField(
                value = deliveryAddress,
                onValueChange = onDeliveryAddressChange,
                label = { Text("Delivery Address") },
                leadingIcon = { Icon(Icons.Default.LocationOn, contentDescription = null) },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Package details
            Text("Package Details", style = MaterialTheme.typography.titleMedium)
            OutlinedTextField(
                value = packageDescription,
                onValueChange = onPackageDescriptionChange,
                label = { Text("Description") },
                leadingIcon = { Icon(Icons.Default.Description, contentDescription = null) },
                modifier = Modifier.fillMaxWidth(),
                maxLines = 3
            )
            OutlinedTextField(
                value = packageWeight,
                onValueChange = onPackageWeightChange,
                label = { Text("Weight (kg)") },
                leadingIcon = { Icon(Icons.Default.Scale, contentDescription = null) },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Open delivery (leave at door)
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column {
                    Text("Open Delivery", style = MaterialTheme.typography.bodyMedium)
                    Text(
                        "Leave package at door",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
                Switch(
                    checked = isOpenDelivery,
                    onCheckedChange = onOpenDeliveryToggle
                )
            }

            Spacer(modifier = Modifier.weight(1f))

            // Send button
            Button(
                onClick = onSendPackage,
                modifier = Modifier.fillMaxWidth(),
                enabled = recipientName.isNotEmpty() && pickupAddress.isNotEmpty() && deliveryAddress.isNotEmpty()
            ) {
                Text("Send Package")
            }
        }
    }
}