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


import com.example.salahsync.ui.Screens.FirebaseSyncCheck
import com.example.salahsync.ui.Screens.testFirebase
import com.example.salahsync.ui.theme.SalahSyncTheme

import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController


import com.example.salahsync.DataBase.Navigation.AppNavigation

import com.example.salahsync.ui.Screens.TopBottom
import com.example.salahsync.ui.Screens.OnBoarding.GenderSelectionScreen
import com.example.salahsync.ui.Screens.OnBoarding.WelcomeScreen
import com.example.salahsync.ui.theme.SalahSyncTheme


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
// ADDED: Import PrayerRepository // Why: To create and pass instance
import com.example.salahsync.ui.Screens.SettingsOptions.DataBackup.PrayerRepository
class MainActivity : ComponentActivity() {
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // ✅ Initialize Room database (removed allowMainThreadQueries)
        val db = Room.databaseBuilder(
            applicationContext,
            AppDatabase::class.java,
            "prayer_db"
        ).build()
        // ✅ Get DAOs
        val prayerDao = db.prayerDao()
        val genderDao = db.genderDao()
        // ADDED: Create repository instance // Why: To pass to ViewModel and navigation for data sync
        val repository = PrayerRepository(prayerDao, genderDao)
        // ✅ Create ViewModel using Factory
        // MODIFIED: Passed repository to factory // Why: ViewModel now depends on it
        val factory = PrayerViewModelFactory(repository)
        val prayerViewModel = ViewModelProvider(this, factory)[PrayerScreenViewModel::class.java]
        val navigateTo = intent.getStringExtra("navigateTo")
        // ✅ Set up Compose content with theme + navigation
        setContent {
            SalahSyncTheme {
                // MODIFIED: Passed repository to AppNavigation // Why: For use in screens and AuthViewModel
                AppNavigation(
                    prayerViewModel = prayerViewModel,
                    genderDao = genderDao,
                    navigateTo = navigateTo ?: "",
                    repository = repository
                )
            }
        }
    }
}
//class MainActivity : ComponentActivity() {
// override fun onCreate(savedInstanceState: Bundle?) {
// super.onCreate(savedInstanceState)
//
// setContent {
// FirebaseSyncCheck() // UI wali check
// }
//
// testFirebase() // Background me ek aur test
// }
//}