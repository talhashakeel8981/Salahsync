package com.example.salahsync.DataBase

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.salahsync.ui.Screens.Prayer
//import com.example.salahsync.ui.Space.User
//import com.example.salahsync.ui.Space.UserDao

import com.example.salahsync.DataBase.PrayerDao
import com.example.salahsync.DataBase.PrayerEntity // 🛠️ CHANGED: Import PrayerEntity instead of Prayer





// ⚡ Include all entities here and bump version when adding new tables
@Database(
    entities = [PrayerEntity::class, Gender::class],
    version = 3, // ← incremented to 2 because we added Gender
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun prayerDao(): PrayerDao
    abstract fun genderDao(): GenderDao
}