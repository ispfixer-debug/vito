package com.vito.app.presentation.ui.screens.admin

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MartManagementScreen(
    onBack: () -> Unit = {}
) {
    var products by remember { mutableStateOf(listOf(
        "Chips" to 2.99,
        "Soda" to 1.99,
        "Water" to 0.99
    )) }
    var showAddDialog by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Mart Products") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Text("Back")
                    }
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(onClick = { showAddDialog = true }) {
                Icon(Icons.Filled.Add, contentDescription = "Add")
            }
        }
    ) { padding ->
        Column(modifier = Modifier.fillMaxSize().padding(padding)) {
            products.forEach {(name, price) ->
                ListItem(
                    headlineContent = { Text(name) },
                    supportingContent = { Text("$$price") }
                )
            }
        }
    }
    
    if (showAddDialog) {
        AlertDialog(
            onDismissRequest = { showAddDialog = false },
            confirmButton = {
                TextButton(onClick = { showAddDialog = false }) {
                    Text("Add")
                }
            },
            text = { Text("Add new product") }
        )
    }
}
