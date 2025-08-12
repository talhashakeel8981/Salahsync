package com.example.salahsync.ui.Screens.Setting

import android.os.Build
import androidx.annotation.RequiresApi
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
import java.time.LocalDate

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun TopBottom() {
    val navController = rememberNavController()
    val selectedDate = remember { mutableStateOf(LocalDate.now()) } // Shared state

    Scaffold(
        topBar = {
            SalahTopBar(
                selectedDate = selectedDate.value,
                onDateSelected = { selectedDate.value = it }
            )
        },
        bottomBar = { SalahBottomBar(navController) }
    ) { paddingValues ->
        NavHost(
            navController = navController,
            startDestination = "prayer",
            modifier = Modifier.padding(paddingValues)
        ) {
            composable("prayer") {
                PrayerScreen(selectedDate.value) // Pass date if needed
            }
//            composable("search") {
//                SearchScreen(selectedDate.value) // Pass date if needed
//            }
            composable("settings") {
                SettingScreen(selectedDate.value) // Pass date if needed
            }
        }
    }
}



//@Composable
//fun PrayerScreen() { Text("Prayer Screen") }
//
//@Composable
//fun SearchScreen() { Text("Search Screen") }
//
//@Composable
//fun Setting() { Text("Settings Screen") }

//Change
@Composable
fun PrayerScreen(selectedDate: LocalDate) {
    Text("Prayer Screen — $selectedDate")
}

//@Composable
//fun SearchScreen(selectedDate: LocalDate) {
//    Text("Search Screen — $selectedDate")
//}

@Composable
fun SettingScreen(selectedDate: LocalDate) { // also fix name: Setting → SettingScreen
    Text("Settings Screen — $selectedDate")
}
