package com.vito.app.presentation.ui.screens.profile

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Language
import androidx.compose.material.icons.filled.QLock
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.vito.app.R
import com.vito.app.domain.model.UserRole

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(viewModel: ProfileViewModel) {
    val st by viewModel.s.collectAsState()
    val user by viewModel.currentUser.collectAsState()
    
    var showDeleteDialog by remember { mutableStateOf(false) }
    var selectedLanguage by remember { mutableStateOf("English") }
    var languageExpanded by remember { mutableStateOf(false) }
    val languages = listOf("English", "Español")
    
    Column(
        Modifier.fillMaxSize().verticalScroll(rememberScrollState()).padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        user?.let { u ->
            Box(Modifier.size(80.dp), Alignment.Center) {
                AsyncImage(
                    model = u.photoUrl,
                    contentDescription = "Profile",
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop
                )
            }
            Spacer(Modifier.height(8.dp))
            Text(u.alias, style = MaterialTheme.typography.headlineSmall)
            Text(u.email ?: "", style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.outline)
        }
        
        Spacer(Modifier.height(24.dp))
        
        // Wallet Balance Card
        Card(Modifier.fillMaxWidth()) {
            Row(Modifier.padding(16.dp), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                Column { 
                    Text(stringResource(R.string.wallet), style = MaterialTheme.typography.bodySmall)
                    Text("$${st.balance}", style = MaterialTheme.typography.headlineLarge) 
                }
                Button(onClick = { viewModel.topUp(25.0) }) { 
                    Icon(Icons.Filled.Add, null); Spacer(Modifier.width(4.dp))
                    Text(stringResource(R.string.top_up)) 
                }
            }
        }
        
        Spacer(Modifier.height(16.dp))
        
        // Payment Methods Card
        Card(Modifier.fillMaxWidth()) {
            Column(Modifier.padding(16.dp)) {
                Text(stringResource(R.string.payment_method), style = MaterialTheme.typography.titleMedium)
                Spacer(Modifier.height(8.dp))
                listOf(
                    stringResource(R.string.cash) to "cash",
                    stringResource(R.string.card) to "card",
                    stringResource(R.string.google_pay) to "gpay"
                ).forEach { (label, _) ->
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        RadioButton(
                            selected = st.paymentMethod == label,
                            onClick = { viewModel.setPaymentMethod(label) }
                        )
                        Text(label)
                    }
                }
                HorizontalDivider(Modifier.padding(vertical = 8.dp))
                Text("Visa ****4242", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.outline)
            }
        }
        
        Spacer(Modifier.height(16.dp))
        
        // Settings Card
        Card(Modifier.fillMaxWidth()) {
            Column(Modifier.padding(16.dp)) {
                // App Lock Toggle
                Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Filled.QLock, null)
                        Spacer(Modifier.width(8.dp))
                        Text(stringResource(R.string.app_lock))
                    }
                    Switch(checked = st.appLock, onCheckedChange = { viewModel.toggleLock() })
                }
                
                HorizontalDivider(Modifier.padding(vertical = 8.dp))
                
                // Language Selector
                Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Filled.Language, null)
                    Spacer(Modifier.width(8.dp))
                    Text(stringResource(R.string.language))
                    Spacer(Modifier.weight(1f))
                    ExposedDropdownMenuBox(
                        expanded = languageExpanded,
                        onExpandedChange = { languageExpanded = it }
                    ) {
                        OutlinedTextField(
                            selectedLanguage,
                            {},
                            readOnly = true,
                            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(languageExpanded) },
                            modifier = Modifier.menuAnchor(),
                            enabled = false
                        )
                        ExposedDropdownMenu(expanded = languageExpanded, onDismissRequest = { languageExpanded = false }) {
                            languages.forEach { lang ->
                                DropdownMenuItem(
                                    text = { Text(lang) },
                                    onClick = {
                                        selectedLanguage = lang
                                        languageExpanded = false
                                        // TODO: Actually change locale
                                    }
                                )
                            }
                        }
                    }
                }
                
                HorizontalDivider(Modifier.padding(vertical = 8.dp))
                
                // Link New Device
                Button(
                    onClick = { /* TODO: Open QR Scanner */ },
                    modifier = Modifier.fillMaxWidth(),
                    enabled = false // Placeholder
                ) {
                    Text(stringResource(R.string.link_new_device))
                }
            }
        }
        
        Spacer(Modifier.height(16.dp))
        
        // Dev Options Card
        Card(Modifier.fillMaxWidth()) {
            Column(Modifier.padding(16.dp)) {
                Text("Dev Options", style = MaterialTheme.typography.titleMedium)
                var expanded by remember { mutableStateOf(false) }
                ExposedDropdownMenuBox(expanded = expanded, onExpandedChange = { expanded = it }) {
                    OutlinedTextField(
                        st.role.name,
                        {},
                        readOnly = true,
                        trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
                        modifier = Modifier.menuAnchor()
                    )
                    ExposedDropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
                        UserRole.entries.forEach { role ->
                            DropdownMenuItem(
                                text = { Text(role.name) },
                                onClick = { viewModel.setRole(role); expanded = false }
                            )
                        }
                    }
                }
            }
        }
        
        Spacer(Modifier.height(16.dp))
        
        // Logout / Delete Account
        OutlinedButton(
            onClick = { viewModel.logout() },
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.outlinedButtonColors(contentColor = MaterialTheme.colorScheme.error)
        ) {
            Text(stringResource(R.string.logout))
        }
        
        Spacer(Modifier.height(8.dp))
        
        TextButton(
            onClick = { showDeleteDialog = true },
            colors = ButtonDefaults.textButtonColors(contentColor = MaterialTheme.colorScheme.error)
        ) {
            Icon(Icons.Filled.Delete, null)
            Spacer(Modifier.width(4.dp))
            Text(stringResource(R.string.delete_account))
        }
    }
    
    // Delete Account Confirmation Dialog
    if (showDeleteDialog) {
        AlertDialog(
            onDismissRequest = { showDeleteDialog = false },
            title = { Text(stringResource(R.string.delete_account)) },
            text = { Text("Are you sure you want to delete your account? This action cannot be undone.") },
            confirmButton = {
                TextButton(
                    onClick = {
                        viewModel.deleteAccount()
                        showDeleteDialog = false
                    },
                    colors = ButtonDefaults.textButtonColors(contentColor = MaterialTheme.colorScheme.error)
                ) {
                    Text(stringResource(R.string.delete))
                }
            },
            dismissButton = {
                TextButton(onClick = { showDeleteDialog = false }) {
                    Text(stringResource(R.string.cancel))
                }
            }
        )
    }
}
