package com.vito.app.presentation.ui.theme

import android.app.Activity
import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

private val LightColorScheme = lightColorScheme(
    primary = VitoPrimary,
    onPrimary = VitoOnPrimary,
    primaryContainer = VitoPrimaryContainer,
    onPrimaryContainer = VitoOnPrimaryContainer,
    secondary = VitoSecondary,
    onSecondary = VitoOnSecondary,
    secondaryContainer = VitoSecondaryContainer,
    onSecondaryContainer = VitoOnSecondaryContainer,
    tertiary = VitoTertiary,
    onTertiary = VitoOnTertiary,
    tertiaryContainer = VitoTertiaryContainer,
    onTertiaryContainer = VitoOnTertiaryContainer,
    error = VitoError,
    onError = VitoOnError,
    errorContainer = VitoErrorContainer,
    onErrorContainer = VitoOnErrorContainer,
    background = VitoBackground,
    onBackground = VitoOnBackground,
    surface = VitoSurface,
    onSurface = VitoOnSurface,
    surfaceVariant = VitoSurfaceVariant,
    onSurfaceVariant = VitoOnSurfaceVariant,
    outline = VitoOutline,
    outlineVariant = VitoOutlineVariant,
    scrim = VitoScrim
)

private val DarkColorScheme = darkColorScheme(
    primary = VitoPrimaryDark,
    onPrimary = VitoOnPrimaryDark,
    primaryContainer = VitoPrimaryContainerDark,
    onPrimaryContainer = VitoOnPrimaryContainerDark,
    secondary = VitoSecondaryDark,
    onSecondary = VitoOnSecondaryDark,
    secondaryContainer = VitoSecondaryContainerDark,
    onSecondaryContainer = VitoOnSecondaryContainerDark,
    tertiary = VitoTertiary,
    onTertiary = VitoOnTertiary,
    tertiaryContainer = VitoTertiaryContainer,
    onTertiaryContainer = VitoOnTertiaryContainer,
    error = VitoError,
    onError = VitoOnError,
    errorContainer = VitoErrorContainer,
    onErrorContainer = VitoOnErrorContainer,
    background = VitoBackgroundDark,
    onBackground = VitoOnBackgroundDark,
    surface = VitoSurfaceDark,
    onSurface = VitoOnSurfaceDark,
    surfaceVariant = VitoSurfaceVariantDark,
    onSurfaceVariant = VitoOnSurfaceVariantDark,
    outline = VitoOutline,
    outlineVariant = VitoOutlineVariant,
    scrim = VitoScrim
)

@Composable
fun VitoTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = true,
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }
        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            window.statusBarColor = colorScheme.primary.toArgb()
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = !darkTheme
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = VitoTypography,
        shapes = VitoShapes,
        content = content
    )
}
