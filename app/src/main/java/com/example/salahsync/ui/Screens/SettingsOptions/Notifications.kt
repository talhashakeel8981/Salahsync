package com.example.salahsync.ui.Screens.SettingsOptions

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.clickable
import com.example.salahsync.R
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.selection.toggleable
import android.text.format.DateFormat
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*

import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import java.time.LocalTime
import java.time.format.DateTimeFormatter

import androidx.compose.foundation.gestures.snapping.rememberSnapFlingBehavior
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.text.BasicText
import androidx.compose.material3.*
import androidx.compose.runtime.*

import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp

import kotlin.math.abs
@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NotificationScreen(onBack: () -> Unit) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    // Center the title text
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Text("Notifications")
                    }
                },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(
                            painter = painterResource(id = R.drawable.leftarrow),
                            contentDescription = "Back",
                            tint = Color.Black
                        )
                    }
                }
            )
        }
    ) { innerPadding ->
        // Screen content
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp)
        ) {
            // Add your toggle here
            DailyReminderToggle()
        }
    }
}



@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun DailyReminderToggle() {
    var isChecked by remember { mutableStateOf(false) }
    var showTimeSheet by remember { mutableStateOf(false) }
    var selectedTime by remember { mutableStateOf(LocalTime.of(8, 0)) }

    val context = LocalContext.current
    val formatter = if (DateFormat.is24HourFormat(context))
        DateTimeFormatter.ofPattern("HH:mm")
    else
        DateTimeFormatter.ofPattern("hh:mm a")

    Column {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
                .toggleable(value = isChecked, onValueChange = { isChecked = it }),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text("Daily reminder", style = MaterialTheme.typography.bodyLarge)
            Switch(checked = isChecked, onCheckedChange = null)
        }

        if (isChecked) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { showTimeSheet = true }
                    .padding(16.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text("Time")
                Text(selectedTime.format(formatter))
            }
        }
    }

    if (showTimeSheet) {
        TimePickerBottomSheet(
            initialTime = selectedTime,
            onDismiss = { showTimeSheet = false },
            onConfirm = {
                selectedTime = it
                showTimeSheet = false
            }
        )
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TimePickerBottomSheet(
    initialTime: LocalTime,
    onDismiss: () -> Unit,
    onConfirm: (LocalTime) -> Unit
) {
    val context = LocalContext.current
    val is24h = DateFormat.is24HourFormat(context)

    var selectedHour by remember { mutableStateOf(initialTime.hour) }
    var selectedMinute by remember { mutableStateOf(initialTime.minute) }
    var isAm by remember { mutableStateOf(initialTime.hour < 12) }

    ModalBottomSheet(onDismissRequest = { onDismiss() }) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.fillMaxWidth().padding(16.dp)
        ) {
            // Header
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text("❌", modifier = Modifier.clickable { onDismiss() })
                Text("✅", modifier = Modifier.clickable {
                    val finalHour = if (is24h) {
                        selectedHour
                    } else {
                        if (isAm) if (selectedHour == 12) 0 else selectedHour
                        else if (selectedHour == 12) 12 else selectedHour + 12
                    }
                    onConfirm(LocalTime.of(finalHour, selectedMinute))
                })
            }

            Spacer(Modifier.height(24.dp))

            // Time picker row
            Row(verticalAlignment = Alignment.CenterVertically) {
                InfiniteNumberPicker(
                    values = if (is24h) (0..23).toList() else (1..12).toList(),
                    selected = if (is24h) selectedHour else (selectedHour % 12).let { if (it == 0) 12 else it },
                    onSelected = { selectedHour = it }
                )

                Text(":", style = MaterialTheme.typography.headlineMedium, modifier = Modifier.padding(horizontal = 8.dp))

                InfiniteNumberPicker(
                    values = (0..59).toList(),
                    selected = selectedMinute,
                    onSelected = { selectedMinute = it }
                )

                if (!is24h) {
                    Column {
                        Text(
                            "AM",
                            color = if (isAm) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface,
                            modifier = Modifier.clickable { isAm = true }.padding(8.dp)
                        )
                        Text(
                            "PM",
                            color = if (!isAm) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface,
                            modifier = Modifier.clickable { isAm = false }.padding(8.dp)
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun InfiniteNumberPicker(
    values: List<Int>,
    selected: Int,
    onSelected: (Int) -> Unit
) {
    val itemHeight = 50.dp
    val visibleCount = 3 // how many rows visible at once (1 above, 1 center, 1 below)

    val state = rememberLazyListState(
        initialFirstVisibleItemIndex = values.indexOf(selected) + 1000 * values.size
    )
    val flingBehavior = rememberSnapFlingBehavior(state)

    Box(
        modifier = Modifier
            .height(itemHeight * visibleCount) // ensure 3 items visible
            .width(70.dp),
        contentAlignment = Alignment.Center
    ) {
        // Selection highlight (center row)
        Box(
            modifier = Modifier
                .matchParentSize()
                .padding(horizontal = 4.dp)
                .height(itemHeight)
                .align(Alignment.Center),
        ) {
            Divider(color = MaterialTheme.colorScheme.primary, thickness = 2.dp)
        }

        LazyColumn(
            state = state,
            flingBehavior = flingBehavior,
            modifier = Modifier.matchParentSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            items(3000 * values.size) { index ->
                val value = values[index % values.size]
                Text(
                    text = value.toString().padStart(2, '0'),
                    style = if (index == state.firstVisibleItemIndex + 1)
                        MaterialTheme.typography.headlineMedium
                    else
                        MaterialTheme.typography.bodyLarge,
                    modifier = Modifier
                        .height(itemHeight)
                        .wrapContentHeight(Alignment.CenterVertically)
                )
            }
        }
    }

    // Update selection on scroll stop
    LaunchedEffect(state.isScrollInProgress) {
        if (!state.isScrollInProgress) {
            val centerIndex = state.firstVisibleItemIndex + 1
            val value = values[centerIndex % values.size]
            onSelected(value)
        }
    }
}



@Preview(showBackground = true)
@Composable
fun NotificationScreenPreview() {
    NotificationScreen(onBack = { })
}
