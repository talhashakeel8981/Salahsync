package com.example.salahsync.ui.Screens.SettingsOptions

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.salahsync.ui.Screens.Setting.SettingScreen
import com.example.salahsync.ui.Screens.SettingsOptions.DataBackup.DataBackupScreen


@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun SettingsNavHost(navController: NavController) { // Added top-level NavController
    val settingsNavController = rememberNavController()

    NavHost(
        navController = settingsNavController,
        startDestination = "settings_main"
    ) {
        composable("settings_main") {
            SettingScreen(
                onNotificationsClick = { settingsNavController.navigate("notifications") },
                onManageDeedsClick = { settingsNavController.navigate("manage_deeds") },
                appearence = { settingsNavController.navigate("appearence") },
                onHapticClick = { settingsNavController.navigate("haptic_feedbacks") },
                onPrivacyPolicyClick = { settingsNavController.navigate("privacy") },
                feedback = { settingsNavController.navigate("feedbacks") },
                databackup = { navController.navigate("data_backup") }, // Use top-level NavController
                tracking = { settingsNavController.navigate("trackings") },
                invite = { settingsNavController.navigate("invite") },
                emailfeedback = { settingsNavController.navigate("emailfeedback") },
                rateus = { settingsNavController.navigate("rateus") }
            )
        }
        composable("feedbacks") { FeedBackScreen(onBack = { settingsNavController.popBackStack() }) }
        composable("notifications") { NotificationScreen(onBack = { settingsNavController.popBackStack() }) }
        composable("manage_deeds") { ManageDeedsScreen(onBack = { settingsNavController.popBackStack() }) }
        composable("invite") { InviteFriendsScreen(onBack = { settingsNavController.popBackStack() }) }
        composable("privacy") { PrivacyPolicyScreen(onBack = { settingsNavController.popBackStack() }) }
        composable("trackings") { TrackingReasonScreen(onBack = { settingsNavController.popBackStack() }) }
        composable("emailfeedback") { EmailFeedback(onBack = { settingsNavController.popBackStack() }) }
        composable("appearence") { AppearanceScreen(onBack = { settingsNavController.popBackStack() }) }
        composable("rateus") { LoveSalahsync(onBack = { settingsNavController.popBackStack() }) }
        // Removed "databackup" route as it’s handled by the top-level NavHost
    }
}