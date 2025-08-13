package com.example.salahsync.ui.Screens.Setting

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingScreen() {
    Scaffold(
        topBar = {
            // Custom Top Bar
            Box(
                modifier = Modifier
                 .fillMaxWidth()
                    .height(50.dp)
                    .background(Color.Red),
                contentAlignment = Alignment.Center // center text vertically & horizontally
            ) {
                Text(
                    modifier = Modifier,
                    text = "Setting",
                    fontSize = 30.sp,
                    color = Color.White
                )
            }
        }
    ) { innerPadding ->
        // Screen content (centered in full screen)
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            contentAlignment = Alignment.Center
        ) {
            Text("Settings Screen")
        }
    }
}

@Preview
@Composable
fun SettingScreenPreview()
{
    SettingScreen()
}