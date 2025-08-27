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

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun StatisticsScreen(viewModel: PrayerScreenViewModel) {
    val prayedCount by viewModel.prayedCount
    val notPrayedCount by viewModel.notPrayedCount
    val onTimeCount by viewModel.onTimeCount
    val jamat by viewModel.jamatCount

    // Load stats when screen opens
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

        // 1st row -> Prayed + Not Prayed
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            StatBox(
                title = "Prayed",
                count = prayedCount,
                color = Color(0xFF4CAF50),
                modifier = Modifier.weight(1f) // ✅ equal width
            )
            StatBox(
                title = "Not Prayed",
                count = notPrayedCount,
                color = Color(0xFFF44336),
                modifier = Modifier.weight(1f) // ✅ equal width
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        // 2nd row -> On Time + Jamaat
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            StatBox(
                title = "On Time",
                count = onTimeCount,
                color = Color(0xFFFFC107),
                modifier = Modifier.weight(1f)
            )
            StatBox(
                title = "Jamaat",
                count = jamat,
                color = Color(0xFF2196F3),
                modifier = Modifier.weight(1f)
            )
        }
    }
}

@Composable
fun StatBox(title: String, count: Int, color: Color, modifier: Modifier = Modifier) {
    Card(
        modifier = modifier.height(100.dp),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = color.copy(alpha = 0.15f)), // soft background
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
            Text(text = "$count", style = MaterialTheme.typography.headlineMedium, color = color)
        }
    }
}
