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


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingScreen(navController: NavController) {
    Scaffold(
        topBar = {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(70.dp)
                    .background(Color(0xFFF4F5F9)),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "Settings",
                    fontSize = 24.sp,
                    color = Color.Black
                )
            }
        }
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            // Section 1
            item { SettingSection("General") }
            item {
                SettingItem(
                    icon = painterResource(id = R.drawable.jamat),
                    title = "Notifications"
                ) { navController.navigate("notifications") }
            }
            item {
                SettingItem(
                    icon = painterResource(id = R.drawable.notprayed),
                    title = "Manage deeds"
                ) { navController.navigate("manage_deeds") }
            }

            // Section 2
            item { SettingSection("Preferences") }
            item {
                SettingItem(
                    icon = painterResource(id = R.drawable.prayedlate),
                    title = "Appearance"
                ) { navController.navigate("appearance") }
            }
            item {
                SettingItem(
                    icon = painterResource(id = R.drawable.ic_launcher_foreground),
                    title = "Haptic feedbacks"
                ) { navController.navigate("haptic_feedbacks") }
            }

            // Section 3
            item { SettingSection("About") }
            item {
                SettingItem(
                    icon = painterResource(id = R.drawable.home),
                    title = "Privacy policy"
                ) { navController.navigate("privacy_policy") }
            }
        }
    }
}

//@Preview
//@Composable
//fun SettingScreenPreview()
//{
//    SettingScreen(navController: NavController)
//}