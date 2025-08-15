package com.example.salahsync.ui.Screens.SettingsOptions

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.salahsync.ui.Screens.Setting.SettingScreen


@Composable
fun SettingsNavHost() {
    val settingsNavController = rememberNavController()

    NavHost(
        navController = settingsNavController,
        startDestination = "settings_main"
    ) {
        composable("settings_main") {
            SettingScreen(
                onNotificationsClick = { settingsNavController.navigate("notifications") },
                onManageDeedsClick = { settingsNavController.navigate("manage_deeds") },
                onAppearanceClick = { settingsNavController.navigate("appearance") },
                onHapticClick = { settingsNavController.navigate("haptic_feedbacks") },
                onPrivacyPolicyClick = { settingsNavController.navigate("privacy") },
                feedback = {settingsNavController.navigate("feedbacks")},
                onDataBackupClick={settingsNavController.navigate("data")},
                tracking = {settingsNavController.navigate("trackings")},
                
        }
        composable("feedbacks"){FeedBackScreen(onBack = {settingsNavController.popBackStack()})}
        composable("notifications") { NotificationScreen(onBack = { settingsNavController.popBackStack() }) }
        composable("manage_deeds") { ManageDeedsScreen(onBack = { settingsNavController.popBackStack() }) }
        composable("invitefriends"){ InviteFriendsScreen (onBack = {settingsNavController.popBackStack()})}
        composable("privacy"){ PrivacyPolicyScreen (onBack = {settingsNavController.popBackStack()})}
        composable("trackings"){ TrackingReasonScreen (onBack = {settingsNavController.popBackStack()})},


//        composable("data"){ DataBackupScreen (onBack ={settingsNavController()})}
//        composable("")

//        composable("appearance") { AppearenceScreen(onBack = { settingsNavController.popBackStack() }) }
//        composable("haptic_feedbacks") { HepticFeedBackScreen(onBack = { settingsNavController.popBackStack() }) }
//        composable("privacy_policy") { PrivacyPolicyScreen(onBack = { settingsNavController.popBackStack() }) }
    }
}

