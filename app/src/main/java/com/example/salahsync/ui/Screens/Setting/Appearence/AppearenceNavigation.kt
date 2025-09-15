//package com.example.salahsync.ui.Screens.Setting.Appearence
//
//import androidx.compose.runtime.Composable
//import com.example.salahsync.ui.Screens.PrayerScreenViewModel
//import com.example.salahsync.ui.Screens.SettingsOptions.AppearanceScreen
//import androidx.compose.foundation.layout.*
//import androidx.compose.material3.*
//import androidx.compose.runtime.*
//import androidx.compose.ui.Alignment
//import androidx.compose.ui.Modifier
//import androidx.compose.ui.graphics.Color
//import androidx.compose.ui.res.painterResource
//import androidx.compose.ui.unit.dp
//import androidx.navigation.NavHostController
//import androidx.navigation.compose.NavHost
//import androidx.navigation.compose.composable
//
//
//import com.example.salahsync.R
//@OptIn(ExperimentalMaterial3Api::class)
//@Composable
//fun AppearanceNavigation(onBack: () -> Unit) {
//    // State to track selected theme
//    val themeOptions = listOf("System", "Light", "Dark")
//    var selectedTheme by remember { mutableStateOf("System") }
//
//    Scaffold(
//        topBar = {
//            TopAppBar(
//                title = {
//                    // Center the title text
//                    Row(
//                        modifier = Modifier.fillMaxWidth(),
//                        horizontalArrangement = Arrangement.Center
//                    ) {
//                        Text("Appearance")
//                    }
//                },
//                navigationIcon = {
//                    IconButton(onClick = onBack) {
//                        Icon(
//                            painter = painterResource(id = R.drawable.leftarrow),
//                            contentDescription = "Back",
//                            tint = Color.Black
//                        )
//                    }
//                }
//            )
//        }
//    ) { innerPadding ->
//        // Screen content
//        Column(
//            modifier = Modifier
//                .fillMaxSize()
//                .padding(innerPadding)
//                .padding(16.dp)
//        ) {
//            Text(
//                text = "Theme",
//                style = MaterialTheme.typography.titleMedium,
//                modifier = Modifier.padding(bottom = 8.dp)
//            )
//
//            // Theme selection radio buttons
//            themeOptions.forEach { theme ->
//                Row(
//                    modifier = Modifier
//                        .fillMaxWidth()
//                        .padding(vertical = 8.dp),
//                    verticalAlignment = Alignment.CenterVertically
//                ) {
//                    RadioButton(
//                        selected = selectedTheme == theme,
//                        onClick = { selectedTheme = theme }
//                    )
//                    Text(
//                        text = theme,
//                        style = MaterialTheme.typography.bodyLarge,
//                        modifier = Modifier.padding(start = 8.dp)
//                    )
//                }
//            }
//        }
//    }
//}
