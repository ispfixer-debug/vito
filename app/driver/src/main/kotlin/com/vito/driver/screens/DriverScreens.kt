package com.vito.driver.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.vito.driver.viewmodel.DriverAuthViewModel

@Composable
fun DriverLoginScreen(
    onLoginSuccess: () -> Unit,
    authViewModel: DriverAuthViewModel = hiltViewModel()
) {
    val authState by authViewModel.authState.collectAsState()

    LaunchedEffect(authState.isLoggedIn) {
        if (authState.isLoggedIn) {
            onLoginSuccess()
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text("Vito Driver", style = MaterialTheme.typography.headlineLarge)
        
        Spacer(modifier = Modifier.height(32.dp))

        Button(
            onClick = { authViewModel.loginWithDemo() },
            enabled = !authState.isLoading,
            modifier = Modifier.fillMaxWidth()
        ) {
            if (authState.isLoading) {
                CircularProgressIndicator(modifier = Modifier.size(24.dp))
            } else {
                Text("Start Driving")
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DriverHomeScreen(
    onLogout: () -> Unit,
    authViewModel: DriverAuthViewModel = hiltViewModel()
) {
    val authState by authViewModel.authState.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Vito Driver") },
                actions = {
                    TextButton(onClick = onLogout) {
                        Text("Logout")
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Online toggle
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Go Online", style = MaterialTheme.typography.titleMedium)
                Spacer(modifier = Modifier.weight(1f))
                Switch(
                    checked = authState.isOnline,
                    onCheckedChange = { authViewModel.setOnline(it) }
                )
            }

            Spacer(modifier = Modifier.height(32.dp))

            if (authState.isOnline) {
                Card(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp)
                    ) {
                        Text("Available Rides", style = MaterialTheme.typography.titleMedium)
                        Spacer(modifier = Modifier.height(8.dp))
                        Text("No rides available nearby")
                        Spacer(modifier = Modifier.height(16.dp))
                        Button(
                            onClick = { /* Accept ride */ },
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text("Refresh")
                        }
                    }
                }
            } else {
                Text(
                    "You're offline. Go online to receive ride requests.",
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }
    }
}