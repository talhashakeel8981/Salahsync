package com.example.salahsync.ui.Screens.Setting.Notification

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
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.selection.toggleable
import android.text.format.DateFormat

import androidx.compose.ui.platform.LocalContext
import java.time.LocalTime
import java.time.format.DateTimeFormatter

import androidx.compose.foundation.gestures.snapping.rememberSnapFlingBehavior
import androidx.compose.foundation.lazy.rememberLazyListState

import android.Manifest
import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent

import androidx.core.content.ContextCompat
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import android.widget.Toast
import java.time.LocalDateTime
import java.time.ZoneId

// Add NotificationReceiver import for scheduling notifications


import android.content.pm.PackageManager
import android.util.Log


import android.provider.Settings
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close


@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NotificationScreen(onBack: () -> Unit) {
    val context = LocalContext.current
    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (!isGranted) {
            Toast.makeText(context, "Notification permission denied", Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(context, "Notification permission granted", Toast.LENGTH_SHORT).show()
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
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
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp)
        ) {
            DailyReminderToggle(
                onRequestPermission = {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                        permissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
                    }
                }
            )


        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun DailyReminderToggle(
    onRequestPermission: () -> Unit
) {
    val context = LocalContext.current
    val sharedPreferences = context.getSharedPreferences("notification_prefs", Context.MODE_PRIVATE)

    // Load saved state from SharedPreferences
    var isChecked by remember {
        mutableStateOf(sharedPreferences.getBoolean("is_notification_enabled", false))
    }
    var selectedTime by remember {
        val savedTime = sharedPreferences.getString("notification_time", "12:00")
        mutableStateOf(LocalTime.parse(savedTime ?: "12:00", DateTimeFormatter.ofPattern("HH:mm")))
    }

    // Save state when it changes
    LaunchedEffect(isChecked, selectedTime) {
        with(sharedPreferences.edit()) {
            putBoolean("is_notification_enabled", isChecked)
            putString("notification_time", selectedTime.format(DateTimeFormatter.ofPattern("HH:mm")))
            apply()
            Log.d("NotificationScreen", "Saved state: isChecked=$isChecked, time=$selectedTime")
        }
    }

    val formatter = if (DateFormat.is24HourFormat(context))
        DateTimeFormatter.ofPattern("HH:mm")
    else
        DateTimeFormatter.ofPattern("hh:mm a")

    // Check permissions
    val hasNotificationPermission = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
        ContextCompat.checkSelfPermission(context, Manifest.permission.POST_NOTIFICATIONS) == PackageManager.PERMISSION_GRANTED
    } else {
        true
    }
    val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
    val hasExactAlarmPermission = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
        alarmManager.canScheduleExactAlarms()
    } else {
        true
    }

    var showTimeSheet by remember { mutableStateOf(false) }
    var pendingSchedule by remember { mutableStateOf(false) }

    val exactAlarmLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { _ ->
        if (pendingSchedule) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S && alarmManager.canScheduleExactAlarms()) {
                scheduleNotification(context, selectedTime)
                Log.d("NotificationScreen", "Notification scheduled after enabling exact alarms")
            } else {
                Toast.makeText(context, "Exact alarm permission not granted", Toast.LENGTH_SHORT).show()
            }
            pendingSchedule = false
        }
    }

    Column {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
                .toggleable(value = isChecked, onValueChange = { newValue ->
                    if (newValue && Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU && !hasNotificationPermission) {
                        onRequestPermission()
                    }
                    isChecked = newValue
                    if (newValue && hasNotificationPermission && hasExactAlarmPermission) {
                        Log.d("NotificationScreen", "Scheduling notification for ${selectedTime.format(formatter)}")
                        scheduleNotification(context, selectedTime)
                    } else if (newValue && hasNotificationPermission && !hasExactAlarmPermission) {
                        Log.d("NotificationScreen", "Requesting exact alarm permission")
                        Toast.makeText(context, "Please enable exact alarms in settings", Toast.LENGTH_SHORT).show()
                        val intent = Intent(Settings.ACTION_REQUEST_SCHEDULE_EXACT_ALARM)
                        exactAlarmLauncher.launch(intent)
                        pendingSchedule = true
                    } else {
                        Log.d("NotificationScreen", "Canceling notification")
                        cancelNotification(context)
                    }
                }),
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
                if (isChecked && hasNotificationPermission && hasExactAlarmPermission) {
                    Log.d("NotificationScreen", "Rescheduling notification for ${selectedTime.format(formatter)}")
                    scheduleNotification(context, selectedTime)
                } else if (isChecked && hasNotificationPermission && !hasExactAlarmPermission) {
                    Log.d("NotificationScreen", "Requesting exact alarm permission for reschedule")
                    Toast.makeText(context, "Please enable exact alarms in settings", Toast.LENGTH_SHORT).show()
                    val intent = Intent(Settings.ACTION_REQUEST_SCHEDULE_EXACT_ALARM)
                    exactAlarmLauncher.launch(intent)
                    pendingSchedule = true
                }
            }
        )
    }
}

@RequiresApi(Build.VERSION_CODES.O)
private fun scheduleNotification(context: Context, time: LocalTime) {
    val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
    val intent = Intent(context, NotificationReceiver::class.java).apply {
        putExtra("title", "Daily Reminder")
        putExtra("message", "Time to check your Salah schedule!")
    }
    val pendingIntent = PendingIntent.getBroadcast(
        context,
        0,
        intent,
        PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
    )

    // Calculate trigger time
    val now = LocalDateTime.now()
    var triggerTime = LocalDateTime.of(now.toLocalDate(), time)
    if (triggerTime.isBefore(now) || triggerTime.isEqual(now)) {
        triggerTime = triggerTime.plusDays(1)
    }
    val triggerMillis = triggerTime.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli()
    Log.d("NotificationScreen", "Calculated trigger time: ${triggerTime.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME)} ($triggerMillis)")

    // Create notification channel before scheduling
    NotificationHelper.createNotificationChannel(context)

    try {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            if (alarmManager.canScheduleExactAlarms()) {
                alarmManager.setExactAndAllowWhileIdle(
                    AlarmManager.RTC_WAKEUP,
                    triggerMillis,
                    pendingIntent
                )
                Log.d("NotificationScreen", "Exact alarm scheduled at $triggerMillis")
            } else {
                Log.e("NotificationScreen", "Cannot schedule exact alarms")
                Toast.makeText(context, "Exact alarm permission not granted", Toast.LENGTH_SHORT).show()
                return
            }
        } else {
            alarmManager.setExact(
                AlarmManager.RTC_WAKEUP,
                triggerMillis,
                pendingIntent
            )
            Log.d("NotificationScreen", "Alarm scheduled at $triggerMillis")
        }
    } catch (e: SecurityException) {
        Log.e("NotificationScreen", "Failed to schedule alarm: ${e.message}")
        Toast.makeText(context, "Failed to schedule notification: ${e.message}", Toast.LENGTH_SHORT).show()
    }
}

private fun cancelNotification(context: Context) {
    val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
    val intent = Intent(context, NotificationReceiver::class.java)
    val pendingIntent = PendingIntent.getBroadcast(
        context,
        0,
        intent,
        PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
    )
    alarmManager.cancel(pendingIntent)
    Log.d("NotificationScreen", "Notification canceled")
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
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Icon(
                    imageVector = Icons.Filled.Close,
                    contentDescription = "Close",
                    modifier = Modifier.clickable { onDismiss() }
                )
                Icon(
                    imageVector = Icons.Filled.Check,
                    contentDescription = "Confirm",
                    modifier = Modifier.clickable {
                        val finalHour = if (is24h) {
                            selectedHour
                        } else {
                            if (isAm) if (selectedHour == 12) 0 else selectedHour
                            else if (selectedHour == 12) 12 else selectedHour + 12
                        }
                        onConfirm(LocalTime.of(finalHour, selectedMinute))
                    }
                )
            }

            Spacer(Modifier.height(24.dp))

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
    val visibleCount = 3

    val state = rememberLazyListState(
        initialFirstVisibleItemIndex = values.indexOf(selected) + 1000 * values.size
    )
    val flingBehavior = rememberSnapFlingBehavior(state)

    Box(
        modifier = Modifier
            .height(itemHeight * visibleCount)
            .width(70.dp),
        contentAlignment = Alignment.Center
    ) {
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

    LaunchedEffect(state.isScrollInProgress) {
        if (!state.isScrollInProgress) {
            val centerIndex = state.firstVisibleItemIndex + 1
            val value = values[centerIndex % values.size]
            onSelected(value)
        }
    }
}

