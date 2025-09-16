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
    primary = Color(0xFF0288D1),     // Vibrant blue for a striking primary
    secondary = Color(0xFF4CAF50),   // Green for a natural secondary
    tertiary = Color(0xFF4FC3F7),    // Bright cyan for accents
    background = Color(0xFF0D1B2A),  // Dark blue, as specified
    surface = Color(0xFF1B263B),     // Slightly lighter blue for surfaces
    onPrimary = Color.White,         // White for contrast on blue
    onSecondary = Color.White,       // White for contrast on green
    onTertiary = Color.Black,        // Black for contrast on bright cyan
    onBackground = Color(0xFFB3E5FC), // Light blue for text on background
    onSurface = Color(0xFFB3E5FC)    // Light blue for text on surface
)

// ☀️ Light mode colors
private val LightColors = lightColorScheme(
    primary = Color(0xFF0D47A1),     // Deep blue for a bold, vibrant primary
    secondary = Color(0xFF0277BD),   // Medium blue for a cohesive secondary
    tertiary = Color(0xFF42A5F5),    // Bright sky blue for striking accents
    background = Color(0xFFE1F5FE),  // Light blue background for blue cohesion
    surface = Color(0xFFE8F6FD),     // Very light blue for surfaces
    onPrimary = Color.White,         // White for contrast on deep blue
    onSecondary = Color.White,       // White for contrast on medium blue
    onTertiary = Color.White,        // White for contrast on sky blue
    onBackground = Color(0xFF0D1B2A), // Dark blue, as specified, for text
    onSurface = Color(0xFF0D1B2A)    // Dark blue, as specified, for surface text
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