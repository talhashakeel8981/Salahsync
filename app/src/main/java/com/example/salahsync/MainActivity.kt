package com.example.salahsync
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.RequiresApi
import com.example.salahsync.ui.Screens.TopBottom
import androidx.lifecycle.ViewModelProvider
import androidx.room.Room
import com.example.salahsync.DataBase.AppDatabase
import com.example.salahsync.ui.Screens.PrayerScreenViewModel
import com.example.salahsync.ui.Screens.PrayerViewModelFactory

class MainActivity : ComponentActivity() {
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val db = Room.databaseBuilder(
            applicationContext,
            AppDatabase::class.java,
            "prayer_db"
        ).build()
        val dao = db.prayerDao()
        val factory = PrayerViewModelFactory(dao)
        val viewModel = ViewModelProvider(this, factory)[PrayerScreenViewModel::class.java]

        setContent {
            TopBottom(viewModel)  // ✅ only pass ViewModel, don't call DAO methods here
        }
    }
}