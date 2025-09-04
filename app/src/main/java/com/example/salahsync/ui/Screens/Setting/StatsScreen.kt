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
import com.example.salahsync.R


@OptIn(ExperimentalMaterial3Api::class)
@RequiresApi(Build.VERSION_CODES.O)

@Composable
fun StatsScreen(viewModel: PrayerScreenViewModel) {
    val prayerCounts by viewModel.prayerCounts

    LaunchedEffect(Unit) {
        viewModel.loadStats(LocalDate.now())
    }

    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        horizontalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        items(listOf(
            Triple("Prayed Late", viewModel.prayedCount.value, R.drawable.prayedlate),
            Triple("Not Prayed", viewModel.notPrayedCount.value, R.drawable.notprayed),
            Triple("On Time", viewModel.onTimeCount.value, R.drawable.prayedontime),
            Triple("In Jamaat", viewModel.jamatCount.value, R.drawable.jamat)
        )) { (title, count, icon) ->
            StatBoxWithPercentage(
                title = title,
                count = count,
                total = viewModel.totalCounts.value,
                backgroundColor = Color(0xFF007AFF),
                icon = icon,
                iconTint = Color.White,
                prayerCounts = prayerCounts,
                modifier = Modifier.fillMaxWidth()
            )
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
    prayerCounts: Map<String, Int> = mapOf(
        "Fajr" to 0, "Dhuhr" to 0, "Asr" to 0, "Maghrib" to 0, "Isha" to 0
    ),
    modifier: Modifier = Modifier
) {
    val percentage = if (total > 0) (count * 100) / total else 0
    Card(
        modifier = modifier
            .height(200.dp)
            .fillMaxWidth(),
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
            Column(
                modifier = Modifier
                    .weight(0.9f)
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
            PrayerBarChart(
                prayerCounts = prayerCounts,
                modifier = Modifier
                    .weight(1.1f)
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
        prayerCounts.entries.sortedByDescending { it.value }.forEach { (name, count) ->
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = name.take(3),
                    color = Color.White,
                    fontSize = 12.sp,
                    modifier = Modifier.width(45.dp)
                )
                Box(
                    modifier = Modifier
                        .height(14.dp)
                        .fillMaxWidth(fraction = if (maxCount > 0) (count / maxCount).coerceAtLeast(0.1f) else 0.1f)
                        .background(Color.White, shape = RoundedCornerShape(4.dp))
                )
            }
        }
    }
}
