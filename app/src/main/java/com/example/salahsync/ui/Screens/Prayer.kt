package com.example.salahsync.ui.Screens

import com.example.salahsync.R

data class Prayer(
    val name: String,
    val iconRes: Int // Resource ID for the icon
)
val prayers = listOf(
    PrayerTilesData("Fajr", R.drawable.ic_fajr, R.drawable.bg_fajr),
    PrayerTilesData("Dhuhr", R.drawable.ic_dhuhur, R.drawable.bg_zohr),
    PrayerTilesData("Asr", R.drawable.ic_asr, R.drawable.bg_asr),
    PrayerTilesData("Maghrib", R.drawable.ic_maghrib, R.drawable.bg_fajr),
    PrayerTilesData("Isha", R.drawable.ic_esha, R.drawable.bg_esha)
)