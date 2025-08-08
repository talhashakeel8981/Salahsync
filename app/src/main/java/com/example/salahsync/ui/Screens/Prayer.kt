package com.example.salahsync.ui.Screens

import com.example.salahsync.R

data class Prayer(
    val name: String,
    val iconRes: Int // Resource ID for the icon
)
val prayers = listOf(
    PrayerTilesData("Fajr", R.drawable.ic_fajr),
    PrayerTilesData("Dhuhr", R.drawable.ic_fajr),
    PrayerTilesData("Asr", R.drawable.ic_fajr),
    PrayerTilesData("Maghrib", R.drawable.ic_fajr),
    PrayerTilesData("Isha", R.drawable.ic_fajr)
)