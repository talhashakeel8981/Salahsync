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
//import com.example.salahsync.ui.Screens.Setting.SearchScreen
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
import com.example.salahsync.ui.Screens.Setting.StatisticsScreen
import java.time.LocalDate
import java.time.format.DateTimeFormatter

// ---------- TopBottom.kt (fixed) ----------
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun TopBottom() {
    val navController = rememberNavController()
    val selectedDate = remember { mutableStateOf(LocalDate.now()) }

    Column(modifier = Modifier.fillMaxSize()) {
        // Content
        Box(modifier = Modifier.weight(1f)) {
            NavHost(
                navController = navController,
                startDestination = "prayer"
            ) {
                composable("prayer") {
                    Column {
                        // ✅ Top bar only here
                        SalahTopBar(
                            selectedDate = selectedDate.value,
                            onDateSelected = { selectedDate.value = it }
                        )
                        Box(modifier = Modifier.weight(1f)) {
                            PrayerScreen(selectedDate.value)
                        }
                    }
                }

                // ---------- NOTE: route name is "stats" now ----------
                composable("stats") {                                  // ← CHANGED: use "stats" route
                    StatisticsScreen()
                }

                composable("settings") {
                    SettingScreen()
                }
            }
        }

        // Bottom Bar — always visible
        SalahBottomBar(navController) // make sure this uses the same route names
    }
}


@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun SalahTopBar(
    selectedDate: LocalDate,
    onDateSelected: (LocalDate) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.White)
            .padding(8.dp, top = 33.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {


        Text(
            text = formatDateLabel(selectedDate),
            color = Color.Black,
            fontSize = 18.sp
        )
        Text(
            text = getHijriDate(selectedDate), // Placeholder
            color = Color.Black,
            fontSize = 14.sp
        )
        Spacer(modifier = Modifier.height(8.dp))
        DateSlider(
            selectedDate = selectedDate,
            onDateSelected = onDateSelected
        )
    }
}
// ✅ Placeholder for Hijri date
fun getHijriDate(date: LocalDate): String {
    // Implement Hijri conversion here
    return "18 Safar 1447" // Example static value
}
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun DateSlider(
    selectedDate: LocalDate,
    onDateSelected: (LocalDate) -> Unit
) {
    val today = LocalDate.now()

    val totalPastDays = 100    // Days backward allowed
    val maxFutureDays = 3      // Days forward allowed

    LazyRow(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
//            .background(Color(0xFF170000))
        ,
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        // ↓ REMOVED reverseLayout = true to start from left side
    ) {
        items(totalPastDays + maxFutureDays + 1) { listIndex ->

            // ↓ Changed calculation so index 0 = oldest date (today - totalPastDays)
            val dayOffsetFromStart = listIndex.toLong()
            val currentDate = today.minusDays(totalPastDays.toLong()).plusDays(dayOffsetFromStart)

            // Only show dates up to maxFutureDays in the future
            if (!currentDate.isAfter(today.plusDays(maxFutureDays.toLong()))) {
                val isSelected = currentDate == selectedDate
//Column for Top Bar Date
                Column(
                    modifier = Modifier
                        .clip(RoundedCornerShape(12.dp))
                        .background(if (isSelected) Color(0,122,255) else Color(255,255,255))
                        .clickable { onDateSelected(currentDate) }
                        .padding(vertical = 8.dp, horizontal = 12.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {

                    Text(
                        text = currentDate.dayOfWeek.name.take(3),
                        fontSize = 15.sp,
                        color = if (isSelected) Color.White else Color(176,176,179)
                    )
                    Text(
                        text = currentDate.dayOfMonth.toString(),
                        fontSize = 15.sp,
                        color = if (isSelected) Color.White else Color.Black
                    )
                }
            }
        }
    }
}



// Helper function for date label
@RequiresApi(Build.VERSION_CODES.O)
fun formatDateLabel(date: LocalDate): String {
    val today = LocalDate.now()
    val yesterday = today.minusDays(1)

    return when (date) {
        today -> "Today, ${date.format(DateTimeFormatter.ofPattern("d MMMM yyyy"))}"
        yesterday -> "Yesterday, ${date.format(DateTimeFormatter.ofPattern("d MMMM yyyy"))}"
        else -> date.format(DateTimeFormatter.ofPattern("d MMMM yyyy"))
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
            onClick = { navController.navigate("prayer") },
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = Color(0xFF9EA3A9))
        )
        NavigationBarItem(
            icon = { Icon(painterResource(id = R.drawable.stats), contentDescription = "stats", modifier = Modifier.size(20.dp)) },
            selected = false,
            onClick = { navController.navigate("stats") }
        )
        NavigationBarItem(
            icon = { Icon(Icons.Default.Settings, contentDescription = "Settings") },
            selected = false,
            onClick = { navController.navigate("settings") }
        )
    }
}


@RequiresApi(Build.VERSION_CODES.O)
@Composable
@Preview
fun SalahTopBarPreview()
{
    SalahTopBar(
        selectedDate = LocalDate.now(), // August 12, 2025
        onDateSelected = { /* No-op for preview */ }
    )
}

