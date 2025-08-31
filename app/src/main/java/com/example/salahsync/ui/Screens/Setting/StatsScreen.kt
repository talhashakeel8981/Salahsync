package com.example.salahsync.ui.Screens.Setting

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
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
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
            // 🔹 Grid with 2 columns → 4 boxes total
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
                        color = Color(0xFF2196F3),
                        icon = com.example.salahsync.R.drawable.jamat
                    )
                }

                // Box 2
                item {
                    StatBoxWithPercentage(
                        title = "On Time",
                        count = onTimeCount,
                        total = total,
                        color = Color(0xFFFFC107),
                        icon = com.example.salahsync.R.drawable.prayedontime
                    )
                }

                // Box 3
                item {
                    StatBoxWithPercentage(
                        title = "Prayed Late",
                        count = prayedCount,
                        total = total,
                        color = Color(0xFF4CAF50),
                        icon = com.example.salahsync.R.drawable.prayedlate
                    )
                }

                // Box 4
                item {
                    StatBoxWithPercentage(
                        title = "Not Prayed",
                        count = notPrayedCount,
                        total = total,
                        color = Color(0xFFF44336),
                        icon = com.example.salahsync.R.drawable.notprayed
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
    color: Color,
    icon: Int,
    modifier: Modifier = Modifier
) {
    val percentage = if (total > 0) (count * 100) / total else 0

    Card(
        modifier = modifier.height(140.dp),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = color),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(12.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Row: Icon + Title
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Icon(
                    painter = painterResource(id = icon),
                    contentDescription = title,
                    tint = Color.White,
                    modifier = Modifier.height(20.dp)
                )
                Text(
                    text = title,
                    style = MaterialTheme.typography.bodyLarge,
                    color = Color.White
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Percentage
            Text(
                text = "$percentage%",
                style = MaterialTheme.typography.headlineMedium,
                color = Color.White
            )

            Spacer(modifier = Modifier.height(4.dp))

            // Count
            Text(
                text = "$count Times",
                fontSize = 14.sp,
                color = Color.White.copy(alpha = 0.9f)
            )
        }
    }
}
