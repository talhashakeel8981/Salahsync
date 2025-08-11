package com.example.salahsync.ui.Screens.Setting

import androidx.compose.runtime.Composable
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.*
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import com.example.salahsync.ui.Screens.SalahBottomBar
import com.example.salahsync.ui.Screens.SalahTopBar

@Composable
fun TopBottom() {
    val navController = rememberNavController()  // 1 NavController jo pura app control karega

    Scaffold(
        topBar = { SalahTopBar() },
        bottomBar = { SalahBottomBar(navController) },  // Bottom bar ko pass karo navController
    ) { paddingValues ->
        NavHost(
            navController = navController,
            startDestination = "prayer",  // app start kaha se hogi
            modifier = Modifier.padding(paddingValues)
        ) {
            composable("prayer") { PrayerScreen() }
            composable("search") { SearchScreen() }
            composable("settings") { SettingScreen() }  // Settings screen define karo
        }
    }
}


@Composable
fun PrayerScreen() { Text("Prayer Screen") }

@Composable
fun SearchScreen() { Text("Search Screen") }

@Composable
fun Setting() { Text("Settings Screen") }
