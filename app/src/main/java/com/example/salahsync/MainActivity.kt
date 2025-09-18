package com.example.salahsync
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
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
import com.example.salahsync.ui.Screens.FirebaseSyncCheck
import com.example.salahsync.ui.Screens.testFirebase
import com.example.salahsync.ui.theme.SalahSyncTheme

import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController

import com.example.salahsync.DataBase.GenderDao
import com.example.salahsync.DataBase.Navigation.AppNavigation

import com.example.salahsync.ui.Screens.TopBottom
import com.example.salahsync.ui.Screens.OnBoarding.GenderSelectionScreen
import com.example.salahsync.ui.Screens.OnBoarding.WelcomeScreen
import com.example.salahsync.ui.theme.SalahSyncTheme

class MainActivity : ComponentActivity() {
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // ✅ Initialize Room database
        val db = Room.databaseBuilder(
            applicationContext,
            AppDatabase::class.java,
            "prayer_db"
        )
            .allowMainThreadQueries()
            .build()

        val prayerDao = db.prayerDao()
        val genderDao = db.genderDao()
        val factory = PrayerViewModelFactory(prayerDao)
        val viewModel = ViewModelProvider(this, factory)[PrayerScreenViewModel::class.java]

        val navigateTo = intent.getStringExtra("navigateTo")

        setContent {
            SalahSyncTheme {
                AppNavigation(
                    viewModel = viewModel,
                    genderDao = genderDao,
                    navigateTo = navigateTo ?: ""
                )
            }
        }
    }
}

//class MainActivity : ComponentActivity() {
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//
//        setContent {
//            FirebaseSyncCheck() // UI wali check
//        }
//
//        testFirebase() // Background me ek aur test
//    }
//}