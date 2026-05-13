package com.beepbeep.vito.app.ui.screens.chat

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun ChatScreen(chatId: String) {
    var message by remember { mutableStateOf("") }
    val messages = listOf("Hello!", "Hi there!", "Your driver is on the way", "Great, thanks!")

    Column(Modifier.fillMaxSize()) {
        Column(Modifier.weight(1f).padding(16.dp)) {
            messages.forEach { msg ->
                Text(msg, modifier = Modifier.padding(4.dp))
            }
        }
        
        Row(Modifier.padding(8.dp).fillMaxWidth()) {
            OutlinedTextField(value = message, onValueChange = { message = it }, modifier = Modifier.weight(1f), placeholder = { Text("Message") })
            Spacer(8.dp))
            Button(onClick = { message = "" }) { Text("Send") }
        }
    }
}
