package com.example.salahsync.ui.Screens


import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.runtime.*

import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.sp
import com.example.salahsync.ui.Screens.Setting.StatisticsScreen

import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.rememberCoroutineScope

import kotlinx.coroutines.launch
import com.example.salahsync.ui.Screens.SettingsOptions.AppearanceScreen
import com.example.salahsync.ui.Screens.SettingsOptions.HepticFeedBackScreen
import com.example.salahsync.ui.Screens.SettingsOptions.ManageDeedsScreen
import com.example.salahsync.ui.Screens.SettingsOptions.NotificationScreen
import com.example.salahsync.ui.Screens.SettingsOptions.PrivacyPolicyScreen
import com.example.salahsync.ui.Screens.SettingsOptions.SettingsNavHost
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.chrono.HijrahDate
import java.time.format.DateTimeFormatter
import java.util.Locale

// ---------- TopBottom.kt (fixed) ----------

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun TopBottom(viewModel: PrayerScreenViewModel) {
    val bottomNavController = rememberNavController()
    val selectedDate = remember { mutableStateOf(LocalDate.now()) }

    Column(modifier = Modifier.fillMaxSize()) {
        Box(modifier = Modifier.weight(1f)) {
            NavHost(
                navController = bottomNavController,
                startDestination = "prayer"
            ) {
                composable("prayer") {
                    Column {
                        SalahTopBar(selectedDate.value) { selectedDate.value = it }
                        Box(modifier = Modifier.weight(1f)) {
                            PrayerScreen(selectedDate.value, viewModel)
                        }
                    }
                }

                composable("stats") { StatisticsScreen() }
                composable("settings") { SettingsNavHost() }
            }
        }
        SalahBottomBar(bottomNavController)
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

@RequiresApi(Build.VERSION_CODES.O)
fun getHijriDate(date: LocalDate): String {
    // Convert LocalDate to HijrahDate (Islamic Umm Al-Qura calendar)
    val hijrahDate = HijrahDate.from(date)

    // Format the Hijri date (e.g., "2 Rabi‘ al-awwal 1447")
    val formatter = DateTimeFormatter.ofPattern("d MMMM yyyy", Locale("en"))
    return hijrahDate.format(formatter)
}
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun DateSlider(
    selectedDate: LocalDate,
    onDateSelected: (LocalDate) -> Unit
) {
    val today = LocalDate.now()
    val totalPastDays = 50000 // Days backward allowed
    val maxFutureDays = 4      // Days forward allowed
    val lazyListState = rememberLazyListState()
    val coroutineScope = rememberCoroutineScope()

    // Ensure the LazyRow scrolls to the selected date on initial load
    LaunchedEffect(selectedDate) {
        val index = (totalPastDays + selectedDate.toEpochDay() - today.toEpochDay()).toInt()
        if (index in 0 until (totalPastDays + maxFutureDays + 1)) {
            lazyListState.scrollToItem(index)
        }
    }

    Row(
        modifier = Modifier
            .fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Left arrow button
        IconButton(
            onClick = {
                val currentIndex = lazyListState.firstVisibleItemIndex
                val visibleItemsCount = lazyListState.layoutInfo.visibleItemsInfo.size
                if (currentIndex > 0) {
                    coroutineScope.launch {
                        // Scroll to the previous row
                        val newIndex = maxOf(0, currentIndex - visibleItemsCount)
                        lazyListState.animateScrollToItem(newIndex)
                        // Update selected date to the first visible date in the new row
                        val newDate = today.minusDays(totalPastDays.toLong()).plusDays(newIndex.toLong())
                        if (!newDate.isAfter(today.plusDays(maxFutureDays.toLong()))) {
                            onDateSelected(newDate)
                        }
                    }
                }
            },
            enabled = lazyListState.canScrollBackward,
            modifier = Modifier.size(32.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(32.dp)
                    .background(color = Color(0, 122, 255), shape = CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.previous),
                    contentDescription = "Previous Row",
                    tint = Color.White,
                    modifier = Modifier.size(20.dp)
                )
            }
        }

        // LazyRow for dates
        LazyRow(
            modifier = Modifier
                .weight(1f)
                .padding(horizontal = 4.dp),
            state = lazyListState,
            horizontalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            items(totalPastDays + maxFutureDays + 1) { listIndex ->
                val dayOffsetFromStart = listIndex.toLong()
                val currentDate = today.minusDays(totalPastDays.toLong()).plusDays(dayOffsetFromStart)

                // Only show dates up to maxFutureDays in the future
                if (!currentDate.isAfter(today.plusDays(maxFutureDays.toLong()))) {
                    val isSelected = currentDate == selectedDate
                    val isToday = currentDate == today
                    Column(
                        modifier = Modifier
                            .clip(
                                when {
                                    isSelected || isToday -> CircleShape // Circular shape for selected and today
                                    else -> RoundedCornerShape(12.dp) // Rounded for others
                                }
                            )
                            .background(
                                when {
                                    isSelected -> Color(0, 122, 255) // Filled blue for selected
                                    isToday -> Color.Transparent // Transparent for today (only border)
                                    else -> Color(255, 255, 255) // White for others
                                }
                            )
                            .then(
                                if (isToday && !isSelected) {
                                    Modifier.border(
                                        width = 2.dp,
                                        color = Color(0, 122, 255),
                                        shape = CircleShape
                                    )
                                } else {
                                    Modifier
                                }
                            )
                            .clickable { onDateSelected(currentDate) }
                            .padding(vertical = 8.dp, horizontal = 6.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = currentDate.dayOfWeek.name.take(3),
                            fontSize = 9.sp,
                            color = if (isSelected || isToday) Color.White else Color(176, 176, 179)
                        )
                        Text(
                            text = currentDate.dayOfMonth.toString(),
                            fontSize = 9.sp,
                            color = if (isSelected || isToday) Color.White else Color.Black
                        )
                    }
                }
            }
        }

        // Right arrow button
        IconButton(
            onClick = {
                val currentIndex = lazyListState.firstVisibleItemIndex
                val visibleItemsCount = lazyListState.layoutInfo.visibleItemsInfo.size
                val totalItems = lazyListState.layoutInfo.totalItemsCount
                if (currentIndex < totalItems - 1) {
                    coroutineScope.launch {
                        // Scroll to the next row
                        val newIndex = minOf(currentIndex + visibleItemsCount, totalItems - 1)
                        lazyListState.animateScrollToItem(newIndex)
                        // Update selected date to the first visible date in the new row
                        val newDate = today.minusDays(totalPastDays.toLong()).plusDays(newIndex.toLong())
                        if (!newDate.isAfter(today.plusDays(maxFutureDays.toLong()))) {
                            onDateSelected(newDate)
                        }
                    }
                }
            },
            enabled = lazyListState.canScrollForward,
            modifier = Modifier.size(32.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(32.dp)
                    .background(color = Color(0, 122, 255), shape = CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.next),
                    contentDescription = "Next Row",
                    tint = Color.White,
                    modifier = Modifier.size(20.dp)
                )
            }
        }
    }
}
@RequiresApi(Build.VERSION_CODES.O)
fun formatDateLabel(date: LocalDate): String {
    val today = LocalDate.now()
    val yesterday = today.minusDays(1)
    val tomorrow=today.plusDays(1)

    return when (date) {
        today -> "Today, ${date.format(DateTimeFormatter.ofPattern("d MMMM yyyy"))}"
        yesterday -> "Yesterday, ${date.format(DateTimeFormatter.ofPattern("d MMMM yyyy"))}"
        tomorrow -> "Tomorrow, ${date.format(DateTimeFormatter.ofPattern("d MMMM yyyy"))}"
        else -> date.format(DateTimeFormatter.ofPattern("d MMMM yyyy"))
    }
}

@Composable
fun SalahBottomBar(navController: NavController) {
    NavigationBar(
        containerColor = Color.White
    ) {
        NavigationBarItem(
            icon = { Icon(painterResource(id = R.drawable.home), contentDescription = "Home", modifier = Modifier.size(20.dp)) },
            selected = false,
            onClick = { navController.navigate("prayer") },
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = Color(0xFF9EA3A9)
            )
        )
        NavigationBarItem(
            icon = { Icon(painterResource(id = R.drawable.stats), contentDescription = "stats", modifier = Modifier.size(20.dp)) },
            selected = false,
            onClick = { navController.navigate("stats") },
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = Color(0xFF9EA3A9)
            )
        )
        NavigationBarItem(
            icon = { Icon(Icons.Default.Settings, contentDescription = "Settings") },
            selected = false,
            onClick = { navController.navigate("settings") },
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = Color(0xFF9EA3A9)
            )
        )
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
@Preview
fun SalahTopBarPreview() {
    SalahTopBar(
        selectedDate = LocalDate.now(),
        onDateSelected = { /* No-op for preview */ }
    )
}

