/**
 * Vito Theme - Material 3 theming
 */
package com.beepbeep.vito.app.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

val VitoPrimary = Color(0xFF6366F1)
val VitoSecondary = Color(0xFF22C55E)
val VitoTertiary = Color(0xFFF59E0B)

private val LightColorScheme = lightColorScheme(
    primary = VitoPrimary,
    onPrimary = Color.White,
    secondary = VitoSecondary,
    tertiary = VitoTertiary,
    background = Color(0xFFF8FAFC),
    surface = Color.White,
    onBackground = Color(0xFF1E293B),
    onSurface = Color(0xFF1E293B)
)

private val DarkColorScheme = darkColorScheme(
    primary = VitoPrimary,
    onPrimary = Color.White,
    secondary = VitoSecondary,
    tertiary = VitoTertiary,
    background = Color(0xFF0F172A),
    surface = Color(0xFF1E293B),
    onBackground = Color.White,
    onSurface = Color(0xFFF1F5F9)
)

@Composable
fun VitoTheme(darkTheme: Boolean = false, content: @Composable () -> Unit) {
    MaterialTheme(colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme, content = content)
}