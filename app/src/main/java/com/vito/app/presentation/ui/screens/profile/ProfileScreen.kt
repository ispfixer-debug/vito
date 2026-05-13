package com.vito.app.presentation.ui.screens.profile

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.vito.app.domain.model.UserRole

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(viewModel: ProfileViewModel) {
    val st by viewModel.s.collectAsState()
    val user by viewModel.currentUser.collectAsState()

    Column(
        Modifier.fillMaxSize().verticalScroll(rememberScrollState()).padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        user?.let { u ->
            AsyncImage(
                model = u.photoUrl,
                contentDescription = "Profile",
                modifier = Modifier.size(80.dp),
                contentScale = ContentScale.Crop
            )
            Text(u.alias)
            Text(u.email ?: "")
        }
        
        Spacer(Modifier.height(24.dp))
        
        Card(Modifier.fillMaxWidth()) {
            Row(Modifier.padding(16.dp), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                Column { Text("Balance"); Text("$${st.balance}", style = MaterialTheme.typography.headlineLarge) }
                Button(onClick = { viewModel.topUp(25.0) }) { Icon(Icons.Filled.Add, null); Text("Top Up") }
            }
        }
        
        Spacer(Modifier.height(16.dp))
        
        Card(Modifier.fillMaxWidth()) {
            Column(Modifier.padding(16.dp)) {
                Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                    Text("App Lock")
                    Switch(checked = st.appLock, onCheckedChange = { viewModel.toggleLock() })
                }
                HorizontalDivider()
                Button(onClick = { viewModel.logout() }, Modifier.fillMaxWidth()) { Text("Logout") }
            }
        }
        
        Spacer(Modifier.height(16.dp))
        
        Card(Modifier.fillMaxWidth()) {
            Column(Modifier.padding(16.dp)) {
                Text("Dev Options")
                var expanded by remember { mutableStateOf(false) }
                ExposedDropdownMenuBox(expanded = expanded, onExpandedChange = { expanded = it }) {
                    OutlinedTextField(st.role.name, {}, readOnly = true, trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) }, modifier = Modifier.menuAnchor())
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
    }
}
