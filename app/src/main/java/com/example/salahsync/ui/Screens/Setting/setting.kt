package com.example.salahsync.ui.Screens.Setting

//import androidx.compose.foundation.R
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.salahsync.ui.Screens.SettingsOptions.SettingItem
import com.example.salahsync.ui.Screens.SettingsOptions.SettingSection
import com.example.salahsync.R



@Composable
fun SettingScreen(
    onNotificationsClick: () -> Unit,
    onManageDeedsClick: () -> Unit,
    onAppearanceClick: () -> Unit,
    onHapticClick: () -> Unit,
    onPrivacyPolicyClick: () -> Unit,
    onDataBackupClick:()->Unit,
    feedback:()->Unit,
    tracking:()->Unit
) {
    LazyColumn {
//        item { SettingSection("General") }
        item { SettingItem(icon = painterResource(R.drawable.notprayed), title = "Notifications", onClick = onNotificationsClick) }//notifications
        item { SettingItem(icon = painterResource(R.drawable.notprayed), title = "Manage deeds", onClick = onManageDeedsClick) }//Manage deeds
                                                                                                                                //DataBackup
                                                                                                                                //Appearence
                                                                                                                                //HapticFeedBakcs
                                                                                                                                //tracking Reasons
                                                                                                                                //Invite friends
                                                                                                                                //Love Daily Deeds? Rate US
                                                                                                                                //Follow us on Facebook
                                                                                                                                //send feedback
                                                                                                                                //privacy Policy

//        item { SettingSection("Preferences") }
        item { SettingItem(icon = painterResource(R.drawable.prayedlate), title = "Appearance", onClick = onAppearanceClick) }
        item { SettingItem(icon = painterResource(R.drawable.ic_launcher_foreground), title = "Haptic feedbacks", onClick = onHapticClick) }

//        item { SettingSection("About") }
        item { SettingItem(icon = painterResource(R.drawable.home), title = "Privacy policy", onClick = onPrivacyPolicyClick) }
        item{ SettingItem(icon= painterResource(R.drawable.prayedontime),title="Data Backup",onClick=onDataBackupClick) }
        item { SettingItem(icon = painterResource(R.drawable.ic_fajr), title = "feedback",onClick=feedback) }
        item { SettingItem(icon = painterResource(R.drawable.jamat), title = "Tracking Reason", onClick = tracking)}
    }
}

//@Preview
//@Composable
//fun SettingScreenPreview()
//{
//    SettingScreen(navController: NavController)
//}