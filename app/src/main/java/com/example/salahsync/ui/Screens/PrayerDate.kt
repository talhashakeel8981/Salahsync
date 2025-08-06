package com.example.salahsync.ui.Screens

data class Prayer(
    val name: String,

)
val prayers = listOf(
    Prayer("Fajr"),
    Prayer("Dhuhr"),
    Prayer("Asr"),
    Prayer("Maghrib"),
    Prayer("Isha")
)