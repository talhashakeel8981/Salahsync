package com.example.salahsync
import android.animation.ObjectAnimator
import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.animation.AnticipateInterpolator
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.RequiresApi
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.core.animation.doOnEnd
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.ViewModelProvider
import androidx.room.Room
import com.example.salahsync.DataBase.AppDatabase
import com.example.salahsync.ui.Screens.PrayerScreenViewModel
import com.example.salahsync.ui.Screens.PrayerViewModelFactory
//import com.example.salahsync.ui.Screens.Setting.Appearence.AppTheme
//import com.example.salahsync.ui.Screens.Setting.Appearence.
//import com.example.salahsync.ui.Screens.Setting.Appearence.ThemeControl


//import com.example.salahsync.ui.Screens.FirebaseSyncCheck
//import com.example.salahsync.ui.Screens.testFirebase
import com.example.salahsync.ui.theme.SalahSyncTheme


import com.example.salahsync.DataBase.Navigation.AppNavigation


//import com.example.salahsync.ui.Screens.Setting.Appearence.AppTheme
//import com.example.salahsync.ui.Screens.Setting.Appearence.
//import com.example.salahsync.ui.Screens.Setting.Appearence.ThemeControl

// ADDED: Import PrayerRepository // Why: To create and pass instance
import com.example.salahsync.ui.Screens.SettingsOptions.DataBackup.PrayerRepository
import kotlinx.coroutines.delay

class MainActivity : ComponentActivity() {
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        // Install SplashScreen API
        val splashScreen = installSplashScreen()

        // Optional: Keep splash longer (simulate loading)
        var isReady = false
        splashScreen.setKeepOnScreenCondition { !isReady }
        super.onCreate(savedInstanceState)

        // Compose state to track when the app is ready
        // Use MutableState directly, not 'by' delegation
        val isAppReady = mutableStateOf(false)
        // Keep splash screen on-screen until isAppReady = true
        splashScreen.setKeepOnScreenCondition { !isAppReady.value }

        // Optional: Custom exit animation
        splashScreen.setOnExitAnimationListener { splashScreenViewProvider ->
            val iconView = splashScreenViewProvider.iconView

            val slideUp = ObjectAnimator.ofFloat(
                iconView,
                "translationY",
                0f,
                -100f // slide up by 100 pixels
            )
            slideUp.interpolator = AnticipateInterpolator()
            slideUp.duration = 200L
            slideUp.doOnEnd { splashScreenViewProvider.remove() }
            slideUp.start()
        }

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
            // Simulate loading (2 seconds) — replace with real initialization if needed
            LaunchedEffect(Unit) {
                delay(2000)
                isAppReady.value = true // update state
            }
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
