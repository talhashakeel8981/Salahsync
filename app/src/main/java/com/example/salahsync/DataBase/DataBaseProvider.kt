package com.example.salahsync.DataBase

import android.content.Context
import androidx.room.Room
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.salahsync.DataBase.AppDatabase
object DataBaseProvider {

    @Volatile
    private var INSTANCE: AppDatabase? = null

    // Migration from version 1 → 2 (adds Gender table)
    private val MIGRATION_1_2 = object : Migration(1, 2) {
        override fun migrate(database: SupportSQLiteDatabase) {
            database.execSQL(
                """
                CREATE TABLE IF NOT EXISTS Gender (
                    id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
                    genderName TEXT NOT NULL
                )
                """.trimIndent()
            )
        }
    }

    fun getDatabase(context: Context): AppDatabase {
        return INSTANCE ?: synchronized(this) {
            val instance = Room.databaseBuilder(
                context.applicationContext,
                AppDatabase::class.java,
                "Prayers_db"
            )
                .fallbackToDestructiveMigration() // ⚡ wipes old DB if schema mismatch
                .build()
            INSTANCE = instance
            instance
        }
    }
}