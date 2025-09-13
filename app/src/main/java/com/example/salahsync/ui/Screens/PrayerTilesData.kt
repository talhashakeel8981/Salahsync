package com.example.salahsync.ui.Screens



import com.example.salahsync.R

// 🛠️ CHANGED: Removed redundant Prayer data class, standardized on PrayerTilesData
data class PrayerTilesData(
    val name: String,
    val iconRes: Int,
    var selected: Boolean = false
)
val prayer = listOf(
    PrayerTilesData("Fajr", R.drawable.ic_fajr),
    PrayerTilesData("Dhuhr", R.drawable.ic_dhuhur),
    PrayerTilesData("Asr", R.drawable.ic_asr),
    PrayerTilesData("Maghrib", R.drawable.ic_maghrib),
    PrayerTilesData("Isha", R.drawable.ic_esha)
)

