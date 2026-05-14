package com.vito.admin.qr

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.vito.core.ui.components.GenerateQrCode

data class QrToken(
    val id: String,
    val content: String,
    val userName: String,
    val userRole: String,
    val createdAt: Long,
    val isActive: Boolean
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun QrManagementScreen(
    contents: List<QrToken>,
    onGenerateToken: (String, String) -> Unit,
    onRevokeToken: (String) -> Unit,
    onDownloadToken: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    var showGenerateDialog by remember { mutableStateOf(false) }
    var selectedToken by remember { mutableStateOf<QrToken?>(null) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("QR Token Management") },
                actions = {
                    IconButton(onClick = { showGenerateDialog = true }) {
                        Icon(Icons.Default.Add, contentDescription = "Generate")
                    }
                }
            )
        }
    ) { padding ->
        LazyColumn(
            modifier = modifier
                .fillMaxSize()
                .padding(padding),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(contents) { content ->
                QrTokenCard(
                    content = content,
                    onShowQr = { selectedToken = content },
                    onRevoke = { onRevokeToken(content.id) },
                    onDownload = { onDownloadToken(content.id) }
                )
            }
        }
    }

    // Show QR dialog
    selectedToken?.let { content ->
        AlertDialog(
            onDismissRequest = { selectedToken = null },
            title = { Text(content.userName) },
            text = { GenerateQrCode(content = content.content) },
            confirmButton = {
                TextButton(onClick = { selectedToken = null }) {
                    Text("Close")
                }
            }
        )
    }

    // Generate dialog
    if (showGenerateDialog) {
        var userName by remember { mutableStateOf("") }
        var userRole by remember { mutableStateOf("CLIENT") }

        AlertDialog(
            onDismissRequest = { showGenerateDialog = false },
            title = { Text("Generate QR Token") },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    OutlinedTextField(
                        value = userName,
                        onValueChange = { userName = it },
                        label = { Text("User Name") }
                    )
                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        listOf("CLIENT", "DRIVER", "RESTAURANT").forEach { role ->
                            FilterChip(
                                selected = userRole == role,
                                onClick = { userRole = role },
                                label = { Text(role) }
                            )
                        }
                    }
                }
            },
            confirmButton = {
                TextButton(onClick = {
                    onGenerateToken(userName, userRole)
                    showGenerateDialog = false
                }) {
                    Text("Generate")
                }
            },
            dismissButton = {
                TextButton(onClick = { showGenerateDialog = false }) {
                    Text("Cancel")
                }
            }
        )
    }
}

@Composable
fun QrTokenCard(
    content: QrToken,
    onShowQr: () -> Unit,
    onRevoke: () -> Unit,
    onDownload: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(modifier = modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(content.userName, style = MaterialTheme.typography.titleMedium)
                Text(content.userRole, style = MaterialTheme.typography.bodySmall)
                Text(
                    content.content.take(8) + "...",
                    style = MaterialTheme.typography.labelSmall
                )
            }
            Row {
                IconButton(onClick = onShowQr) {
                    Icon(Icons.Default.QrCode, contentDescription = "Show")
                }
                IconButton(onClick = onDownload) {
                    Icon(Icons.Default.Download, contentDescription = "Download")
                }
                IconButton(onClick = onRevoke) {
                    Icon(Icons.Default.Delete, contentDescription = "Revoke")
                }
            }
        }
    }
}