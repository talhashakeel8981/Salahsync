package com.example.salahsync.ui.Screens


import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.*
import androidx.compose.ui.Alignment
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.salahsync.ui.Screens.Setting.SearchScreen
import com.example.salahsync.ui.Screens.Setting.SettingScreen
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.painterResource
import androidx.compose.material3.Icon
import androidx.compose.ui.graphics.Color
import com.example.salahsync.R

@Composable


fun TopBottom() {
    val navController = rememberNavController()

    Scaffold(
        topBar = { SalahTopBar() },
        bottomBar = { SalahBottomBar(navController) }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            NavHost(
                navController = navController,       // <---- Here!
                startDestination = "prayer"           // <---- Define your start screen route
            ) {
                composable("prayer") { PrayerScreen() }
                composable("search") { SearchScreen() }
                composable("settings") { SettingScreen() }
            }
        }
    }
}


@Composable
fun SalahTopBar() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(64.dp) // custom height
            .background(color = Color.White),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
                text = "Today, 6 August 2025",
                color = Color.Black
            )
            Text(
                text = "12 Safar 1447",
                color = Color.Black
            )
        }
    }
}




@Composable
fun SalahBottomBar(navController: NavController) {
    NavigationBar(
        containerColor = Color.White   // <-- sets white background correctly
    ) {
        NavigationBarItem(
            icon = { Icon(painterResource(id = R.drawable.home), contentDescription = "Home", modifier = Modifier.size(20.dp)) },
            selected = false,
            onClick = { navController.navigate("prayer") }
        )
        NavigationBarItem(
            icon = { Icon(painterResource(id = R.drawable.stats), contentDescription = "Search", modifier = Modifier.size(20.dp)) },
            selected = false,
            onClick = { navController.navigate("search") }
        )
        NavigationBarItem(
            icon = { Icon(Icons.Default.Settings, contentDescription = "Settings") },
            selected = false,
            onClick = { navController.navigate("settings") }
        )
    }
}




@Preview(showSystemUi = true)
@Composable
fun TopBar() {
    TopBottom()
}
