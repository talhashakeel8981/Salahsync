package com.example.salahsync.ui.Screens.Setting.Appearence

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable

@Composable
fun AppTheme(themeMode: Themes, content: @Composable () -> Unit) {
    val isDark = when (themeMode) {
        Themes.SYSTEM -> isSystemInDarkTheme()
        Themes.DARK -> true
        Themes.LIGHT -> false
    }

    MaterialTheme(
        colorScheme = if (isDark) darkColorScheme() else lightColorScheme(),
        content = content
    )
}
