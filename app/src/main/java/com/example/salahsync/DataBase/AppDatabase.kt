package com.example.salahsync.DataBase

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.salahsync.ui.Screens.Prayer
//import com.example.salahsync.ui.Space.User
//import com.example.salahsync.ui.Space.UserDao

import com.example.salahsync.DataBase.PrayerDao
import com.example.salahsync.DataBase.PrayerEntity // 🛠️ CHANGED: Import PrayerEntity instead of Prayer



@Database(entities = [PrayerEntity::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun prayerDao(): PrayerDao // 🛠️ CHANGED: Ensure method is correctly defined
}