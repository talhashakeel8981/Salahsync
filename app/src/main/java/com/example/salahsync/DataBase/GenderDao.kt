package com.example.salahsync.DataBase

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update

@Dao
interface GenderDao {
    @Insert
    suspend fun insert(gender: Gender)   // changed name ✅

    @Query("SELECT * FROM Gender LIMIT 1")
    suspend fun getGender(): Gender?
}