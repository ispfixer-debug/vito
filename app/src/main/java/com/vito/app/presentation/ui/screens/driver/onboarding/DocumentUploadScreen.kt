package com.vito.app.presentation.ui.screens.driver.onboarding

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

data class Document(val id: String, val label: String, var uploaded: Boolean)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DocumentUploadScreen(
    onComplete: () -> Unit = {},
    onBack: () -> Unit = {}
) {
    var docs by remember { 
        mutableStateOf(listOf(
            Document("license", "Driver License", false),
            Document("registration", "Vehicle Registration", false),
            Document("insurance", "Insurance", false)
        ))
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Upload Documents") },
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
            docs.forEachIndexed { index, doc ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(doc.label)
                        if (doc.uploaded) {
                            Text("Uploaded")
                        } else {
                            Button(onClick = {
                                docs = docs.toMutableList().apply {
                                    this[index] = Document(doc.id, doc.label, true)
                                }
                            }) {
                                Text("Upload")
                            }
                        }
                    }
                }
            }
            Spacer(Modifier.weight(1f))
            Button(
                onClick = onComplete,
                modifier = Modifier.fillMaxWidth(),
                enabled = docs.all { it.uploaded }
            ) {
                Text("Continue")
            }
        }
    }
}
