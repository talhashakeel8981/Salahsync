package com.example.salahsync.ui.Screens.SettingsOptions

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.salahsync.ui.Screens.Setting.SettingScreen


@Composable
fun NavGraph(navController: NavHostController) {
    NavHost(navController, startDestination = "settings") {
        composable("settings") { SettingScreen(navController) }
        composable("notifications") { NotificationScreen() }
        composable("manage_deeds") { ManageDeedsScreen() }
        composable("appearance") { AppearenceScreen() }
        composable("haptic_feedbacks") { HepticFeedBackScreen() }
        composable("privacy_policy") { PrivacyPolicyScreen() }
    }
}


