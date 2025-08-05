package com.example.salahsync.ui.Screens

data class Prayer(
    val name: String,
    val iconRes: Int
)
val prayers = listOf(
    Prayer("Fajr", android.R.drawable.ic_dialog_info), // Placeholder icon
    Prayer("Dhuhr", android.R.drawable.ic_dialog_info),
    Prayer("Asr", android.R.drawable.ic_dialog_info),
    Prayer("Maghrib", android.R.drawable.ic_dialog_info),
    Prayer("Isha", android.R.drawable.ic_dialog_info)
)