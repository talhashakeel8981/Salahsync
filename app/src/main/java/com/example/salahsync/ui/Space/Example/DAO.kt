package com.example.salahsync.ui.Space.Example

import androidx.room.Dao
import androidx.room.Insert

@Dao{
    interface naaamDoa{
        @Insert
        suspend fun insertnaam()
    }
}