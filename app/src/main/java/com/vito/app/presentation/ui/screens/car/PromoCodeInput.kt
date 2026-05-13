package com.vito.app.presentation.ui.screens.car

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.LocalOffer
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.vito.app.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PromoCodeInput(
    appliedCode: String?,
    onApplyCode: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    var code by remember { mutableStateOf("") }
    var isError by remember { mutableStateOf(false) }
    var isApplied by remember { mutableStateOf(false) }
    
    val validCodes = listOf("RIDE10", "SAVE5", "FIRST20", "VIP50")
    
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically
    ) {
        OutlinedTextField(
            value = code,
            onValueChange = { 
                code = it.uppercase()
                isError = false
                isApplied = false
            },
            label = { Text("Promo Code") },
            leadingIcon = { Icon(Icons.Filled.LocalOffer, null) },
            trailingIcon = {
                if (isApplied) {
                    Icon(
                        Icons.Filled.Check,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.primary
                    )
                }
            },
            isError = isError,
            singleLine = true,
            modifier = Modifier.weight(1f)
        )
        
        Spacer(Modifier.width(8.dp))
        
        Button(
            onClick = {
                if (code in validCodes) {
                    onApplyCode(code)
                    isApplied = true
                    isError = false
                } else if (code.isNotEmpty()) {
                    isError = true
                }
            },
            enabled = code.isNotEmpty() && !isApplied
        ) {
            Text("Apply")
        }
    }
    
    if (isError) {
        Text(
            "Invalid promo code",
            color = MaterialTheme.colorScheme.error,
            style = MaterialTheme.typography.bodySmall,
            modifier = Modifier.padding(start = 16.dp, top = 4.dp)
        )
    }
    
    if (isApplied) {
        Text(
            "Code applied!",
            color = MaterialTheme.colorScheme.primary,
            style = MaterialTheme.typography.bodySmall,
            modifier = Modifier.padding(start = 16.dp, top = 4.dp)
        )
    }
}