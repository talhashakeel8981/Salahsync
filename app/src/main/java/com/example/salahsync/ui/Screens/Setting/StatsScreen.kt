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
//                .padding(innerPadding) //
                .background(Color(0xFFF3F5F8))


        )
        {


        //
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding) //
                .padding(16.dp)

            ,
            horizontalAlignment = Alignment.CenterHorizontally


        ) {


            Spacer(modifier = Modifier.height(24.dp))

            // Row 1 → Jamaat -------- On Time
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                StatBoxWithPercentage(
                    title = "Jamaat",
                    count = jamat,
                    total = total,
                    color = Color(0xFF2196F3),
                    modifier = Modifier.weight(1f),
                    icon = com.example.salahsync.R.drawable.jamat
                )
                StatBoxWithPercentage(
                    title = "On Time",
                    count = onTimeCount,
                    total = total,
                    color = Color(0xFFFFC107),
                    modifier = Modifier.weight(1f),
                    icon = com.example.salahsync.R.drawable.prayedontime
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Row 2 → Prayed Late -------- Not Prayed
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                StatBoxWithPercentage(
                    title = "Prayed Late",
                    count = prayedCount,
                    total = total,
                    color = Color(0xFF4CAF50),
                    modifier = Modifier.weight(1f),
                    icon = com.example.salahsync.R.drawable.prayedlate
                )
                StatBoxWithPercentage(
                    title = "Not Prayed",
                    count = notPrayedCount,
                    total = total,
                    color = Color(0xFFF44336),
                    modifier = Modifier.weight(1f),
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
    modifier: Modifier = Modifier,
    icon: Int //  NEW: parameter for drawable icon

) {
    val percentage = if (total > 0) (count * 100) / total else 0

    Card(
        modifier = modifier.height(120.dp),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = color // 🔄 CHANGED: removed fade (alpha)
        ),
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
                    tint = Color.White, //  CHANGED: white icon for contrast
                    modifier = Modifier.height(20.dp)
                )
                Text(
                    text = title,
                    style = MaterialTheme.typography.bodyLarge,
                    color = Color.White //  CHANGED: white text for contrast
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

            // Count / Total
            Text(
                text = "$count Times",
                fontSize = 14.sp,
                color = Color.White.copy(alpha = 0.9f) // slightly softer white
            )
        }
    }

}



