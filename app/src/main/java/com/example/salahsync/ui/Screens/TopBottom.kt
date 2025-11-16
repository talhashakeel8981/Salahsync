package com.example.salahsync.ui.Screens


import android.content.Context
import android.content.SharedPreferences
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
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
import androidx.compose.ui.res.painterResource
import androidx.compose.material3.Icon
import androidx.compose.ui.graphics.Color
import com.example.salahsync.R
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.*

import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.sp
import com.example.salahsync.ui.Screens.Setting.StatsScreen

import androidx.compose.material3.IconButton
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.navigation.compose.currentBackStackEntryAsState

import kotlinx.coroutines.launch
import com.example.salahsync.ui.Screens.SettingsOptions.SettingsNavHost
import kotlinx.coroutines.delay

import java.time.LocalDate
import java.time.LocalDateTime
import java.time.chrono.HijrahDate
import java.time.format.DateTimeFormatter
import java.util.Locale
import android.text.format.DateFormat
import androidx.compose.animation.*
import androidx.compose.animation.core.tween
import androidx.compose.runtime.getValue
// ---------- TopBottom.kt (fixed) ----------



// ---------- TopBottom.kt (NO ANIMATION + BACK WORKS + RED BAR ON ALL SCREENS) ----------
// ---------- TopBottom.kt (FIXED: RED BAR HIDDEN ON NOTIFICATIONS + BACK WORKS) ----------
// ---------- TopBottom.kt (SMOOTH RED BAR + CORRECT SETTINGS NAV) ----------
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun TopBottom(
    viewModel: PrayerScreenViewModel,
    startDestination: String = "home",
    navController: NavController
) {
    val bottomNavController = rememberNavController()
    val selectedDate = remember { mutableStateOf(LocalDate.now()) }

    // --- SharedPreferences ---
    val context = LocalContext.current
    val sharedPreferences = context.getSharedPreferences("notification_prefs", Context.MODE_PRIVATE)

    var isNotificationsEnabled by remember {
        mutableStateOf(sharedPreferences.getBoolean("is_notification_enabled", false))
    }
    var wasRedBarClicked by remember {
        mutableStateOf(sharedPreferences.getBoolean("red_bar_clicked", false))
    }

    // --- Real-time sync ---
    DisposableEffect(Unit) {
        val listener = SharedPreferences.OnSharedPreferenceChangeListener { prefs, key ->
            when (key) {
                "is_notification_enabled" -> isNotificationsEnabled = prefs.getBoolean(key, false)
                "red_bar_clicked" -> wasRedBarClicked = prefs.getBoolean(key, false)
            }
        }
        sharedPreferences.registerOnSharedPreferenceChangeListener(listener)
        onDispose {
            sharedPreferences.unregisterOnSharedPreferenceChangeListener(listener)
        }
    }

    // --- Observe current route ---
    val currentRoute by bottomNavController.currentBackStackEntryAsState()
    val currentScreen = currentRoute?.destination?.route

    // --- Reset red_bar_clicked when leaving notifications ---
    LaunchedEffect(currentScreen) {
        if (currentScreen == "prayer" || currentScreen == "stats") {
            if (wasRedBarClicked && !isNotificationsEnabled) {
                with(sharedPreferences.edit()) {
                    putBoolean("red_bar_clicked", false)
                    apply()
                }
            }
        }
    }

    // --- SMOOTH STATE: Track if red bar should be visible ---
    var shouldShowRedBar by remember { mutableStateOf(false) }
    val isOnPrayerOrStats = currentScreen == "prayer" || currentScreen == "stats"
    val isOnNotifications = currentScreen?.contains("notifications") == true

    LaunchedEffect(isNotificationsEnabled, wasRedBarClicked, currentScreen) {
        shouldShowRedBar = !isNotificationsEnabled &&
                (!wasRedBarClicked || !isOnNotifications) &&
                isOnPrayerOrStats
    }

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
                composable("stats") {
                    StatsScreen(viewModel)
                }
                composable("settings") {
                    SettingsNavHost(
                        navController = navController,
                        // FIXED: Only auto-open if red bar was clicked
                        autoOpenNotifications = wasRedBarClicked && !isNotificationsEnabled
                    )
                }
            }
        }

        // --- SMOOTH RED BAR WITH FADE ANIMATION ---
        AnimatedVisibility(
            visible = shouldShowRedBar,
            enter = fadeIn(),
            exit = fadeOut(animationSpec = tween(300))
        ) {
            NotificationPromptBar {
                // Mark as clicked
                with(sharedPreferences.edit()) {
                    putBoolean("red_bar_clicked", true)
                    apply()
                }

                bottomNavController.navigate("settings") {
                    popUpTo("prayer") { saveState = true }
                    restoreState = true
                    launchSingleTop = true
                }
            }
        }

        SalahBottomBar(bottomNavController)
    }
}

// --- SMOOTH RED BAR ---
@Composable
fun NotificationPromptBar(onClick: () -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.Red)
            .clickable(onClick = onClick)
            .padding(16.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = "Tap here to enable notifications",
            color = Color.White,
            fontSize = 16.sp,
            fontWeight = FontWeight.Medium,
            style = MaterialTheme.typography.bodyLarge
        )
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
            .background(MaterialTheme.colorScheme.surface)
            .padding(2.dp, top = 33.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = formatDateLabel(selectedDate),
            color = MaterialTheme.colorScheme.onSurface,
            fontSize = 18.sp
        )
        Text(
            text = getHijriDate(selectedDate),
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            fontSize = 14.sp
        )
        DateSlider(
            selectedDate = selectedDate,
            onDateSelected = onDateSelected
        )
        Spacer(modifier = Modifier.height(8.dp))
        RealTimeClockOnly()
    }
}
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun RealTimeClockOnly(
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    var currentTime by remember { mutableStateOf(LocalDateTime.now()) }

    // 🔁 Update every second
    LaunchedEffect(Unit) {
        while (true) {
            currentTime = LocalDateTime.now()
            delay(1000)
        }
    }

    // 🕒 Detect phone's 24-hour setting
    val is24HourFormat = DateFormat.is24HourFormat(context)
    val pattern = if (is24HourFormat) "HH:mm:ss" else "hh:mm:ss a"
    val timeFormatter = DateTimeFormatter.ofPattern(pattern, Locale.getDefault())

    // 🎨 Colors and UI styling
    val backgroundColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.1f)
    val textColor = MaterialTheme.colorScheme.onSurface
    val accentColor = MaterialTheme.colorScheme.primary

    Column(
        modifier = modifier
            .background(backgroundColor, RoundedCornerShape(16.dp))
            .padding(vertical = 12.dp, horizontal = 24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // 🕓 Time
        Text(
            text = currentTime.format(timeFormatter),
            color = accentColor,
            fontSize = 40.sp,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center
        )
        Spacer(modifier = Modifier.height(6.dp))
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun TimeScreen() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        contentAlignment = Alignment.Center
    ) {
        RealTimeClockOnly()
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun DateSlider(
    selectedDate: LocalDate,
    onDateSelected: (LocalDate) -> Unit
) {
    val today = LocalDate.now()
    val totalPastDays = 50000 // Days backward allowed
    val maxFutureDays = 4 // Days forward allowed
    val lazyListState = rememberLazyListState()
    val coroutineScope = rememberCoroutineScope()
    // Ensure the LazyRow scrolls to the selected date on initial load
    LaunchedEffect(Unit) {
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
                        .padding(start = 3.dp)
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
                                    isSelected -> Color(0, 122, 255) // keep brand blue ✅
                                    isToday -> Color.Transparent
                                    // CHANGED: from Color(255,255,255) -> MaterialTheme.colorScheme.surface
                                    else -> MaterialTheme.colorScheme.surface
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
                            color = when {
                                isSelected -> Color.White // Blue bg → White text
                                isToday -> Color(0, 122, 255) // Today border → Brand blue text
                                else -> MaterialTheme.colorScheme.onSurfaceVariant // Adaptive for light/dark
                            }
                        )
                        Text(
                            text = currentDate.dayOfMonth.toString(),
                            fontSize = 9.sp,
                            color = when {
                                isSelected -> Color.White // Blue bg → White text
                                isToday -> Color(0, 122, 255) // Today border → Brand blue text
                                else -> MaterialTheme.colorScheme.onSurface // Adaptive for light/dark
                            }
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
    val currentDestination = navController.currentBackStackEntryAsState().value?.destination?.route

    NavigationBar(
        containerColor = MaterialTheme.colorScheme.surface
    ) {
        val items = listOf(
            BottomNavItem("prayer", R.drawable.home, "Home"),
            BottomNavItem("stats", R.drawable.stats, "Stats"),
            BottomNavItem("settings", R.drawable.settings, "Settings") // ✅ use same drawable icon style
        )

        items.forEach { item ->
            val selected = currentDestination == item.route

            // 🔹 Smoothly animate icon color & size
            val iconColor by animateColorAsState(
                targetValue = if (selected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurfaceVariant,
                label = ""
            )
            val iconSize by animateDpAsState(
                targetValue = if (selected) 26.dp else 22.dp,
                label = ""
            )

            NavigationBarItem(
                icon = {
                    Icon(
                        painter = painterResource(id = item.iconRes),
                        contentDescription = item.label,
                        tint = iconColor,
                        modifier = Modifier.size(iconSize)
                    )
                },
                label = {
                    Text(
                        text = item.label,
                        fontSize = if (selected) 12.sp else 11.sp,
                        fontWeight = if (selected) FontWeight.Bold else FontWeight.Normal,
                        color = iconColor
                    )
                },
                selected = selected,
                onClick = {
                    if (!selected) {
                        navController.navigate(item.route) {
                            launchSingleTop = true
                            restoreState = true
                            popUpTo(navController.graph.startDestinationId) {
                                saveState = true
                            }
                        }
                    }
                },
                colors = NavigationBarItemDefaults.colors(
                    indicatorColor = Color.Transparent,
                    selectedIconColor = iconColor,
                    unselectedIconColor = iconColor
                )
            )
        }
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
data class BottomNavItem(val route: String, val iconRes: Int, val label: String)

@RequiresApi(Build.VERSION_CODES.O)
@Composable
@Preview
fun SalahTopBarPreview() {
    SalahTopBar(
        selectedDate = LocalDate.now(),
        onDateSelected = { /* No-op for preview */ }
    )
}
