package com.example.salahsync.DataBase

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update

@Dao
interface GenderDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertGender(gender: Gender) // ✅ Save gender choice

    @Query("SELECT * FROM gender LIMIT 1") // (NEW)
    suspend fun getGender(): Gender?
    // 🔹 Why: PrayerScreenViewModel uses this in init{} to load gender

    @Update
    suspend fun updateGender(gender: Gender) // (NEW)
    // 🔹 Why: Update gender if user changes later

    @Query("DELETE FROM gender") // (NEW)
    suspend fun clearGender()
    // 🔹 Why: Optional, reset if user re-selects
}