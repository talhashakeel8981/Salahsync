package com.example.salahsync.DataBase
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "gender")
data class Gender(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    // 🔴 Before: e.g., Male, Female, Other
    // 🟢 After: Changed comment → matches actual saved values ("Man", "Woman")
    val genderName: String // e.g., Man, Woman
)