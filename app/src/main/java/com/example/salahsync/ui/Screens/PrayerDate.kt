package com.example.salahsync.ui.Screens

data class Prayer(
    val name: String,
    val time: String
)
val prayers = listOf(
    Prayer("Fajr", "05:00 AM"),
    Prayer("Dhuhr", "12:30 PM"),
    Prayer("Asr", "03:45 PM"),
    Prayer("Maghrib", "06:30 PM"),
    Prayer("Isha", "08:00 PM")
)