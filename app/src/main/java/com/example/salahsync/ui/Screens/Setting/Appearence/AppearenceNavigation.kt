package com.example.salahsync.ui.Screens.Setting.Appearence

import androidx.compose.runtime.Composable
import com.example.salahsync.ui.Screens.PrayerScreenViewModel
import com.example.salahsync.ui.Screens.SettingsOptions.AppearenceScreen

@Composable
fun AppearenceNavigation(viewModel: PrayerScreenViewModel, onBack: () -> Unit) {
    ThemeControl(viewModel = viewModel) { theme, onThemeChange ->
        AppTheme(themeMode = theme) {
            AppearenceScreen(
                selectedTheme = theme,
                onThemeChange = onThemeChange,
                onBack = onBack
            )
        }
    }
}