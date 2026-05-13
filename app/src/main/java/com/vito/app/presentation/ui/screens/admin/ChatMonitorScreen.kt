package com.vito.app.presentation.ui.screens.admin

import androidx.compose.foundation.layout.*
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
fun ChatMonitorScreen(onBack: () -> Unit = {}) {
    var searchQuery by remember { mutableStateOf("") }
    var selectedChat by remember { mutableStateOf<ChatInfo?>(null) }
    
    val chats = remember {
        listOf(
            ChatInfo("chat1", "ride_123", "Ahmed", "Client", "2024-01-15 10:30"),
            ChatInfo("chat2", "ride_456", "Sara", "Ahmed", "2024-01-15 10:25"),
            ChatInfo("chat3", "pkg_789", "Khalid", "Driver", "2024-01-15 10:20")
        )
    }
    
    val filteredChats = if (searchQuery.isEmpty()) chats
                      else chats.filter { 
                          it.tripId.contains(searchQuery, ignoreCase = true) ||
                          it.userName.contains(searchQuery, ignoreCase = true)
                      }
    
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Chat Monitor") },
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
        ) {
            // Search
            OutlinedTextField(
                value = searchQuery,
                onValueChange = { searchQuery = it },
                placeholder = { Text("Search by trip ID or user") },
                leadingIcon = { Icon(Icons.Filled.Search, null) },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                singleLine = true
            )
            
            // Chat list
            if (filteredChats.isEmpty()) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        "No chats found",
                        color = MaterialTheme.colorScheme.outline
                    )
                }
            } else {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    filteredChats.forEach { chat ->
                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            onClick = { selectedChat = chat }
                        ) {
                            Row(
                                modifier = Modifier.padding(16.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                // Avatar
                                Icon(
                                    Icons.Filled.Chat,
                                    null,
                                    modifier = Modifier.size(40.dp),
                                    tint = MaterialTheme.colorScheme.primary
                                )
                                Spacer(Modifier.width(16.dp))
                                Column(modifier = Modifier.weight(1f)) {
                                    Text(chat.userName, style = MaterialTheme.typography.titleMedium)
                                    Text(
                                        "Trip: ${chat.tripId}",
                                        style = MaterialTheme.typography.bodySmall,
                                        color = MaterialTheme.colorScheme.outline
                                    )
                                    Text(
                                        chat.lastMessage,
                                        style = MaterialTheme.typography.bodySmall
                                    )
                                }
                                Text(
                                    chat.timestamp,
                                    style = MaterialTheme.typography.labelSmall,
                                    color = MaterialTheme.colorScheme.outline
                                )
                            }
                        }
                    }
                }
            }
        }
    }
    
    // Chat detail dialog
    selectedChat?.let { chat ->
        AlertDialog(
            onDismissRequest = { selectedChat = null },
            title = { Text(chat.userName) },
            text = {
                Column {
                    Text("Trip: ${chat.tripId}")
                    Text("Role: ${chat.role}")
                    Spacer(Modifier.height(16.dp))
                    Text("Last message preview...")
                }
            },
            confirmButton = {
                TextButton(onClick = { selectedChat = null }) {
                    Text("View Full Chat")
                }
            },
            dismissButton = {
                TextButton(onClick = { selectedChat = null }) {
                    Text(stringResource(R.string.cancel))
                }
            }
        )
    }
}

data class ChatInfo(
    val id: String,
    val tripId: String,
    val userName: String,
    val role: String,
    val timestamp: String,
    val lastMessage: String = "Click to view messages..."
)