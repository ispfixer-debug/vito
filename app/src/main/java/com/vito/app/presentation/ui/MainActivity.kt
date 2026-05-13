package com.vito.app.presentation.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import com.vito.app.presentation.ui.navigation.VitoNavHost
import com.vito.app.presentation.ui.screens.login.LoginScreen
import com.vito.app.presentation.ui.screens.login.LoginViewModel
import com.vito.app.presentation.ui.theme.VitoTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        
        setContent {
            VitoTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    VitoApp()
                }
            }
        }
    }
}

@Composable
fun VitoApp() {
    val loginViewModel: LoginViewModel = hiltViewModel()
    val isLoggedIn by loginViewModel.isLoggedIn.collectAsState()
    
    if (isLoggedIn) {
        VitoNavHost()
    } else {
        LoginScreen(viewModel = loginViewModel)
    }
}