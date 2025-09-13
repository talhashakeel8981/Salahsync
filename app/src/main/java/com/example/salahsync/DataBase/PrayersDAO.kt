package com.example.salahsync.DataBase

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.salahsync.ui.Screens.Prayer
import com.example.salahsync.DataBase.PrayerDao
import java.time.LocalDate


import com.example.salahsync.DataBase.PrayerEntity // Updated import: Use PrayerEntity instead of Prayer for consistency

@Dao
interface PrayerDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPrayer(prayer: PrayerEntity)
    @Query("SELECT * FROM Prayers WHERE date = :date")
    suspend fun getPrayersByDate(date: String): List<PrayerEntity>
    @Query("SELECT COUNT(*) FROM Prayers WHERE statusRes = :statusRes AND date = :date")
    suspend fun getStatusCountByDate(statusRes: Int, date: String): Int
    @Query("SELECT COUNT(*) FROM Prayers WHERE statusRes = :statusRes")
    suspend fun getTotalStatusCount(statusRes: Int): Int
    @Query("SELECT COUNT(*) FROM Prayers")
    suspend fun getTotalPrayers(): Int
    @Update
    suspend fun updatePrayer(prayer: PrayerEntity)
    // CHANGED: Added for bar chart updates
    @Query("SELECT COUNT(*) FROM Prayers WHERE name = :prayerName AND statusRes != :notPrayedRes")
    suspend fun getPrayerPerformedCount(prayerName: String, notPrayedRes: Int): Int
    // CHANGED: Added new query to get count of a specific status for a specific prayer name across all dates.
    // This allows per-status per-prayer counts for the bar charts.
    // Before: No such query, leading to incorrect bar chart data using only performed counts.
    // After: Enables fetching exact counts for each status-prayer combination.
    @Query("SELECT COUNT(*) FROM Prayers WHERE name = :prayerName AND statusRes = :statusRes")
    suspend fun getPrayerStatusCount(prayerName: String, statusRes: Int): Int
    // In PrayerDao
    @Query("SELECT COUNT(*) FROM Prayers WHERE statusRes = :statusRes AND date BETWEEN :startDate AND :endDate")
    suspend fun getStatusCountByDateRange(statusRes: Int, startDate: String, endDate: String): Int
    @Query("SELECT COUNT(*) FROM Prayers WHERE name = :prayerName AND statusRes = :statusRes AND date BETWEEN :startDate AND :endDate")
    suspend fun getPrayerStatusCountByDateRange(prayerName: String, statusRes: Int, startDate: String, endDate: String): Int
    @Query("SELECT COUNT(*) FROM Prayers WHERE date BETWEEN :startDate AND :endDate")
    suspend fun getTotalPrayersByDateRange(startDate: String, endDate: String): Int
}