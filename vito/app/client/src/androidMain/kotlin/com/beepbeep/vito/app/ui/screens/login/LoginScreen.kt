package com.beepbeep.vito.app.ui.screens.login

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp

@Composable
fun LoginScreen(onLoginSuccess: () -> Unit) {
    var username by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var error by remember { mutableStateOf<String?>(null) }

    Column(Modifier.fillMaxSize().padding(24.dp), horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.Center) {
        Text("Welcome to Vito", style = MaterialTheme.typography.headlineLarge)
        Spacer(Modifier.height(8.dp))
        Text("Sign in to continue", style = MaterialTheme.typography.bodyLarge)
        Spacer(32.dp))

        OutlinedTextField(value = username, onValueChange = { username = it }, label = { Text("Username") }, modifier = Modifier.fillMaxWidth())
        Spacer(16.dp))

        OutlinedTextField(value = password, onValueChange = { password = it }, label = { Text("Password") }, modifier = Modifier.fillMaxWidth(), visualTransformation = PasswordVisualTransformation())

        error?.let { Spacer(16.dp)); Text(it, color = MaterialTheme.colorScheme.error) }

        Spacer(24.dp))

        Button(onClick = { if (username == "oussama" && password == "oussama") { onLoginSuccess() } else { error = "Invalid credentials" } }, modifier = Modifier.fillMaxWidth().height(50.dp), enabled = username.isNotBlank() && password.isNotBlank()) {
            Text("Sign In")
        }

        Spacer(16.dp))
        Text("Use: oussama / oussama", style = MaterialTheme.typography.bodySmall)
    }
}
