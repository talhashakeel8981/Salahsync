package com.example.salahsync.ui.Screens.Setting.Appearence

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext

import androidx.compose.runtime.*
import androidx.compose.ui.platform.LocalContext
import com.example.salahsync.ui.Screens.PrayerScreenViewModel
import com.example.salahsync.ui.Screens.TopBottom

@Composable
fun ThemeControl(
    viewModel: PrayerScreenViewModel,
    content: @Composable (selectedTheme: Themes, onThemeChange: (Themes) -> Unit) -> Unit
) {
    val context = LocalContext.current
    val themeManager = remember { ThemeManager(context) }
    val selectedTheme by remember { mutableStateOf(themeManager.getTheme()) }

    content(selectedTheme) { newTheme ->
        themeManager.saveTheme(newTheme)
    }
}