package com.example.salahsync.ui.Screens.SettingsOptions

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.salahsync.ui.Screens.PrayerScreenViewModel
import com.example.salahsync.ui.Screens.Setting.Appearence.ThemeControl
import com.example.salahsync.ui.Screens.Setting.SettingScreen
import com.example.salahsync.ui.Screens.Setting.SettingScreen
import androidx.lifecycle.viewmodel.compose.viewModel

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun SettingsNavHost(viewModel: PrayerScreenViewModel = viewModel()) {
    val settingsNavController = rememberNavController()

    NavHost(
        navController = settingsNavController,
        startDestination = "settings_main"
    ) {
        composable("settings_main") {
            SettingScreen(
                onNotificationsClick = { settingsNavController.navigate("notifications") },
                onManageDeedsClick = { settingsNavController.navigate("manage_deeds") },
                appearence = { settingsNavController.navigate("appearance") },
                onHapticClick = { settingsNavController.navigate("haptic_feedbacks") },
                onPrivacyPolicyClick = { settingsNavController.navigate("privacy") },
                feedback = { settingsNavController.navigate("feedbacks") },
                databackup = { settingsNavController.navigate("databackup") },
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
        composable("databackup") { DataBackupScreen(onBack = { settingsNavController.popBackStack() }) }
        composable("appearance") {
            ThemeControl(viewModel = viewModel) { selectedTheme, onThemeChange ->
                AppearenceScreen(
                    selectedTheme = selectedTheme,
                    onThemeChange = onThemeChange,
                    onBack = { settingsNavController.popBackStack() }
                )
            }
        }
        composable("rateus") { LoveSalahsync(onBack = { settingsNavController.popBackStack() }) }
    }
}