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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.vito.app.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UserManagementScreen(onBack: () -> Unit = {}) {
    val users = remember {
        listOf(
            UserUi("1", "user1", "Oussama", "client", true),
            UserUi("2", "driver1", "Ahmed", "driver", true),
            UserUi("3", "user2", "Sara", "client", false),
            UserUi("4", "driver2", "Khalid", "driver", true)
        )
    }
    
    var selectedUser by remember { mutableStateOf<UserUi?>(null) }
    
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(stringResource(R.string.users)) },
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
            items(users) { user ->
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    onClick = { selectedUser = user }
                ) {
                    Row(
                        modifier = Modifier.padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text(user.username, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                            Text(user.alias, style = MaterialTheme.typography.bodyMedium)
                            Text(user.role, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.outline)
                        }
                        
                        Surface(
                            color = if (user.isActive) MaterialTheme.colorScheme.primaryContainer 
                                   else MaterialTheme.colorScheme.errorContainer,
                            shape = MaterialTheme.shapes.small
                        ) {
                            Text(
                                if (user.isActive) "Active" else "Suspended",
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                                style = MaterialTheme.typography.labelSmall
                            )
                        }
                    }
                }
            }
        }
    }
    
    // User actions dialog
    selectedUser?.let { user ->
        AlertDialog(
            onDismissRequest = { selectedUser = null },
            title = { Text(user.alias) },
            text = {
                Column {
                    Button(
                        onClick = { /* Toggle active */ },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(if (user.isActive) "Suspend User" else "Activate User")
                    }
                    Spacer(Modifier.height(8.dp))
                    OutlinedButton(
                        onClick = { /* Delete */ },
                        modifier = Modifier.fillMaxWidth(),
                        colors = ButtonDefaults.outlinedButtonColors(contentColor = MaterialTheme.colorScheme.error)
                    ) {
                        Text("Delete Account")
                    }
                }
            },
            confirmButton = {
                TextButton(onClick = { selectedUser = null }) {
                    Text("Close")
                }
            }
        )
    }
}

data class UserUi(
    val id: String,
    val username: String,
    val alias: String,
    val role: String,
    val isActive: Boolean
)