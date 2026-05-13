package com.vito.app.presentation.ui.screens.admin

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.vito.app.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun QrManagementScreen(onBack: () -> Unit = {}) {
    var tokenRole by remember { mutableStateOf("client") }
    var tokenCount by remember { mutableStateOf(1) }
    var generatedTokens by remember { mutableStateOf<List<String>>(emptyList()) }
    
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(stringResource(R.string.qr_management)) },
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
            // Generate form
            Card(modifier = Modifier.fillMaxWidth()) {
                Column(Modifier.padding(16.dp)) {
                    Text(
                        stringResource(R.string.generate_token),
                        style = MaterialTheme.typography.titleMedium
                    )
                    Spacer(Modifier.height(16.dp))
                    
                    // Role selector
                    var expanded by remember { mutableStateOf(false) }
                    ExposedDropdownMenuBox(expanded = expanded, onExpandedChange = { expanded = it }) {
                        OutlinedTextField(
                            tokenRole,
                            {},
                            readOnly = true,
                            label = { Text("Role") },
                            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
                            modifier = Modifier.menuAnchor()
                        )
                        ExposedDropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
                            listOf("client", "driver", "admin").forEach { role ->
                                DropdownMenuItem(
                                    text = { Text(role) },
                                    onClick = {
                                        tokenRole = role
                                        expanded = false
                                    }
                                )
                            }
                        }
                    }
                    
                    Spacer(Modifier.height(16.dp))
                    
                    // Token count
                    Row(verticalAlignment = androidx.compose.ui.Alignment.CenterVertically) {
                        Text("Tokens: ")
                        IconButton(onClick = { if (tokenCount > 1) tokenCount-- }) { Icon(Icons.Filled.Remove, null) }
                        Text("$tokenCount")
                        IconButton(onClick = { if (tokenCount < 10) tokenCount++ }) { Icon(Icons.Filled.Add, null) }
                    }
                    
                    Spacer(Modifier.height(16.dp))
                    
                    Button(
                        onClick = {
                            val newTokens = (1..tokenCount).map { 
                                java.util.UUID.randomUUID().toString().take(8).uppercase()
                            }
                            generatedTokens = generatedTokens + newTokens
                        },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(stringResource(R.string.generate_token))
                    }
                }
            }
            
            Spacer(Modifier.height(16.dp))
            
            // Generated tokens list
            if (generatedTokens.isNotEmpty()) {
                Text(
                    "Generated Tokens",
                    style = MaterialTheme.typography.titleMedium
                )
                Spacer(Modifier.height(8.dp))
                
                generatedTokens.forEach { token ->
                    Card(modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp)) {
                        Row(Modifier.padding(16.dp)) {
                            Text(token, modifier = Modifier.weight(1f))
                            IconButton(onClick = {
                                // Copy to clipboard would go here
                            }) {
                                Icon(Icons.Filled.ContentCopy, null)
                            }
                        }
                    }
                }
            }
        }
    }
}