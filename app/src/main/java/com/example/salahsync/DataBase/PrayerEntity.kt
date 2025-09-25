package com.example.salahsync.DataBase

import androidx.room.Entity
import androidx.room.PrimaryKey


import androidx.room.Index

@Entity(
    tableName = "Prayers",
    // 🆕 ADDED: unique index on (date, name) so each prayer for a given date is only stored once
    indices = [Index(value = ["date", "name"], unique = true)]
)
data class PrayerEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val name: String, // Fajr, Dhuhr, etc.
    val iconRes: Int, // 🛠️ CHANGED: Clarified this stores the status icon (e.g., R.drawable.notprayed)
    val date: String, // LocalDate stored as String
    val statusRes: Int // Prayer status icon (Not prayed, On time, etc.)
)