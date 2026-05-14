package com.vito.client.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.vito.client.BuildConfig
import com.vito.client.screens.home.ClientHomeScreen
import com.vito.client.screens.auth.LoginScreen
import com.vito.client.viewmodel.AuthViewModel

sealed class Screen(val route: String) {
    object Login : Screen("login")
    object ClientHome : Screen("home")
}

@Composable
fun VitoNavHost(
    authViewModel: AuthViewModel = hiltViewModel()
) {
    val navController = rememberNavController()
    val authState by authViewModel.authState.collectAsState()
    val isDemoMode = BuildConfig.DEMO_MODE

    // Auto-login in demo mode
    LaunchedEffect(isDemoMode) {
        if (isDemoMode && !authState.isLoggedIn) {
            authViewModel.loginWithDemo()
        }
    }

    // Navigate based on login state or demo mode
    LaunchedEffect(authState.isLoggedIn, isDemoMode) {
        if (isDemoMode || authState.isLoggedIn) {
            navController.navigate(Screen.ClientHome.route) {
                popUpTo(Screen.Login.route) { inclusive = true }
            }
        }
    }

    NavHost(
        navController = navController,
        startDestination = if (isDemoMode) Screen.ClientHome.route else Screen.Login.route
    ) {
        composable(Screen.Login.route) {
            LoginScreen(
                onLoginSuccess = {
                    navController.navigate(Screen.ClientHome.route) {
                        popUpTo(Screen.Login.route) { inclusive = true }
                    }
                }
            )
        }
        composable(Screen.ClientHome.route) {
            ClientHomeScreen(
                onLogout = {
                    authViewModel.logout()
                    navController.navigate(Screen.Login.route) {
                        popUpTo(Screen.ClientHome.route) { inclusive = true }
                    }
                }
            )
        }
    }
}