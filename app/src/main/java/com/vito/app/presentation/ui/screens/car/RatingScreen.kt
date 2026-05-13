package com.vito.app.presentation.ui.screens.car

import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.vito.app.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RatingScreen(
    onSubmit: (Int, Double?, String) -> Unit = { _, _, _ -> }
) {
    var selectedRating by remember { mutableStateOf(0) }
    var selectedTip by remember { mutableStateOf<Double?>(null) }
    var customTip by remember { mutableStateOf("") }
    var tipMethod by remember { mutableStateOf("card") }
    
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = stringResource(R.string.rate_your_trip),
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold
        )
        
        Spacer(modifier = Modifier.height(32.dp))
        
        // Star rating
        Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            (1..5).forEach { star ->
                val isSelected = star <= selectedRating
                
                // Scale animation on press
                val scale by animateFloatAsState(
                    targetValue = if (isSelected) 1.2f else 1f,
                    label = "star_scale"
                )
                
                Icon(
                    imageVector = if (isSelected) Icons.Filled.Star else Icons.Filled.StarBorder,
                    contentDescription = "$star stars",
                    modifier = Modifier
                        .size(48.dp)
                        .scale(scale)
                        .clickable { selectedRating = star },
                    tint = Color(0xFFFFC107)
                )
            }
        }
        
        Spacer(modifier = Modifier.height(32.dp))
        
        // Tip selection
        Text(
            text = stringResource(R.string.tip_driver),
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold
        )
        
        Spacer(modifier = Modifier.height(16.dp))
        
        Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            listOf(1.0, 2.0, 5.0).forEach { amount ->
                FilterChip(
                    selected = selectedTip == amount,
                    onClick = { 
                        selectedTip = if (selectedTip == amount) null else amount 
                        customTip = ""
                    },
                    label = { Text("$${amount.toInt()}") }
                )
            }
        }
        
        Spacer(modifier = Modifier.height(16.dp))
        
        // Custom tip
        OutlinedTextField(
            value = customTip,
            onValueChange = { 
                customTip = it
                selectedTip = it.toDoubleOrNull()
            },
            label = { Text("Custom tip") },
            prefix = { Text("$") },
            modifier = Modifier.fillMaxWidth(),
            enabled = selectedTip == null || customTip.isNotEmpty()
        )
        
        Spacer(modifier = Modifier.height(24.dp))
        
        // Payment method for tip
        if (selectedTip != null && selectedTip!! > 0) {
            Text(
                text = stringResource(R.string.payment_method),
                style = MaterialTheme.typography.titleSmall
            )
            
            Spacer(modifier = Modifier.height(8.dp))
            
            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                listOf("cash" to stringResource(R.string.cash), 
                     "card" to stringResource(R.string.card),
                     "gpay" to stringResource(R.string.google_pay)).forEach { (key, label) ->
                    FilterChip(
                        selected = tipMethod == key,
                        onClick = { tipMethod = key },
                        label = { Text(label) }
                    )
                }
            }
            
            Spacer(modifier = Modifier.height(24.dp))
        }
        
        // Submit button
        Button(
            onClick = { 
                onSubmit(selectedRating, selectedTip, tipMethod)
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp),
            enabled = selectedRating > 0
        ) {
            Text(stringResource(R.string.submit))
        }
    }
}