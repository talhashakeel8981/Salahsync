package com.example.salahsync.ui.Screens.Setting

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text

import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.runtime.State
import androidx.compose.ui.graphics.Color
import com.example.salahsync.DataBase.PrayerDao
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.*
import androidx.compose.runtime.*

import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp



import java.time.LocalDate


import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*

import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp




@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun StatisticsScreen(viewModel: PrayerScreenViewModel) {
    val prayedCount by viewModel.prayedCount // "Prayed Late"
    val notPrayedCount by viewModel.notPrayedCount
    val onTimeCount by viewModel.onTimeCount // "On Time"
    val jamat by viewModel.jamatCount
    val total by viewModel.totalCounts

    LaunchedEffect(Unit) {
        viewModel.loadStats(LocalDate.now())
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Prayer Statistics",
            style = MaterialTheme.typography.headlineSmall
        )

        Spacer(modifier = Modifier.height(24.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            StatBoxWithPercentage(
                title = "Prayed Late",
                count = prayedCount,
                total = total,
                color = Color(0xFF4CAF50),
                modifier = Modifier.weight(1f)
            )
            StatBoxWithPercentage(
                title = "Not Prayed",
                count = notPrayedCount,
                total = total,
                color = Color(0xFFF44336),
                modifier = Modifier.weight(1f)
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            StatBoxWithPercentage(
                title = "On Time",
                count = onTimeCount,
                total = total,
                color = Color(0xFFFFC107),
                modifier = Modifier.weight(1f)
            )
            StatBoxWithPercentage(
                title = "Jamaat",
                count = jamat,
                total = total,
                color = Color(0xFF2196F3),
                modifier = Modifier.weight(1f)
            )
        }
    }
}

@Composable
fun StatBoxWithPercentage(
    title: String,
    count: Int,
    total: Int,
    color: Color,
    modifier: Modifier = Modifier
) {
    val percentage = if (total > 0) (count * 100) / total else 0
    // COMMENT: Prevents divide-by-zero error

    Card(
        modifier = modifier.height(100.dp),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = color.copy(alpha = 0.15f)),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(12.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(text = title, style = MaterialTheme.typography.bodyLarge, color = color)
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "$count ($percentage%)",
                style = MaterialTheme.typography.headlineMedium,
                color = color
            )
        }
    }
}