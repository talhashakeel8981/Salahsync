package com.example.salahsync.ui.Screens.SettingsOptions

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.salahsync.ui.Screens.Setting.Notification.NotificationScreen
import com.example.salahsync.ui.Screens.Setting.SettingScreen
import androidx.compose.runtime.getValue

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun SettingsNavHost(
    navController: NavController,
    autoOpenNotifications: Boolean = false
) {
    val settingsNavController = rememberNavController()

    val currentRoute by settingsNavController.currentBackStackEntryAsState()
    val isOnSettingsRoute = currentRoute?.destination?.route == "settings_main"

    LaunchedEffect(isOnSettingsRoute, autoOpenNotifications) {
        if (isOnSettingsRoute && autoOpenNotifications) {
            settingsNavController.navigate("notifications") {
                popUpTo("settings_main") { inclusive = true }
                launchSingleTop = true
            }
        }
    }

    NavHost(
        navController = settingsNavController,
        startDestination = "settings_main"
    ) {
        composable("settings_main") {
            if (!autoOpenNotifications) {
                SettingScreen(
                    onNotificationsClick = { settingsNavController.navigate("notifications") },
                    onManageDeedsClick = { settingsNavController.navigate("manage_deeds") },
                    appearence = { settingsNavController.navigate("appearence") },
                    onHapticClick = { settingsNavController.navigate("haptic_feedbacks") },
                    onPrivacyPolicyClick = { settingsNavController.navigate("privacy") },
                    feedback = { settingsNavController.navigate("feedbacks") },
                    databackup = { navController.navigate("data_backup") },
                    tracking = { settingsNavController.navigate("trackings") },
                    invite = { settingsNavController.navigate("invite") },
                    emailfeedback = { settingsNavController.navigate("emailfeedback") },
                    rateus = { settingsNavController.navigate("rateus") }
                )
            }
        }
        composable("feedbacks") { FeedBackScreen(onBack = { settingsNavController.popBackStack() }) }
        composable("notifications") {
            NotificationScreen(onBack = { settingsNavController.popBackStack() })
        }
        composable("manage_deeds") { ManageDeedsScreen(onBack = { settingsNavController.popBackStack() }) }
        composable("invite") { InviteFriendsScreen(onBack = { settingsNavController.popBackStack() }) }
        composable("privacy") { PrivacyPolicyScreen(onBack = { settingsNavController.popBackStack() }) }
        composable("trackings") { TrackingReasonScreen(onBack = { settingsNavController.popBackStack() }) }
        composable("emailfeedback") { EmailFeedback(onBack = { settingsNavController.popBackStack() }) }
        composable("appearence") { AppearanceScreen(onBack = { settingsNavController.popBackStack() }) }
        composable("rateus") { LoveSalahsync(onBack = { settingsNavController.popBackStack() }) }
    }
}