package com.example.salahsync.ui.Screens


import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.material.icons.Icons
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
import androidx.compose.material3.Icon
import androidx.compose.ui.graphics.Color
import com.example.salahsync.R
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.*

import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.sp
import java.time.LocalDate
import java.time.format.DateTimeFormatter

@RequiresApi(Build.VERSION_CODES.O)
@Composable


fun TopBottom() {
    val navController = rememberNavController()

    // 🔹 NEW: Shared selected date state
    val selectedDate = remember { mutableStateOf(LocalDate.now()) }  // <-- NEW

    Scaffold(
        topBar = {
            // 🔹 UPDATED: SalahTopBar replaced with DateSlider
            DateSlider(  // <-- NEW
                selectedDate = selectedDate.value,
                onDateSelected = { selectedDate.value = it }
            )
        },
        bottomBar = { SalahBottomBar(navController) }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            NavHost(
                navController = navController,
                startDestination = "prayer"
            ) {
                // 🔹 Pass selected date to screens if needed
                composable("prayer") { PrayerScreen(selectedDate.value) }  // <-- UPDATED if PrayerScreen takes date
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



@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun SalahScreen() {
    var selectedDate by remember { mutableStateOf(LocalDate.now()) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        // Date slider (top of this screen)
        DateSlider(
            selectedDate = selectedDate,
            onDateSelected = { selectedDate = it }
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Rest of your screen content
        Text(
            text = "Selected Date: ${selectedDate.format(DateTimeFormatter.ofPattern("d MMM yyyy"))}",
            modifier = Modifier.padding(16.dp)
        )
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun DateSlider(
    selectedDate: LocalDate,
    onDateSelected: (LocalDate) -> Unit
) {
    val today = remember { LocalDate.now() }
    val days = remember { (0..30).map { today.plusDays(it.toLong()) } }

    LazyRow(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(days) { date ->
            val isSelected = date == selectedDate
            Column(
                modifier = Modifier
                    .clip(RoundedCornerShape(12.dp))
                    .background(if (isSelected) Color.Black else Color.LightGray)
                    .clickable { onDateSelected(date) }
                    .padding(vertical = 8.dp, horizontal = 12.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = date.dayOfMonth.toString(),
                    color = if (isSelected) Color.White else Color.Black
                )
                Text(
                    text = date.dayOfWeek.name.take(3),
                    fontSize = 12.sp,
                    color = if (isSelected) Color.White else Color.Black
                )
            }
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




//@Preview(showSystemUi = true)
//@Composable
//fun TopBar() {
//    TopBottom()
//}
