package com.example.salahsync.ui.Screens.Setting

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingScreen() {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = "Settings") }
            )
        }
    ) { innerPadding ->
        // Screen ka main content
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding) // 👈 Overlap avoid karega
        ) {
            Text("Settings screen content yahan aayega")
        }
    }
}





@Preview
@Composable
fun SettingPreview()
{
    SettingScreen()
}
