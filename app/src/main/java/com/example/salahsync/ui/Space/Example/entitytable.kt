package com.example.salahsync.ui.Space.Example

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
    (tableName = "tablekanaam")
data class meratable(
    @PrimaryKey(autoGenerate = true)
    val id:Int=0,
    val columnkanaamjismenvalueshowhogi:String
)