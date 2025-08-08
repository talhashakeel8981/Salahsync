package com.example.salahsync.ui.Screens

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.runtime.Composable

// ✅ Data class for prayers
data class PrayerTilesData(
    val name: String,
    val iconRes: Int, // Resource ID for the icon
    var selected: Boolean = false
)


