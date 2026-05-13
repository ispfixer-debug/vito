package com.vito.app.presentation.ui.screens.admin

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
fun DocumentReviewScreen(onBack: () -> Unit = {}) {
    val documents = remember {
        listOf(
            DocumentUi("1", "Ahmed", "license", "https://randomuser.me/api/portraits/men/1.jpg", "pending"),
            DocumentUi("2", "Sara", "registration", "https://randomuser.me/api/portraits/women/2.jpg", "pending")
        )
    }
    
    var selectedDoc by remember { mutableStateOf<DocumentUi?>(null) }
    
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(stringResource(R.string.documents)) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Filled.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { padding ->
        LazyColumn(
            modifier = Modifier.fillMaxSize().padding(padding),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(documents) { doc ->
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    onClick = { selectedDoc = doc }
                ) {
                    Row(
                        modifier = Modifier.padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        // Thumbnail placeholder
                        Card(Modifier.size(60.dp)) {
                            Box(Modifier.fillMaxSize(), Alignment.Center) {
                                Icon(Icons.Filled.Description, null, Modifier.size(32.dp))
                            }
                        }
                        Spacer(Modifier.width(16.dp))
                        Column(modifier = Modifier.weight(1f)) {
                            Text(doc.driverName, style = MaterialTheme.typography.titleMedium)
                            Text(doc.type, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.outline)
                            Surface(
                                color = MaterialTheme.colorScheme.errorContainer,
                                shape = MaterialTheme.shapes.small
                            ) {
                                Text(
                                    doc.status,
                                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                                    style = MaterialTheme.typography.labelSmall
                                )
                            }
                        }
                    }
                }
            }
        }
    }
    
    selectedDoc?.let { doc ->
        AlertDialog(
            onDismissRequest = { selectedDoc = null },
            title = { Text("${doc.driverName}'s ${doc.type}") },
            text = {
                Column {
                    Card(Modifier.fillMaxWidth().height(200.dp)) {
                        Box(Modifier.fillMaxSize(), Alignment.Center) {
                            Icon(Icons.Filled.Image, null, Modifier.size(64.dp))
                        }
                    }
                    Spacer(Modifier.height(16.dp))
                    Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceEvenly) {
                        Button(
                            onClick = { /* Approve */ },
                            colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
                        ) {
                            Icon(Icons.Filled.Check, null)
                            Spacer(Modifier.width(8.dp))
                            Text(stringResource(R.string.approve))
                        }
                        OutlinedButton(
                            onClick = { /* Reject */ },
                            colors = ButtonDefaults.outlinedButtonColors(contentColor = MaterialTheme.colorScheme.error)
                        ) {
                            Icon(Icons.Filled.Close, null)
                            Spacer(Modifier.width(8.dp))
                            Text(stringResource(R.string.reject))
                        }
                    }
                }
            },
            confirmButton = {
                TextButton(onClick = { selectedDoc = null }) {
                    Text("Close")
                }
            }
        )
    }
}

data class DocumentUi(
    val id: String,
    val driverName: String,
    val type: String,
    val imageUrl: String,
    val status: String
)