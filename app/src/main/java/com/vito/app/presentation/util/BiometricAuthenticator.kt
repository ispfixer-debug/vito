package com.vito.app.presentation.util

import android.content.Context
import androidx.biometric.BiometricManager
import androidx.biometric.BiometricPrompt
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentActivity
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException

class BiometricAuthenticator(private val context: Context) {
    
    fun canAuthenticate(): Boolean {
        val biometricManager = BiometricManager.from(context)
        return when (biometricManager.canAuthenticate(BiometricManager.Authenticators.BIOMETRIC_STRONG or BiometricManager.Authenticators.BIOMETRIC_WEAK)) {
            BiometricManager.BIOMETRIC_SUCCESS -> true
            else -> false
        }
    }
    
    suspend fun authenticate(
        title: String = "Authenticate",
        subtitle: String = "Verify your identity",
        negativeButtonText: String = "Cancel",
        onSuccess: () -> Unit,
        onError: () -> Unit
    ): Boolean = suspendCancellableCoroutine { continuation ->
        val activity = context as? FragmentActivity
            ?: run { continuation.resumeWithException(IllegalStateException("Context must be FragmentActivity")); return@suspendCancellableCoroutine }
        
        val executor = ContextCompat.getMainExecutor(context)
        
        val callback = object : BiometricPrompt.AuthenticationCallback() {
            override fun onAuthenticationSucceeded(result: BiometricPrompt.AuthenticationResult) {
                onSuccess()
                if (continuation.isActive) continuation.resume(true)
            }
            
            override fun onAuthenticationError(errorCode: Int, errString: CharSequence) {
                onError()
                if (continuation.isActive) continuation.resume(false)
            }
            
            override fun onAuthenticationFailed() {
                // Do nothing, wait for retry
            }
        }
        
        val biometricPrompt = BiometricPrompt(activity, executor, callback)
        
        val promptInfo = BiometricPrompt.PromptInfo.Builder()
            .setTitle(title)
            .setSubtitle(subtitle)
            .setNegativeButtonText(negativeButtonText)
            .setAllowedAuthenticators(BiometricManager.Authenticators.BIOMETRIC_STRONG or BiometricManager.Authenticators.BIOMETRIC_WEAK)
            .build()
        
        biometricPrompt.authenticate(promptInfo)
        
        continuation.invokeOnCancellation {
            biometricPrompt.cancelAuthentication()
        }
    }
}
