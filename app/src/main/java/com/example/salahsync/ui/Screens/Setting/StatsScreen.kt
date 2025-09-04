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


@OptIn(ExperimentalMaterial3Api::class)
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun StatisticsScreen(viewModel: PrayerScreenViewModel) {
    val prayedCount by viewModel.prayedCount
    val notPrayedCount by viewModel.notPrayedCount
    val onTimeCount by viewModel.onTimeCount
    val jamat by viewModel.jamatCount
    val total by viewModel.totalCounts

    LaunchedEffect(Unit) {
        viewModel.loadStats(LocalDate.now())
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Box(
                        modifier = Modifier.fillMaxWidth(),
                        contentAlignment = Alignment.Center
                    ) {
                        Text("Statistics")
                    }
                }
            )
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFFF3F5F8))
                .padding(innerPadding)
        ) {
            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // Box 1
                item {
                    StatBoxWithPercentage(
                        title = "Jamaat",
                        count = jamat,
                        total = total,
                        backgroundColor = Color(rgb(94, 147, 108)),
                        icon = com.example.salahsync.R.drawable.jamat,
                        iconTint = Color(0xFF1DD1A1)
                    )
                }

                // Box 2
                item {
                    StatBoxWithPercentage(
                        title = "On Time",
                        count = onTimeCount,
                        total = total,
                        backgroundColor = Color(rgb(255, 217, 61)),
                        icon = com.example.salahsync.R.drawable.prayedontime,
                        iconTint = Color(rgb(255, 154, 0))
                    )
                }

                // Box 3
                item {
                    StatBoxWithPercentage(
                        title = "Prayed Late",
                        count = prayedCount,
                        total = total,
                        backgroundColor = Color(rgb(255, 99, 99)),
                        icon = com.example.salahsync.R.drawable.prayedlate,
                        iconTint = Color(0xFFD64F73) //
                    )
                }

                // Box 4
                item {
                    StatBoxWithPercentage(
                        title = "Not Prayed",
                        count = notPrayedCount,
                        total = total,
                        backgroundColor = Color(rgb(87, 86, 79)),
                        icon = com.example.salahsync.R.drawable.notprayed,
                        iconTint = Color(0xFF000000)
                    )
                }
            }
        }
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
    modifier: Modifier = Modifier
) {
    val percentage = if (total > 0) (count * 100) / total else 0

    Card(
        modifier = modifier
            .height(200.dp) // fixed height
            .fillMaxWidth(), // take full width of grid cell
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = backgroundColor),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(12.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            // ✅ Left Side Stats
            Column(
                modifier = Modifier
                    .weight(0.9f) // give fixed space
                    .fillMaxHeight(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
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
                        color = Color.White
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = "$percentage%",
                    style = MaterialTheme.typography.headlineMedium,
                    color = Color.White
                )

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = "$count Times",
                    fontSize = 14.sp,
                    color = Color.White
                )
            }

            // ✅ Right Side Prayer Bar Chart
            PrayerBarChart(
                prayerCounts = mapOf(
                    "Fajr" to 1,
                    "Zuhr" to 3,
                    "Asr" to 2,
                    "Maghrib" to 4,
                    "Isha" to 2
                ),
                modifier = Modifier
                    .weight(1.1f) // chart thoda zyada space le
                    .padding(start = 8.dp)
            )
        }
    }
}

@Composable
fun PrayerBarChart(
    prayerCounts: Map<String, Int>,
    modifier: Modifier = Modifier
) {
    val maxCount = (prayerCounts.values.maxOrNull() ?: 1).toFloat()

    Column(
        modifier = modifier.fillMaxHeight(),
        verticalArrangement = Arrangement.SpaceEvenly
    ) {
        prayerCounts.forEach { (name, count) ->
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Short Prayer Name (Faj, Zuh, Asr, etc.)
                Text(
                    text = name.take(3),
                    color = Color.White,
                    fontSize = 12.sp,
                    modifier = Modifier.width(45.dp)
                )

                // Bar
                Box(
                    modifier = Modifier
                        .height(14.dp) // fixed bar height
                        .fillMaxWidth(fraction = count / maxCount)
                        .background(Color.White, shape = RoundedCornerShape(4.dp))
                )
            }
        }
    }
}
