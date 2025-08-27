package com.example.salahsync.ui.Screens.Setting

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import com.example.salahsync.ui.Screens.PrayerScreenViewModel

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text

import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.runtime.State
import com.example.salahsync.DataBase.PrayerDao

import java.time.LocalDate

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun StatisticsScreen(viewModel: PrayerScreenViewModel) {
    val prayedCount by viewModel.prayedCount
    val notPrayedCount by viewModel.notPrayedCount
    val onTimeCount by viewModel.onTimeCount

// Added LaunchedEffect to load stats when the screen is composed
    // COMMENT: Ensures stats are fetched and updated when the Stats screen is opened
    LaunchedEffect(Unit) {
        viewModel.loadStats(LocalDate.now()) // Load stats for the current date
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = "Prayer Statistics", style = MaterialTheme.typography.headlineSmall)
        Spacer(modifier = Modifier.height(16.dp))
        Text(text = "Prayed: $prayedCount")
        Text(text = "Not Prayed: $notPrayedCount")
        Text(text = "On Time: $onTimeCount")
    }
}