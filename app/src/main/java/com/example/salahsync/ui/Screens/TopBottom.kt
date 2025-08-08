package com.example.salahsync.ui.Screens


import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
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

@Composable

fun TopBottom()
{
    Scaffold (
        topBar = {SalahTopBar()},
        bottomBar = { SalahBottomBar() }
    )
    {
paddingValues ->
        Box(
     modifier = Modifier
         .fillMaxSize()
         .padding(paddingValues)
        )
        PrayerScreen()
    }
}


@Composable
fun SalahTopBar() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(64.dp) // custom height
            .background(MaterialTheme.colorScheme.primary),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
                text = "Today, 6 August 2025",
                color = MaterialTheme.colorScheme.onPrimary
            )
            Text(
                text = "12 Safar 1447",
                color = MaterialTheme.colorScheme.onPrimary
            )
        }
    }
}

@Composable
fun SalahBottomBar() {
    NavigationBar {
        NavigationBarItem(
            icon = { Icon(Icons.Default.Home, contentDescription = "Home") },
            label = { Text("Home") },
            selected = true,
            onClick = { /* Navigate to Home */ }
        )
        NavigationBarItem(
            icon = { Icon(Icons.Default.Search, contentDescription = "Search") },
            label = { Text("Search") },
            selected = false,
            onClick = { /* Navigate to Search */ }
        )
        NavigationBarItem(
            icon = { Icon(Icons.Default.Settings, contentDescription = "Settings") },
            label = { Text("Settings") },
            selected = false,
            onClick = { /* Navigate to Settings */ }
        )
    }
}



@Preview(showSystemUi = true)
@Composable
fun TopBar() {
    TopBottom()
}
