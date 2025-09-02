package com.example.salahsync.ui.Screens.Setting.Notification
import android.app.AlarmManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import java.time.LocalDateTime
import java.time.LocalTime

import java.time.ZoneId
import java.time.format.DateTimeFormatter

class NotificationReceiver : BroadcastReceiver() {
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onReceive(context: Context, intent: Intent) {
        Log.d("NotificationReceiver", "Received broadcast for notification: ${intent.action}")
        val title = intent.getStringExtra("title") ?: "Daily Reminder"
        val message = intent.getStringExtra("message") ?: "Time to check your Salah schedule!"

        if (intent.action == Intent.ACTION_BOOT_COMPLETED) {
            // [CHANGE] Added boot handling to reschedule notifications if enabled
            rescheduleOnBoot(context)
        } else {
            NotificationHelper.createNotificationChannel(context)
            NotificationHelper.showNotification(context, title, message)
            // [CHANGE] Added check for saved time and scheduling of next day's notification
            val sharedPreferences = context.getSharedPreferences("notification_prefs", Context.MODE_PRIVATE)
            val savedTimeStr = sharedPreferences.getString("notification_time", "08:00") // Changed default to 08:00 for morning
            if (sharedPreferences.getBoolean("is_notification_enabled", false) && savedTimeStr != null) {
                val selectedTime = LocalTime.parse(savedTimeStr, DateTimeFormatter.ofPattern("HH:mm"))
                scheduleNextNotification(context, selectedTime)
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun scheduleNextNotification(context: Context, time: LocalTime) {
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val intent = Intent(context, NotificationReceiver::class.java).apply {
            putExtra("title", "Daily Reminder")
            putExtra("message", "Time to Mark your Today's Salah")
        }
        val pendingIntent = PendingIntent.getBroadcast(
            context,
            0,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val now = LocalDateTime.now()
        var triggerTime = LocalDateTime.of(now.toLocalDate(), time).plusDays(1)
        if (triggerTime.isBefore(now)) {
            triggerTime = triggerTime.plusDays(1)
        }
        val triggerMillis = triggerTime.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli()

        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                if (alarmManager.canScheduleExactAlarms()) {
                    alarmManager.setExactAndAllowWhileIdle(
                        AlarmManager.RTC_WAKEUP,
                        triggerMillis,
                        pendingIntent
                    )
                    Log.d("NotificationReceiver", "Next notification scheduled at $triggerMillis")
                } else {
                    Log.e("NotificationReceiver", "Cannot schedule exact alarms")
                }
            } else {
                alarmManager.setExact(
                    AlarmManager.RTC_WAKEUP,
                    triggerMillis,
                    pendingIntent
                )
                Log.d("NotificationReceiver", "Next notification scheduled at $triggerMillis")
            }
        } catch (e: SecurityException) {
            Log.e("NotificationReceiver", "Failed to schedule next alarm: ${e.message}")
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun rescheduleOnBoot(context: Context) {
        // [CHANGE] Added function to reschedule on boot using saved preferences
        val sharedPreferences = context.getSharedPreferences("notification_prefs", Context.MODE_PRIVATE)
        val isEnabled = sharedPreferences.getBoolean("is_notification_enabled", false)
        val savedTimeStr = sharedPreferences.getString("notification_time", "08:00")
        if (isEnabled && savedTimeStr != null) {
            val time = LocalTime.parse(savedTimeStr, DateTimeFormatter.ofPattern("HH:mm"))
            scheduleNextNotification(context, time)
            Log.d("NotificationReceiver", "Rescheduled notification on boot for $savedTimeStr")
        }
    }
}