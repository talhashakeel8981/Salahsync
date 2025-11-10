package com.example.salahsync.ui.Screens.Setting

import android.graphics.Color.rgb
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.focusable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsFocusedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import com.example.salahsync.ui.Screens.PrayerScreenViewModel

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text

import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TopAppBar

import androidx.compose.ui.res.painterResource

import androidx.compose.ui.unit.sp
import androidx.compose.ui.graphics.Color

import java.time.LocalDate


import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.Divider
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.draw.clip
import com.example.salahsync.R

import android.util.Log // ADDED: Import Log for debugging // Why: To log stats display and gender
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign

// Define a data class to hold color configurations for each stat
data class StatColors(
    val backgroundColor: Color,
    val iconTint: Color,
    val barColor: Color
)

@OptIn(ExperimentalMaterial3Api::class)
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun StatsScreen(viewModel: PrayerScreenViewModel) {
    val prayedLateCounts by viewModel.prayedLateCounts
    val notPrayedCounts by viewModel.notPrayedCounts
    val onTimeCounts by viewModel.onTimeCounts
    val jamatCounts by viewModel.jamatCounts
    // ADDED: Observe user gender // Why: Determines whether to show In Jamaat (male) or Exempted (female)
    val gender by viewModel.userGender

    // ✅ Theme-aware color configs (Reordered + Meaningful colors)
    // ✅ Custom icon colors applied for all statuses (background + bar remain theme-based)
    // CHANGED: Replaced Menstruation with Exempted for females // Why: Matches requirement for female-specific stats




    val isDark = isSystemInDarkTheme()

    val statColorConfigs = listOf(
        Triple(
            if (gender == "Woman") "Exempted" else "In Jamaat",
            viewModel.jamatCount.value,
            StatColors(
                backgroundColor = if (isDark) Color(0xFF344955) else Color(0xFFCBDCEB), // Light green tint
                iconTint = if (gender == "Woman") Color(0xFF8B5CF6) else Color(0xFF22C55E),
                barColor = if (gender == "Woman") MaterialTheme.colorScheme.primary else Color(0xFF15803D)
            )
        ),
        Triple(
            "On Time",
            viewModel.onTimeCount.value,
            StatColors(
                backgroundColor = if (isDark) Color(0xFF344955) else Color(0xFFCBDCEB), // Light blue tint
                iconTint = Color(0xFF3B82F6),
                barColor = Color(0xFF1D4ED8)
            )
        ),
        Triple(
            "Prayed Late",
            viewModel.prayedCount.value,
            StatColors(
                backgroundColor = if (isDark) Color(0xFF344955) else Color(0xFFCBDCEB), // Light amber tint
                iconTint = Color(0xFFF59E0B),
                barColor = Color(0xFFE07B00)
            )
        ),
        Triple(
            "Not Prayed",
            viewModel.notPrayedCount.value,
            StatColors(
                backgroundColor = if (isDark) Color(0xFF344955) else Color(0xFFCBDCEB), // Light red tint
                iconTint = Color(0xFFEF4444),
                barColor = Color(0xFFB91C1C)
            )
        )
    )
    // State for selected tab (for styling only, non-functional for data)
    var selectedTabIndex by remember { mutableIntStateOf(0) }

    // ✅ Tabs will also adapt to dark mode
  

    val tabColors = listOf(
        if (isDark) Color(0xFF22C55E) else Color(0xFF16A34A), // Green shades
        if (isDark) Color(0xFF3B82F6) else Color(0xFF2563EB), // Blue shades
        if (isDark) Color(0xFFF59E0B) else Color(0xFFD97706), // Amber shades
        if (isDark) Color(0xFFEF4444) else Color(0xFFDC2626)  // Red shades
    )
    val tabs = listOf("Week", "Month", "Year", "All Time")

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        text = "Prayer Insights",
                        style = MaterialTheme.typography.titleLarge.copy(
                            fontWeight = FontWeight.SemiBold
                        ),
                        color = MaterialTheme.colorScheme.onSurface
                    )
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surface
                )
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
//                .fillMaxSize()
                .padding(paddingValues)
        ) {
            //  TabRow updated to use theme colors
            val tabBackgroundColor = if (isDark) Color(0xFF5D688A) else Color(0xFFCBD5E1) // 🌞🌙 Different for both themes

            TabRow(
                selectedTabIndex = selectedTabIndex,
                modifier = Modifier
                    .fillMaxWidth()
                    .background(tabBackgroundColor), // ✅ Applied custom background
                containerColor = tabBackgroundColor,
                contentColor = MaterialTheme.colorScheme.onSurfaceVariant,
                indicator = { tabPositions ->
                    TabRowDefaults.Indicator(
                        modifier = Modifier
                            .tabIndicatorOffset(tabPositions[selectedTabIndex])
                            .clip(RoundedCornerShape(topStart = 4.dp, topEnd = 4.dp)),
                        color = MaterialTheme.colorScheme.primary,
                        height = 4.dp
                    )
                }
            ) {
                tabs.forEachIndexed { index, title ->
                    val interactionSource = remember { MutableInteractionSource() }
                    val isFocused by interactionSource.collectIsFocusedAsState()
                    Tab(
                        selected = selectedTabIndex == index,
                        onClick = {
                            selectedTabIndex = index
                            // ADDED: Log tab selection // Why: Debug which tab is selected
                            Log.d("StatsScreen", "Selected tab: $title")
                        },
                        modifier = Modifier
                            .padding(horizontal = 10.dp, vertical = 15.dp)
                            .background(
                                color = when {
                                    selectedTabIndex == index -> tabColors[index].copy(alpha = 0.15f)
                                    isFocused -> MaterialTheme.colorScheme.surfaceVariant
                                    else -> Color.Transparent
                                },
                                shape = RoundedCornerShape(10.dp)
                            )
                            .focusable()
                            .height(45.dp),
                        interactionSource = interactionSource,
                        text = {
                            Text(
                                text = title,
                                style = MaterialTheme.typography.labelLarge.copy(fontSize = 12.sp),
                                color = if (selectedTabIndex == index) tabColors[index] else MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    )
                }
            }

            // Stats Gridsss
            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                items(statColorConfigs) { (title, count, colors) ->
                    StatBoxWithPercentage(
                        title = title,
                        count = count,
                        total = viewModel.totalCounts.value,
                        backgroundColor = colors.backgroundColor,
                        icon = when (title) {
                            "Prayed Late" -> R.drawable.prayedlate
                            "Not Prayed" -> R.drawable.notprayed
                            "On Time" -> R.drawable.prayedontime
                            "In Jamaat" -> R.drawable.jamat
                            "Exempted" -> R.drawable.track // CHANGED: Added Exempted icon // Why: For female users in stats
                            else -> R.drawable.prayedlate
                        },
                        iconTint = colors.iconTint,
                        barColor = colors.barColor,
                        prayerCounts = when (title) {
                            "Prayed Late" -> prayedLateCounts
                            "Not Prayed" -> notPrayedCounts
                            "On Time" -> onTimeCounts
                            "In Jamaat" -> jamatCounts
                            "Exempted" -> jamatCounts
                            else -> emptyMap()
                        },
                        modifier = Modifier.fillMaxWidth()
                    )
                    // ADDED: Log stat display // Why: Debug which stats are displayed
                    Log.d("StatsScreen", "Displaying stat: $title, count: $count")
                }
            }
        }
    }

    // ✅ Loads stats when tab changes
    LaunchedEffect(selectedTabIndex) {
        viewModel.loadStatsForPeriod(tabs[selectedTabIndex], LocalDate.now())
    }
}

@Composable
fun StatBoxWithPercentage(
    title: String,
    count: Int,
    total: Int,
    backgroundColor: Color,
    icon: Int,
    iconTint: Color,
    barColor: Color,
    prayerCounts: Map<String, Int> = mapOf(
        "Fajr" to 0, "Dhuhr" to 0, "Asr" to 0, "Maghrib" to 0, "Isha" to 0
    ),
    modifier: Modifier = Modifier
) {
    val percentage = if (total > 0) (count * 100) / total else 0
    Card(
        modifier = modifier
            .height(220.dp)
            .fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = backgroundColor),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            // Title Row
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Icon(
                    painter = painterResource(id = icon),
                    contentDescription = title,
                    tint = iconTint,
                    modifier = Modifier.size(24.dp)
                )
                Text(
                    text = title,
                    style = MaterialTheme.typography.bodyLarge,
                    color = iconTint
                )
            }
            // Percentage + Count
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(
                    text = "$percentage%",
                    style = MaterialTheme.typography.headlineMedium,
                    color = iconTint
                )
                Text(
                    text = "$count Times",
                    fontSize = 14.sp,
                    color = iconTint
                )
            }
            // Bar Chart
            PrayerBarChart(
                prayerCounts = prayerCounts,
                barColor = barColor,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(80.dp)
            )
        }
    }
}

@Composable
fun PrayerBarChart(
    prayerCounts: Map<String, Int>,
    barColor: Color,
    modifier: Modifier = Modifier
) {
    val maxCount = (prayerCounts.values.maxOrNull() ?: 1).toFloat()
    val fixedPrayerOrder = listOf("Fajr", "Dhuhr", "Asr", "Maghrib", "Isha")
    Column(
        modifier = modifier.fillMaxHeight(),
        verticalArrangement = Arrangement.SpaceEvenly
    ) {
        fixedPrayerOrder.forEach { name ->
            val count = prayerCounts[name] ?: 0
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .height(10.dp)
                        .fillMaxWidth(fraction = if (maxCount > 0) (count / maxCount).coerceAtLeast(0.1f) else 0.1f)
                        .background(barColor, shape = RoundedCornerShape(4.dp))
                )
            }
        }
    }
}