package com.vito.app.presentation.ui.navigation

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DirectionsCar
import androidx.compose.material.icons.filled.Inventory
import androidx.compose.material.icons.filled.Store
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.outlined.DirectionsCar
import androidx.compose.material.icons.outlined.Inventory
import androidx.compose.material.icons.outlined.Store
import androidx.compose.material.icons.outlined.History
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.vito.app.presentation.ui.screens.car.VitoCarScreen
import com.vito.app.presentation.ui.screens.send.SendScreen
import com.vito.app.presentation.ui.screens.mart.MartScreen
import com.vito.app.presentation.ui.screens.activity.ActivityScreen
import com.vito.app.presentation.ui.screens.profile.ProfileScreen
import com.vito.app.presentation.ui.screens.profile.ProfileViewModel

sealed class BottomNavItem(
    val route: String,
    val title: String,
    val selectedIcon: ImageVector,
    val unselectedIcon: ImageVector
) {
    data object Ride : BottomNavItem(
        "ride", "Ride", Icons.Filled.DirectionsCar, Icons.Outlined.DirectionsCar
    )
    data object Send : BottomNavItem(
        "send", "Send", Icons.Filled.Inventory, Icons.Outlined.Inventory
    )
    data object Mart : BottomNavItem(
        "mart", "Mart", Icons.Filled.Store, Icons.Outlined.Store
    )
    data object Activity : BottomNavItem(
        "activity", "Activity", Icons.Filled.History, Icons.Outlined.History
    )
    data object Profile : BottomNavItem(
        "profile", "Profile", Icons.Filled.Person, Icons.Outlined.Person
    )
}

val clientNavItems = listOf(
    BottomNavItem.Ride, BottomNavItem.Send, BottomNavItem.Mart,
    BottomNavItem.Activity, BottomNavItem.Profile
)

@Composable
fun VitoNavHost() {
    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    Scaffold(
        bottomBar = {
            NavigationBar {
                clientNavItems.forEach { item ->
                    NavigationBarItem(
                        selected = currentRoute == item.route,
                        onClick = {
                            if (currentRoute != item.route) {
                                navController.navigate(item.route) {
                                    popUpTo(navController.graph.startDestinationId) {
                                        saveState = true
                                    }
                                    launchSingleTop = true
                                    restoreState = true
                                }
                            }
                        },
                        icon = {
                            Icon(
                                imageVector = if (currentRoute == item.route) item.selectedIcon else item.unselectedIcon,
                                contentDescription = item.title
                            )
                        },
                        label = { Text(item.title) }
                    )
                }
            }
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = BottomNavItem.Ride.route,
            modifier = Modifier.padding(innerPadding)
        ) {
            composable(BottomNavItem.Ride.route) { VitoCarScreen(viewModel = hiltViewModel()) }
            composable(BottomNavItem.Send.route) { SendScreen() }
            composable(BottomNavItem.Mart.route) { MartScreen() }
            composable(BottomNavItem.Activity.route) { ActivityScreen() }
            composable(BottomNavItem.Profile.route) { ProfileScreen(viewModel = hiltViewModel()) }
        }
    }
}
