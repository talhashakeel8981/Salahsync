package com.example.salahsync.ui.Screens



import com.example.salahsync.R

// 🛠️ CHANGED: Removed redundant Prayer data class, standardized on PrayerTilesData
data class PrayerTilesData(
    val name: String,
    val iconRes: Int,
    val backgroundRes: Int, // 🆕 Added background image resource
    var selected: Boolean = false
)

// ---------------------------
// 🧩 SAMPLE DATA (for preview or ViewModel initialization)
// ---------------------------

val prayer = listOf(
    PrayerTilesData("Fajr", R.drawable.ic_fajr, R.drawable.bg_fajr),
    PrayerTilesData("Dhuhr", R.drawable.ic_dhuhur, R.drawable.bg_zohr),
    PrayerTilesData("Asr", R.drawable.ic_asr, R.drawable.bg_asr),
    PrayerTilesData("Maghrib", R.drawable.ic_maghrib, R.drawable.bg_maghrib),
    PrayerTilesData("Isha", R.drawable.ic_esha, R.drawable.bg_esha)
)
