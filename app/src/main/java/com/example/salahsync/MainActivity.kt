package com.example.salahsync
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import com.example.salahsync.ui.Screens.TopBottom
import androidx.lifecycle.ViewModelProvider
import androidx.room.Room
import com.example.salahsync.DataBase.AppDatabase
import com.example.salahsync.ui.Screens.PrayerScreenViewModel
import com.example.salahsync.ui.Screens.PrayerViewModelFactory
//import com.example.salahsync.ui.Screens.Setting.Appearence.AppTheme
//import com.example.salahsync.ui.Screens.Setting.Appearence.
//import com.example.salahsync.ui.Screens.Setting.Appearence.ThemeControl
import com.example.salahsync.ui.Screens.SettingsOptions.NotificationScreen
import com.example.salahsync.ui.Screens.SettingsOptions.SettingsNavHost

import androidx.compose.ui.Modifier
import com.example.salahsync.ui.theme.SalahSyncTheme

class MainActivity : ComponentActivity() {
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val db = Room.databaseBuilder(
            applicationContext,
            AppDatabase::class.java,
            "prayer_db"
        )
            .allowMainThreadQueries()
            .build()

        val dao = db.prayerDao()
        val factory = PrayerViewModelFactory(dao)
        val viewModel = ViewModelProvider(this, factory)[PrayerScreenViewModel::class.java]

        // Read flag from notification
        val navigateTo = intent.getStringExtra("navigateTo")

        setContent {
            SalahSyncTheme {   // App now supports Dark/Light mode
                TopBottom(
                    viewModel = viewModel,
                    startDestination = if (navigateTo == "prayer") "prayerScreen" else "home"
                )
            }
        }
    }
}