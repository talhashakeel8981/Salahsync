package com.example.salahsync.DataBase

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.salahsync.ui.Screens.Prayer
import com.example.salahsync.DataBase.PrayerDao
import java.time.LocalDate


@Dao
interface PrayerDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPrayer(prayer: PrayerEntity) // 🛠️ CHANGED: Use PrayerEntity instead of Prayer

    @Query("SELECT * FROM Prayers WHERE date = :date")
    suspend fun getPrayersByDate(date: String): List<PrayerEntity> // 🛠️ CHANGED: Use String for date and return List<PrayerEntity>
}