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


// NotificationScreen: Main composable for the notifications settings screen
// Requires API O for time APIs and opts into experimental Material 3 features
@RequiresApi(Build.VERSION_CODES.O)
// This annotation allows use of experimental Material 3 APIs like certain modifiers or composables
@OptIn(ExperimentalMaterial3Api::class)
// Marks this function as a Composable, so it can be used in Compose UI and recomposes on state changes
@Composable
// Defines the function with a parameter: onBack lambda to navigate back when called
fun NotificationScreen(onBack: () -> Unit) {
    // Retrieves the current Android context using Compose's LocalContext for system services like toasts
    val context = LocalContext.current
    // Creates a launcher for requesting runtime permissions; remember ensures it survives recompositions
    val permissionLauncher = rememberLauncherForActivityResult(
        // Specifies the contract for requesting a single permission
        contract = ActivityResultContracts.RequestPermission()
        // Callback lambda: receives boolean if permission was granted
    ) { isGranted ->
        // If permission denied, show a short toast message using the context
        if (!isGranted) {
            Toast.makeText(context, "Notification permission denied", Toast.LENGTH_SHORT).show()
            // Else branch for granted permission
        } else {
            // Shows a confirmation toast for granted permission
            Toast.makeText(context, "Notification permission granted", Toast.LENGTH_SHORT).show()
        }
    }

    // Starts a Material 3 Scaffold for standard app layout (top bar, content padding, etc.)
    Scaffold(
        // Defines the top bar content as a lambda
        topBar = {
            // Creates a TopAppBar for the screen header
            TopAppBar(
                // Title content as a lambda
                title = {
                    // Horizontal Row to layout the title, filling max width
                    Row(
                        // Makes the row fill the full available width
                        modifier = Modifier.fillMaxWidth(),
                        // Centers children horizontally
                        horizontalArrangement = Arrangement.Center
                    ) {
                        // Displays the static text "Notifications" as the title
                        Text("Notifications")
                    }
                },
                // Navigation icon content as a lambda
                navigationIcon = {
                    // IconButton that calls onBack when clicked
                    IconButton(onClick = onBack) {
                        // Icon composable inside the button
                        Icon(
                            // Loads the left arrow drawable from resources as the painter
                            painter = painterResource(id = R.drawable.leftarrow),
                            // Accessibility description for screen readers
                            contentDescription = "Back",
                            // Sets icon tint to black
                            tint = MaterialTheme.colorScheme.onSurface
                        )
                    }
                }
            )
        }
        // Content lambda with innerPadding to avoid overlap with top bar/system UI
    ) { innerPadding ->
        // Vertical Column for the main screen content
        Column(
            // Starts modifier chain for the column
            modifier = Modifier
                // Fills the entire available size
                .fillMaxSize()
                // Applies padding from the scaffold to respect top bar
                .padding(innerPadding)
                // Adds 16.dp padding around the content for spacing
                .padding(16.dp)
        ) {
            // Calls the DailyReminderToggle sub-composable for the reminder UI
            DailyReminderToggle(
                // Passes a lambda for requesting notification permission
                onRequestPermission = {
                    // Checks if device is Android 13 (TIRAMISU) or higher where permission is needed
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                        // Launches the permission request for POST_NOTIFICATIONS
                        permissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
                    }
                }
            )


        }
    }
}

// DailyReminderToggle: Sub-composable for the daily reminder toggle and time selection
// Requires API O for LocalTime usage
@RequiresApi(Build.VERSION_CODES.O)
// Marks as Composable
@Composable
// Function with parameter for permission request lambda
fun DailyReminderToggle(
    onRequestPermission: () -> Unit
) {
    // Gets the current context
    val context = LocalContext.current
    // Initializes SharedPreferences for storing notification settings in private mode
    val sharedPreferences = context.getSharedPreferences("notification_prefs", Context.MODE_PRIVATE)

    // Load saved state from SharedPreferences
    // This comment indicates the following lines restore UI state (toggle enabled/disabled and selected time) from persistent storage.
    // It uses remember and mutableStateOf for reactive Compose state, defaulting to false (disabled) and 12:00 if no saved data.
    // This ensures the app remembers user choices across restarts or rotations.
    // Load saved state from SharedPreferences
    // Declares mutable boolean state for toggle, delegated for easy updates; remember persists across recompositions
    var isChecked by remember {
        // Initializes with saved boolean, default false if not set
        mutableStateOf(sharedPreferences.getBoolean("is_notification_enabled", false))
    }
    // Declares mutable LocalTime state for selected reminder time
    var selectedTime by remember {
        // Retrieves saved time string, default "12:00"
        val savedTime = sharedPreferences.getString("notification_time", "12:00")
        // Parses string to LocalTime using HH:mm format, falls back to 12:00 if null
        mutableStateOf(LocalTime.parse(savedTime ?: "12:00", DateTimeFormatter.ofPattern("HH:mm")))
    }

    // Save state when it changes
    // This comment precedes the side-effect that persists state changes to SharedPreferences automatically.
    // LaunchedEffect runs the lambda only when keys (isChecked, selectedTime) change, editing prefs asynchronously with apply().
    // Logs for debugging; ensures data is saved without blocking UI, only on mutations.
    // Save state when it changes
    // LaunchedEffect triggers on changes to isChecked or selectedTime for side effects
    LaunchedEffect(isChecked, selectedTime) {
        // Creates editor scope for SharedPreferences
        with(sharedPreferences.edit()) {
            // Stores the boolean toggle state
            putBoolean("is_notification_enabled", isChecked)
            // Formats and stores time as HH:mm string
            putString("notification_time", selectedTime.format(DateTimeFormatter.ofPattern("HH:mm")))
            // Applies changes asynchronously to disk
            apply()
            // Debug log of saved values
            Log.d("NotificationScreen", "Saved state: isChecked=$isChecked, time=$selectedTime")
        }
    }

    // Determines time display formatter based on device 24-hour preference
    val formatter = if (DateFormat.is24HourFormat(context))
    // 24-hour format: HH:mm (e.g., 14:30)
        DateTimeFormatter.ofPattern("HH:mm")
    else
    // 12-hour format: hh:mm a (e.g., 02:30 PM)
        DateTimeFormatter.ofPattern("hh:mm a")

    // Check permissions
    // This comment flags the permission checks for notifications and exact alarms, crucial for Android compliance.
    // hasNotificationPermission checks POST_NOTIFICATIONS on 13+ (true otherwise); hasExactAlarmPermission checks canScheduleExactAlarms on 12+ (true otherwise).
    // These booleans control scheduling logic, prompting users only when needed.
    // Check permissions
    // Checks if notification permission is granted; conditional for Android 13+
    val hasNotificationPermission = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
        // Uses ContextCompat for safe permission check
        ContextCompat.checkSelfPermission(context, Manifest.permission.POST_NOTIFICATIONS) == PackageManager.PERMISSION_GRANTED
    } else {
        // Pre-13: always granted (legacy)
        true
    }
    // Gets AlarmManager system service for alarm operations
    val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
    // Checks exact alarm permission; conditional for Android 12+
    val hasExactAlarmPermission = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
        // Queries AlarmManager for exact alarm capability
        alarmManager.canScheduleExactAlarms()
    } else {
        // Pre-12: always allowed
        true
    }

    // State for showing/hiding time picker sheet, initially hidden
    var showTimeSheet by remember { mutableStateOf(false) }
    // State flag for pending schedule after permission request, initially false
    var pendingSchedule by remember { mutableStateOf(false) }

    // Launcher for starting activities like exact alarm settings
    val exactAlarmLauncher = rememberLauncherForActivityResult(
        // Contract for starting an activity and getting result
        ActivityResultContracts.StartActivityForResult()
        // Callback on activity result (ignores details, re-checks permission)
    ) { _ ->
        // If schedule was pending
        if (pendingSchedule) {
            // Checks Android 12+ and if exact alarms now allowed
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S && alarmManager.canScheduleExactAlarms()) {
                // Schedules the notification with current time
                scheduleNotification(context, selectedTime)
                // Logs success after permission
                Log.d("NotificationScreen", "Notification scheduled after enabling exact alarms")
            } else {
                // Shows denial toast if still not granted
                Toast.makeText(context, "Exact alarm permission not granted", Toast.LENGTH_SHORT).show()
            }
            // Resets pending flag
            pendingSchedule = false
        }
    }

    // Main vertical Column for toggle UI
    Column {
        // Horizontal Row for the toggle layout
        Row(
            // Starts modifier chain
            modifier = Modifier
                // Fills full width
                .fillMaxWidth()
                // Adds 16.dp padding
                .padding(16.dp)
                // Makes the row toggleable, passing current value and change callback
                .toggleable(value = isChecked, onValueChange = { newValue ->
                    // If enabling and needs notification permission on 13+
                    if (newValue && Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU && !hasNotificationPermission) {
                        // Requests permission via passed lambda
                        onRequestPermission()
                    }
                    // Updates the toggle state
                    isChecked = newValue
                    // If enabling with all permissions
                    if (newValue && hasNotificationPermission && hasExactAlarmPermission) {
                        // Logs the scheduled time
                        Log.d("NotificationScreen", "Scheduling notification for ${selectedTime.format(formatter)}")
                        // Schedules the daily alarm
                        scheduleNotification(context, selectedTime)
                        // Else if notification ok but no exact alarm
                    } else if (newValue && hasNotificationPermission && !hasExactAlarmPermission) {
                        // Logs the permission request
                        Log.d("NotificationScreen", "Requesting exact alarm permission")
                        // Shows prompt toast
                        Toast.makeText(context, "Please enable exact alarms in settings", Toast.LENGTH_SHORT).show()
                        // Creates intent for exact alarm settings
                        val intent = Intent(Settings.ACTION_REQUEST_SCHEDULE_EXACT_ALARM)
                        // Launches the settings activity
                        exactAlarmLauncher.launch(intent)
                        // Sets pending flag for post-result scheduling
                        pendingSchedule = true
                    } else {
                        // Default: disabling the toggle
                        // Logs cancel
                        Log.d("NotificationScreen", "Canceling notification")
                        // Cancels the existing alarm
                        cancelNotification(context)
                    }
                }),
            // Spaces children between left and right
            horizontalArrangement = Arrangement.SpaceBetween,
            // Vertically centers children
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Left: Label text with body large style
            Text("Daily reminder", style = MaterialTheme.typography.bodyLarge)
            // Right: Switch component, checked based on state; null onChange as toggleable handles it
            Switch(checked = isChecked, onCheckedChange = null)
        }

        // Conditional: if toggle is enabled, show time selection row
        if (isChecked) {
            // Horizontal Row for time display and tap
            Row(
                // Starts modifier
                modifier = Modifier
                    // Full width
                    .fillMaxWidth()
                    // Clickable to show time sheet
                    .clickable { showTimeSheet = true }
                    // 16.dp padding
                    .padding(16.dp),
                // Spaces between label and time
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                // Left: "Time" label
                Text("Time")
                // Right: Formatted current time display
                Text(selectedTime.format(formatter))
            }
        }
    }

    // Conditional: if sheet should show, render the bottom sheet
    if (showTimeSheet) {
        // Calls TimePickerBottomSheet with props
        TimePickerBottomSheet(
            // Passes current selected time as initial
            initialTime = selectedTime,
            // Dismiss lambda hides the sheet
            onDismiss = { showTimeSheet = false },
            // Confirm lambda processes new time
            onConfirm = {
                // Updates state with new time
                selectedTime = it
                // Hides sheet
                showTimeSheet = false
                // If enabled with all permissions, reschedule
                if (isChecked && hasNotificationPermission && hasExactAlarmPermission) {
                    // Logs reschedule
                    Log.d("NotificationScreen", "Rescheduling notification for ${selectedTime.format(formatter)}")
                    // Calls schedule with new time
                    scheduleNotification(context, selectedTime)
                    // Else if needs exact alarm
                } else if (isChecked && hasNotificationPermission && !hasExactAlarmPermission) {
                    // Logs request
                    Log.d("NotificationScreen", "Requesting exact alarm permission for reschedule")
                    // Shows prompt
                    Toast.makeText(context, "Please enable exact alarms in settings", Toast.LENGTH_SHORT).show()
                    // Settings intent
                    val intent = Intent(Settings.ACTION_REQUEST_SCHEDULE_EXACT_ALARM)
                    // Launches
                    exactAlarmLauncher.launch(intent)
                    // Sets pending
                    pendingSchedule = true
                }
            }
        )
    }
}

// scheduleNotification: Private function to set up a daily alarm for the reminder
// Requires API O for LocalDateTime calculations
@RequiresApi(Build.VERSION_CODES.O)
// Private to limit access outside this file
private fun scheduleNotification(context: Context, time: LocalTime) {
    // Gets AlarmManager for scheduling
    val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
    // Creates broadcast intent targeting NotificationReceiver (assumed BroadcastReceiver elsewhere)
    val intent = Intent(context, NotificationReceiver::class.java).apply {
        // Adds title extra for the notification
        putExtra("title", "Daily Reminder")
        // Adds message extra
        putExtra("message", "Time to check your Salah schedule!")
    }
    // Builds PendingIntent for broadcast: request 0, updates current, immutable for security
    val pendingIntent = PendingIntent.getBroadcast(
        context,
        0,
        intent,
        PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
    )

    // Calculate trigger time
    // This comment introduces the timestamp calculation for the next daily slot.
    // Combines current date with target time; advances to tomorrow if past/now to avoid immediate fire.
    // Converts to epoch millis in system timezone; logs for debug to verify timing.
    // Calculate trigger time
    // Current date-time
    val now = LocalDateTime.now()
    // Combines today's date with target time
    var triggerTime = LocalDateTime.of(now.toLocalDate(), time)
    // If trigger is before or equal to now, add one day
    if (triggerTime.isBefore(now) || triggerTime.isEqual(now)) {
        triggerTime = triggerTime.plusDays(1)
    }
    // Converts to milliseconds since epoch in system default zone
    val triggerMillis = triggerTime.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli()
    // Debug log of calculated time and millis
    Log.d("NotificationScreen", "Calculated trigger time: ${triggerTime.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME)} ($triggerMillis)")

    // Create notification channel before scheduling
    // This comment notes the essential pre-step for Android 8+: creates channel via helper (assumed external) for notification display.
    // Ensures channel exists before alarm triggers, preventing post failures.
    // Create notification channel before scheduling
    // Calls helper to create/retrieve channel
    NotificationHelper.createNotificationChannel(context)

    // Try-catch for potential SecurityException (e.g., permission denial)
    try {
        // Conditional for Android 12+ exact alarm handling
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            // If exact alarms allowed
            if (alarmManager.canScheduleExactAlarms()) {
                // Sets exact alarm, allows while idle/doze for reliability
                alarmManager.setExactAndAllowWhileIdle(
                    // RTC_WAKEUP: real-time clock, wakes device if needed
                    AlarmManager.RTC_WAKEUP,
                    // Trigger at calculated millis
                    triggerMillis,
                    // PendingIntent to trigger
                    pendingIntent
                )
                // Logs success
                Log.d("NotificationScreen", "Exact alarm scheduled at $triggerMillis")
            } else {
                // Error log if not allowed
                Log.e("NotificationScreen", "Cannot schedule exact alarms")
                // User toast
                Toast.makeText(context, "Exact alarm permission not granted", Toast.LENGTH_SHORT).show()
                // Early return to skip scheduling
                return
            }
        } else {
            // Pre-12: uses legacy exact set
            alarmManager.setExact(
                // RTC_WAKEUP
                AlarmManager.RTC_WAKEUP,
                // Trigger millis
                triggerMillis,
                // PendingIntent
                pendingIntent
            )
            // Logs success
            Log.d("NotificationScreen", "Alarm scheduled at $triggerMillis")
        }
        // Catches SecurityException for permission issues
    } catch (e: SecurityException) {
        // Error log with message
        Log.e("NotificationScreen", "Failed to schedule alarm: ${e.message}")
        // Shows error toast to user
        Toast.makeText(context, "Failed to schedule notification: ${e.message}", Toast.LENGTH_SHORT).show()
    }
}

// cancelNotification: Private function to remove the scheduled alarm
private fun cancelNotification(context: Context) {
    // Gets AlarmManager
    val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
    // Rebuilds intent for receiver (no extras needed)
    val intent = Intent(context, NotificationReceiver::class.java)
    // Rebuilds matching PendingIntent with same flags
    val pendingIntent = PendingIntent.getBroadcast(
        context,
        0,
        intent,
        PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
    )
    // Cancels the alarm using the pending intent
    alarmManager.cancel(pendingIntent)
    // Debug log
    Log.d("NotificationScreen", "Notification canceled")
}

// TimePickerBottomSheet: Composable for modal time selection bottom sheet
// Requires API O and experimental Material 3
@RequiresApi(Build.VERSION_CODES.O)
// Opts into experimental APIs
@OptIn(ExperimentalMaterial3Api::class)
// Composable
@Composable
// Function with parameters for initial time and callbacks
fun TimePickerBottomSheet(
    initialTime: LocalTime,
    onDismiss: () -> Unit,
    onConfirm: (LocalTime) -> Unit
) {
    // Gets context
    val context = LocalContext.current
    // Checks device 24-hour format preference
    val is24h = DateFormat.is24HourFormat(context)

    // Mutable state for selected hour, initialized from initial
    var selectedHour by remember { mutableStateOf(initialTime.hour) }
    // Mutable state for selected minute
    var selectedMinute by remember { mutableStateOf(initialTime.minute) }
    // Mutable state for AM/PM (true if hour < 12)
    var isAm by remember { mutableStateOf(initialTime.hour < 12) }

    // ModalBottomSheet: dismissible sheet from bottom; calls onDismiss on request (e.g., tap outside)
    ModalBottomSheet(onDismissRequest = { onDismiss() }) {
        // Main horizontal-centered Column with full width and 16.dp padding
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.fillMaxWidth().padding(16.dp)
        ) {
            // Top horizontal Row for close/confirm icons, full width
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                // Left: Close icon
                Icon(
                    imageVector = Icons.Filled.Close,
                    contentDescription = "Close",
                    // Clickable to dismiss
                    modifier = Modifier.clickable { onDismiss() }
                )
                // Right: Confirm icon
                Icon(
                    imageVector = Icons.Filled.Check,
                    contentDescription = "Confirm",
                    // Clickable: calculates final hour and confirms
                    modifier = Modifier.clickable {
                        // Final hour calculation
                        val finalHour = if (is24h) {
                            // 24h: use selected directly
                            selectedHour
                        } else {
                            // 12h: adjust for AM/PM
                            if (isAm) if (selectedHour == 12) 0 else selectedHour
                            else if (selectedHour == 12) 12 else selectedHour + 12
                        }
                        // Creates new LocalTime and calls confirm callback
                        onConfirm(LocalTime.of(finalHour, selectedMinute))
                    }
                )
            }

            // 24.dp vertical spacer for layout
            Spacer(Modifier.height(24.dp))

            // Horizontal Row for pickers, vertically centered
            Row(verticalAlignment = Alignment.CenterVertically) {
                // Hour InfiniteNumberPicker
                InfiniteNumberPicker(
                    // Values: 0-23 for 24h, 1-12 for 12h
                    values = if (is24h) (0..23).toList() else (1..12).toList(),
                    // Selected value, adjusted for 12h display (0/12 as 12)
                    selected = if (is24h) selectedHour else (selectedHour % 12).let { if (it == 0) 12 else it },
                    // Updates hour state on selection
                    onSelected = { selectedHour = it }
                )

                // Colon separator: large headline style, horizontal padding
                Text(":", style = MaterialTheme.typography.headlineMedium, modifier = Modifier.padding(horizontal = 8.dp))

                // Minute InfiniteNumberPicker
                InfiniteNumberPicker(
                    // Values 0-59
                    values = (0..59).toList(),
                    // Current minute
                    selected = selectedMinute,
                    // Updates minute state
                    onSelected = { selectedMinute = it }
                )

                // Conditional: if not 24h, show AM/PM column
                if (!is24h) {
                    // Vertical Column for AM/PM
                    Column {
                        // AM text
                        Text(
                            "AM",
                            // Primary color if selected, else surface
                            color = if (isAm) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface,
                            // Clickable to set AM, with padding
                            modifier = Modifier.clickable { isAm = true }.padding(8.dp)
                        )
                        // PM text
                        Text(
                            "PM",
                            // Primary if selected
                            color = if (!isAm) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface,
                            // Clickable to set PM, padded
                            modifier = Modifier.clickable { isAm = false }.padding(8.dp)
                        )
                    }
                }
            }
        }
    }
}

// InfiniteNumberPicker: Composable for wheel-style infinite scrolling number selector
@Composable
// Function with parameters for values list, current selected, and update callback
fun InfiniteNumberPicker(
    values: List<Int>,
    selected: Int,
    onSelected: (Int) -> Unit
) {
    // Fixed height per item (50.dp)
    val itemHeight = 50.dp
    // Number of visible items (3: above, center, below)
    val visibleCount = 3

    // LazyListState: remembers scroll position; initial index simulates infinite by offsetting many cycles
    val state = rememberLazyListState(
        initialFirstVisibleItemIndex = values.indexOf(selected) + 1000 * values.size
    )
    // Snapping fling behavior for smooth wheel snapping
    val flingBehavior = rememberSnapFlingBehavior(state)

    // Outer Box: container sized for visible items, 70.dp wide, centered content
    Box(
        modifier = Modifier
            .height(itemHeight * visibleCount)
            .width(70.dp),
        contentAlignment = Alignment.Center
    ) {
        // Inner Box: overlay for center divider
        Box(
            modifier = Modifier
                .matchParentSize()
                .padding(horizontal = 4.dp)
                .height(itemHeight)
                .align(Alignment.Center),
        ) {
            // Primary color divider (2.dp thick) as selector highlight for center item
            Divider(color = MaterialTheme.colorScheme.primary, thickness = 2.dp)
        }

        // LazyColumn: vertical scrollable list filling the box
        LazyColumn(
            // Uses the state for scroll control
            state = state,
            // Applies snapping on fling
            flingBehavior = flingBehavior,
            // Full size modifier
            modifier = Modifier.matchParentSize(),
            // Centers items horizontally
            horizontalAlignment = Alignment.CenterHorizontally,
            // Centers vertically
            verticalArrangement = Arrangement.Center
        ) {
            // Generates items: 3000 full cycles for infinite scroll illusion
            items(3000 * values.size) { index ->
                // Cycles value using modulo for repetition
                val value = values[index % values.size]
                // Text for the number
                Text(
                    // Pads to 2 digits (e.g., 05)
                    text = value.toString().padStart(2, '0'),
                    // Style: headline for center item (firstVisible +1), else body large
                    style = if (index == state.firstVisibleItemIndex + 1)
                        MaterialTheme.typography.headlineMedium
                    else
                        MaterialTheme.typography.bodyLarge,
                    // Modifier: fixed height, vertically centered
                    modifier = Modifier
                        .height(itemHeight)
                        .wrapContentHeight(Alignment.CenterVertically)
                )
            }
        }
    }

    // LaunchedEffect: runs when scroll progress changes; updates selection when stopped
    LaunchedEffect(state.isScrollInProgress) {
        // If scroll has stopped
        if (!state.isScrollInProgress) {
            // Calculates center index (first visible +1)
            val centerIndex = state.firstVisibleItemIndex + 1
            // Gets value at center via modulo
            val value = values[centerIndex % values.size]
            // Calls callback to update parent state
            onSelected(value)
        }
    }
}
