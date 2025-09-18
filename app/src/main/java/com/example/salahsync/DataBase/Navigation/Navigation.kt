package com.example.salahsync.DataBase.Navigation

import android.content.Context
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.salahsync.DataBase.GenderDao
import com.example.salahsync.ui.Screens.OnBoarding.GenderSelectionScreen
import com.example.salahsync.ui.Screens.OnBoarding.WelcomeScreen
import com.example.salahsync.ui.Screens.PrayerScreenViewModel
import com.example.salahsync.ui.Screens.TopBottom

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun AppNavigation(
    viewModel: PrayerScreenViewModel,
    genderDao: GenderDao,
    navigateTo: String
) {
    val context = LocalContext.current
    val sharedPreferences = context.getSharedPreferences("app_prefs", Context.MODE_PRIVATE)
    val onboardingDone = sharedPreferences.getBoolean("onboarding_done", false)

    val navController = rememberNavController()

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
                genderDao = genderDao
            )
        }
        composable("topbottom") {
            TopBottom(
                viewModel = viewModel,
                startDestination = if (navigateTo == "prayer") "prayerScreen" else "prayer"
            )
        }
    }
}