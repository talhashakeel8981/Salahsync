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
    suspend fun insertPrayer(prayer: PrayerEntity)

    @Query("SELECT * FROM Prayers WHERE date = :date")
    suspend fun getPrayersByDate(date: String): List<PrayerEntity>

    @Query("SELECT COUNT(*) FROM Prayers WHERE statusRes = :statusRes AND date = :date")
    suspend fun getStatusCountByDate(statusRes: Int, date: String): Int

    @Query("SELECT COUNT(*) FROM Prayers WHERE statusRes = :statusRes")
    suspend fun getTotalStatusCount(statusRes: Int): Int
}