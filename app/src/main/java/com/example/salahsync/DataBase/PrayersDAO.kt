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
    // ---------------- Insert & Update ----------------
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPrayer(prayer: PrayerEntity) // ✅ Insert new prayer
    @Update
    suspend fun updatePrayer(prayer: PrayerEntity) // ✅ Update existing prayer
    // ---------------- Query by Date ----------------
    @Query("SELECT * FROM prayers WHERE date = :date")
    suspend fun getPrayersByDate(date: String): List<PrayerEntity> // ✅ Get all prayers for a given date
    // ---------------- Count Queries (All Time) ----------------
    @Query("SELECT COUNT(*) FROM prayers WHERE name = :prayerName AND statusRes = :statusRes") // (NEW)
    suspend fun getPrayerStatusCount(prayerName: String, statusRes: Int): Int
    // 🔹 Why: Needed for per-prayer breakdown (Fajr/Dhuhr/…)
    @Query("SELECT COUNT(*) FROM prayers WHERE statusRes = :statusRes") // (NEW)
    suspend fun getTotalStatusCount(statusRes: Int): Int
    // 🔹 Why: Needed for global counts (Not Prayed, On Time, etc.)
    @Query("SELECT COUNT(*) FROM prayers") // (NEW)
    suspend fun getTotalPrayers(): Int
    // 🔹 Why: Needed for total count of all records
    // ---------------- Count Queries (Date Range) ----------------
    @Query("SELECT COUNT(*) FROM prayers WHERE name = :prayerName AND statusRes = :statusRes AND date BETWEEN :startDate AND :endDate") // (NEW)
    suspend fun getPrayerStatusCountByDateRange(
        prayerName: String,
        statusRes: Int,
        startDate: String,
        endDate: String
    ): Int
    // 🔹 Why: Needed for weekly/monthly/yearly breakdown
    @Query("SELECT COUNT(*) FROM prayers WHERE statusRes = :statusRes AND date BETWEEN :startDate AND :endDate") // (NEW)
    suspend fun getStatusCountByDateRange(
        statusRes: Int,
        startDate: String,
        endDate: String
    ): Int
    // 🔹 Why: Needed for global stats by period
    @Query("SELECT COUNT(*) FROM prayers WHERE date BETWEEN :startDate AND :endDate") // (NEW)
    suspend fun getTotalPrayersByDateRange(startDate: String, endDate: String): Int
    // 🔹 Why: Needed for total count by period
    // ADDED: Query to get all prayers // Why: Needed for syncLocalToCloud in repository
    @Query("SELECT * FROM prayers")
    suspend fun getAllPrayers(): List<PrayerEntity>
}