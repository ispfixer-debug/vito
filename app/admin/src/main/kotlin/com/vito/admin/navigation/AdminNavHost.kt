package com.vito.admin.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.vito.admin.BuildConfig
import com.vito.admin.screens.AdminDashboardScreen
import com.vito.admin.screens.AdminLoginScreen
import com.vito.admin.viewmodel.AdminAuthViewModel

sealed class AdminScreen(val route: String) {
    object Login : AdminScreen("login")
    object Dashboard : AdminScreen("dashboard")
}

@Composable
fun AdminNavHost(
    authViewModel: AdminAuthViewModel
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
            navController.navigate(AdminScreen.Dashboard.route) {
                popUpTo(AdminScreen.Login.route) { inclusive = true }
            }
        }
    }

    NavHost(
        navController = navController,
        startDestination = if (isDemoMode) AdminScreen.Dashboard.route else AdminScreen.Login.route
    ) {
        composable(AdminScreen.Login.route) {
            AdminLoginScreen(
                onLoginSuccess = {
                    navController.navigate(AdminScreen.Dashboard.route) {
                        popUpTo(AdminScreen.Login.route) { inclusive = true }
                    }
                }
            )
        }
        composable(AdminScreen.Dashboard.route) {
            AdminDashboardScreen(
                onLogout = {
                    authViewModel.logout()
                    navController.navigate(AdminScreen.Login.route) {
                        popUpTo(AdminScreen.Dashboard.route) { inclusive = true }
                    }
                }
            )
        }
    }
}