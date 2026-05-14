package com.vito.driver.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.vito.driver.BuildConfig
import com.vito.driver.screens.DriverHomeScreen
import com.vito.driver.screens.DriverLoginScreen
import com.vito.driver.viewmodel.DriverAuthViewModel

sealed class DriverScreen(val route: String) {
    object Login : DriverScreen("login")
    object Home : DriverScreen("home")
}

@Composable
fun DriverNavHost(
    authViewModel: DriverAuthViewModel
) {
    val navController = rememberNavController()
    val authState by authViewModel.authState.collectAsState()
    val isDemoMode = BuildConfig.DEMO_MODE

    LaunchedEffect(isDemoMode) {
        if (isDemoMode && !authState.isLoggedIn) {
            authViewModel.loginWithDemo()
        }
    }

    LaunchedEffect(authState.isLoggedIn, isDemoMode) {
        if (isDemoMode || authState.isLoggedIn) {
            navController.navigate(DriverScreen.Home.route) {
                popUpTo(DriverScreen.Login.route) { inclusive = true }
            }
        }
    }

    NavHost(
        navController = navController,
        startDestination = if (isDemoMode) DriverScreen.Home.route else DriverScreen.Login.route
    ) {
        composable(DriverScreen.Login.route) {
            DriverLoginScreen(
                onLoginSuccess = {
                    navController.navigate(DriverScreen.Home.route) {
                        popUpTo(DriverScreen.Login.route) { inclusive = true }
                    }
                }
            )
        }
        composable(DriverScreen.Home.route) {
            DriverHomeScreen(
                onLogout = {
                    authViewModel.logout()
                    navController.navigate(DriverScreen.Login.route) {
                        popUpTo(DriverScreen.Home.route) { inclusive = true }
                    }
                }
            )
        }
    }
}