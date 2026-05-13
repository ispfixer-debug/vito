package com.beepbeep.vito.app.ui.screens.main

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp

sealed class BottomNav(val route: String, val icon: ImageVector, val label: String) {
    object VitoCar : BottomNav("car", Icons.Default.DirectionsCar, "VitoCar")
    object VitoSend : BottomNav("send", Icons.Default.LocalShipping, "VitoSend")
    object VitoMart : BottomNav("mart", Icons.Default.Store, "VitoMart")
    object Activity : BottomNav("activity", Icons.Default.History, "Activity")
    object Profile : BottomNav("profile", Icons.Default.Person, "Profile")
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(onLogout: () -> Unit) {
    var selectedTab by remember { mutableStateOf(0) }
    val tabs = listOf(BottomNav.VitoCar, BottomNav.VitoSend, BottomNav.VitoMart, BottomNav.Activity, BottomNav.Profile)

    Scaffold(bottomBar = {
        NavigationBar {
            tabs.forEachIndexed { index, tab ->
                NavigationBarItem(icon = { Icon(tab.icon, tab.label) }, label = { Text(tab.label) }, selected = selectedTab == index, onClick = { selectedTab = index })
            }
        }
    }) { padding ->
        Box(Modifier.fillMaxSize().padding(padding)) {
            when (selectedTab) {
                0 -> Text("VitoCar Screen", modifier = Modifier.padding(16.dp))
                1 -> Text("VitoSend Screen", modifier = Modifier.padding(16.dp))
                2 -> Text("VitoMart Screen", modifier = Modifier.padding(16.dp))
                3 -> Text("Activity Screen", modifier = Modifier.padding(16.dp))
                4 -> Text("Profile Screen", modifier = Modifier.padding(16.dp))
            }
        }
    }
}
