package com.example.salahsync.ui.Screens.Setting

import android.graphics.Color.rgb
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
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
import androidx.compose.material3.TopAppBarDefaults
import com.example.salahsync.R


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

    // Define color configurations for each stat
    val statColorConfigs = listOf(
        Triple(
            "Prayed Late",
            viewModel.prayedCount.value,
            StatColors(
                backgroundColor = MaterialTheme.colorScheme.primary,
                iconTint = MaterialTheme.colorScheme.onPrimary,
                barColor = Color(0xFFFFA726) // Orange
            )
        ),
        Triple(
            "Not Prayed",
            viewModel.notPrayedCount.value,
            StatColors(
                backgroundColor = MaterialTheme.colorScheme.error,
                iconTint = MaterialTheme.colorScheme.onError,
                barColor = Color(0xFFEF5350) // Red
            )
        ),
        Triple(
            "On Time",
            viewModel.onTimeCount.value,
            StatColors(
                backgroundColor = MaterialTheme.colorScheme.secondary,
                iconTint = MaterialTheme.colorScheme.onSecondary,
                barColor = Color(0xFF66BB6A) // Green
            )
        ),
        Triple(
            "In Jamaat",
            viewModel.jamatCount.value,
            StatColors(
                backgroundColor = MaterialTheme.colorScheme.tertiary,
                iconTint = MaterialTheme.colorScheme.onTertiary,
                barColor = Color(0xFF42A5F5) // Blue
            )
        )
    )

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Prayer Stats",
                        style = MaterialTheme.typography.titleLarge,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surface,
                    titleContentColor = MaterialTheme.colorScheme.onSurface
                )
            )
        },
        modifier = Modifier.fillMaxSize()
    ) { paddingValues ->
        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
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
                        else -> R.drawable.prayedlate
                    },
                    iconTint = colors.iconTint,
                    barColor = colors.barColor,
                    prayerCounts = when (title) {
                        "Prayed Late" -> prayedLateCounts
                        "Not Prayed" -> notPrayedCounts
                        "On Time" -> onTimeCounts
                        "In Jamaat" -> jamatCounts
                        else -> emptyMap()
                    },
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }
    }
    LaunchedEffect(Unit) {
        viewModel.loadStats(LocalDate.now())
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
            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
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
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
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