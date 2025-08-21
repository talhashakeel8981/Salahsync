package com.example.salahsync.DataBase

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.salahsync.ui.Screens.Prayer
import com.example.salahsync.ui.Space.User
import com.example.salahsync.ui.Space.UserDao


@Database(entities = [Prayer::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun prayerDao(): PrayerDao
}