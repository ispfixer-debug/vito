package com.beepbeep.vito.app.ui.screens.profile

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun ProfileScreen(onLogout: () -> Unit) {
    var alias by remember { mutableStateOf("Oussama") }
    var walletBalance by remember { mutableStateOf(25.00) }
    var appLock by remember { mutableStateOf(false) }

    Column(Modifier.fillMaxSize().padding(16.dp)) {
        Text("Profile", style = MaterialTheme.typography.headlineLarge)
        Spacer(24.dp))
        
        OutlinedTextField(value = alias, onValueChange = { alias = it }, label = { Text("Alias") }, modifier = Modifier.fillMaxWidth())
        Spacer(16.dp))
        
        Card(Modifier.fillMaxWidth()) {
            Row(Modifier.padding(16.dp).fillMaxWidth()) {
                Text("Wallet: $${"%.2f".format(walletBalance)}", style = MaterialTheme.typography.titleLarge, modifier = Modifier.weight(1f))
                Button(onClick = { }) { Text("Top Up") }
            }
        }
        
        Spacer(16.dp))
        
        Text("Preferred Payment", style = MaterialTheme.typography.titleMedium)
        Column {
            Row { RadioButton(selected = true, onClick = { }); Text("Cash") }
            Row { RadioButton(selected = false, onClick = { }); Text("Card") }
            Row { RadioButton(selected = false, onClick = { }); Text("Google Pay") }
        }
        
        Spacer(16.dp))
        
        Row(Modifier.fillMaxWidth()) {
            Text("App Lock"); Switch(checked = appLock, onCheckedChange = { appLock = it })
        }
        
        Spacer(16.dp))
        
        OutlinedButton(onClick = { }, modifier = Modifier.fillMaxWidth()) { Text("Link New Device") }
        
        Spacer(16.dp))
        
        OutlinedButton(onClick = onLogout, modifier = Modifier.fillMaxWidth(), colors = ButtonDefaults.outlinedButtonColors(contentColor = MaterialTheme.colorScheme.error)) { Text("Sign Out") }
    }
}
