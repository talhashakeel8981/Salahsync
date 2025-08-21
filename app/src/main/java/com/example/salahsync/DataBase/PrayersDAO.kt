package com.example.salahsync.DataBase

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.salahsync.ui.Screens.Prayer
import com.example.salahsync.ui.Space.User
import java.time.LocalDate


@Dao
interface PrayerDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)   //// whats this pnConflictStrategy
    suspend fun insertPrayer(prayer: Prayer)

    @Query("SELECT * FROM Prayers WHERE date = :date")
    suspend fun getPrayersByDate(date: LocalDate): List<Prayer>
}