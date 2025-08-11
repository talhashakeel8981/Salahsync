package com.example.salahsync.ui.Screens.Setting

import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.salahsync.ui.Screens.SalahBottomBar
import com.example.salahsync.ui.Screens.SalahTopBar
import java.lang.reflect.Modifier

//@Composable
//fun TopBottom() {
//    val navController = rememberNavController()  // 1 NavController jo pura app control karega
//
//    Scaffold(
//        topBar = { SalahTopBar() },
//        bottomBar = { SalahBottomBar(navController) },  // Bottom bar ko pass karo navController
//    ) { paddingValues ->
//        NavHost(
//            navController = navController,
//            startDestination = "prayer",  // app start kaha se hogi
//            modifier = Modifier.padding(paddingValues)
//        ) {
//            composable("prayer") { PrayerScreen() }
//            composable("search") { SearchScreen() }
//            composable("settings") { SettingScreen() }  // Settings screen define karo
//        }
//    }
//}
