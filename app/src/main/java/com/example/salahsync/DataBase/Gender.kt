package com.example.salahsync.DataBase
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "Gender")
data class Gender(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val genderName: String // e.g., Male, Female, Other
)