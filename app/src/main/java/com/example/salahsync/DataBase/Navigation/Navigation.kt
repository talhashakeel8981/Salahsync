package com.example.salahsync.DataBase.Navigation

import android.content.Context
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.salahsync.DataBase.GenderDao
import com.example.salahsync.ui.Screens.OnBoarding.GenderSelectionScreen
import com.example.salahsync.ui.Screens.OnBoarding.WelcomeScreen
import com.example.salahsync.ui.Screens.PrayerScreenViewModel
import com.example.salahsync.ui.Screens.SettingsOptions.DataBackup.AuthViewModel
import com.example.salahsync.ui.Screens.SettingsOptions.DataBackup.DataBackupScreen
import com.example.salahsync.ui.Screens.TopBottom

import com.example.salahsync.ui.Screens.TopBottom
// ADDED: Import PrayerRepository // Why: To pass to screens and ViewModels
import com.example.salahsync.ui.Screens.SettingsOptions.DataBackup.PrayerRepository
// ADDED: Import AuthViewModelFactory // Why: To create AuthViewModel with repository
import com.example.salahsync.ui.Screens.SettingsOptions.DataBackup.AuthViewModelFactory
import com.example.salahsync.ui.Screens.SettingsOptions.SettingsNavHost
import com.example.salahsync.ui.Screens.SettingsOptions.SettingsNavHost  // ADDED
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun AppNavigation(
    prayerViewModel: PrayerScreenViewModel,
    genderDao: GenderDao,
    navigateTo: String,
    repository: PrayerRepository
) {
    val context = LocalContext.current
    val sharedPreferences = context.getSharedPreferences("app_prefs", Context.MODE_PRIVATE)
    val onboardingDone = sharedPreferences.getBoolean("onboarding_done", false)
    val navController = rememberNavController()
    val authViewModel: AuthViewModel = viewModel(factory = AuthViewModelFactory(repository))

    NavHost(
        navController = navController,
        startDestination = if (onboardingDone) "topbottom" else "welcome"
    ) {
        composable("welcome") {
            WelcomeScreen(navController = navController)
        }
        composable("gender_selection") {
            GenderSelectionScreen(
                navController = navController,
                repository = repository
            )
        }
        composable("topbottom") {
            TopBottom(
                viewModel = prayerViewModel,
                startDestination = if (navigateTo == "prayer") "prayerScreen" else "prayer",
                navController = navController
            )
        }
        composable("data_backup") {
            DataBackupScreen(
                onBack = { navController.popBackStack() },
                viewModel = authViewModel
            )
        }

        // ADDED: Settings route — THIS FIXES THE CRASH
        composable("settings") {
            SettingsNavHost(
                navController = navController,
                autoOpenNotifications = false  // Will be controlled from TopBottom
            )
        }
    }
}