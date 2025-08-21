package com.example.salahsync.DataBase

import android.content.Context
import androidx.room.Room
import com.example.salahsync.ui.Space.AppDatabase

object DataBaseProvider {


    @Volatile
    private var INSTANCE: AppDatabase? = null

    fun getDatabase(context: Context): AppDatabase {
        return INSTANCE ?: synchronized(this) {
            val instance = Room.databaseBuilder(
                context.applicationContext,
                AppDatabase::class.java,
                "Prayers_db"
            ).build()
            INSTANCE = instance
            instance
        }
    }
}
