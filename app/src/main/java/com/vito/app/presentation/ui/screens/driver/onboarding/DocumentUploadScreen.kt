package com.vito.app.presentation.ui.screens.driver.onboarding

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
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
fun DocumentUploadScreen(
    onDocumentsUploaded: () -> Unit = {},
    onBack: () -> Unit = {}
) {
    val documents = remember {
        listOf(
            DocumentType("license", stringResource(R.string.license), false),
            DocumentType("registration", stringResource(R.string.registration), false),
            DocumentType("insurance", stringResource(R.string.insurance_doc), false)
        )
    }
    
    var docStates by remember { mutableStateOf(documents) }
    
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
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            items(docStates.size) { index ->
                val doc = docStates[index]
                Card(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                doc.name,
                                style = MaterialTheme.typography.titleMedium
                            )
                            if (doc.uploaded) {
                                Icon(
                                    Icons.Filled.CheckCircle,
                                    contentDescription = null,
                                    tint = MaterialTheme.colorScheme.primary
                                )
                            }
                        }
                        
                        Spacer(Modifier.height(16.dp))
                        
                        if (!doc.uploaded) {
                            OutlinedButton(
                                onClick = {
                                    docStates = docStates.mapIndexed { i, d ->
                                        if (i == index) d.copy(uploaded = true) else d
                                    }
                                },
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Icon(Icons.Filled.Upload, null)
                                Spacer(Modifier.width(8.dp))
                                Text(stringResource(R.string.upload))
                            }
                        } else {
                            // Show preview placeholder
                            Card(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(120.dp),
                                colors = CardDefaults.cardColors(
                                    containerColor = MaterialTheme.colorScheme.surfaceVariant
                                )
                            ) {
                                Box(
                                    modifier = Modifier.fillMaxSize(),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Icon(
                                        Icons.Filled.Description,
                                        null,
                                        modifier = Modifier.size(48.dp),
                                        tint = MaterialTheme.colorScheme.outline
                                    )
                                }
                            }
                        }
                    }
                }
            }
            
            item {
                Spacer(Modifier.height(16.dp))
                Button(
                    onClick = onDocumentsUploaded,
                    modifier = Modifier.fillMaxWidth(),
                    enabled = docStates.all { it.uploaded }
                ) {
                    Text(stringResource(R.string.next))
                }
            }
        }
    }
}

data class DocumentType(
    val key: String,
    val name: String,
    val uploaded: Boolean
)