package com.example.salahsync.ui.theme

import android.app.Activity
import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.*

import androidx.compose.ui.graphics.Color

// 🌙 Dark mode colors
private val DarkColors = darkColorScheme(
    primary = Color(0xFF90CAF9),     // Light Blue
    secondary = Color(0xFF80CBC4),   // Teal
    tertiary = Color(0xFFCE93D8),    // Purple
    background = Color(0xFF121212),  // Pure dark background
    surface = Color(0xFF1E1E1E),     // Slightly lighter surface
    onPrimary = Color.Black,
    onSecondary = Color.Black,
    onTertiary = Color.Black,
    onBackground = Color.White,
    onSurface = Color.White
)

// ☀️ Light mode colors
private val LightColors = lightColorScheme(
    primary = Color(0xFF1976D2),     // Blue
    secondary = Color(0xFF009688),   // Teal
    tertiary = Color(0xFF9C27B0),    // Purple
    background = Color(0xFFFFFFFF),  // White
    surface = Color(0xFFF5F5F5),     // Light grey
    onPrimary = Color.White,
    onSecondary = Color.White,
    onTertiary = Color.White,
    onBackground = Color.Black,
    onSurface = Color.Black
)

@Composable
fun SalahSyncTheme(
    darkTheme: Boolean = isSystemInDarkTheme(), // 🔄 Follows system setting
    content: @Composable () -> Unit
) {
    val colors = if (darkTheme) DarkColors else LightColors

    MaterialTheme(
        colorScheme = colors,
        typography = Typography,
        content = content
    )
}