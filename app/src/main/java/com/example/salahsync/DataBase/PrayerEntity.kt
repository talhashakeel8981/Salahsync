package com.example.salahsync.DataBase

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "Prayers")
data class PrayerEntity (
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,

    val name: String,        // Fajr, Dhuhr etc
    val iconRes: Int,        // prayer icon (R.drawable.ic_fajr etc)

    val date: String,        // 🟢 LocalDate ko string me store karenge (Room directly LocalDate ko handle nahi kar sakta without TypeConverter)
    val statusRes: Int       // 🟢 Prayer ka status icon (Not prayed, On time etc)
)