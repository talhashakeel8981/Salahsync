package com.example.salahsync.ui.Screens.Setting.Notification
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi

class NotificationReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        Log.d("NotificationReceiver", "Received broadcast for notification")
        val title = intent.getStringExtra("title") ?: "Daily Reminder"
        val message = intent.getStringExtra("message") ?: "Time to check your Salah schedule!"

        NotificationHelper.createNotificationChannel(context)
        NotificationHelper.showNotification(context, title, message)
    }
}

    @RequiresApi(Build.VERSION_CODES.O)
    private fun scheduleNextNotification(context: Context, time: java.time.LocalTime) {
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as android.app.AlarmManager
        val intent = Intent(context, NotificationReceiver::class.java).apply {
            putExtra("title", "Daily Reminder")
            putExtra("message", "Time to Mark your Today's Salah")
        }
        val pendingIntent = android.app.PendingIntent.getBroadcast(
            context,
            0,
            intent,
            android.app.PendingIntent.FLAG_UPDATE_CURRENT or android.app.PendingIntent.FLAG_IMMUTABLE
        )

        val now = java.time.LocalDateTime.now()
        var triggerTime = java.time.LocalDateTime.of(now.toLocalDate(), time).plusDays(1)
        val triggerMillis = triggerTime.atZone(java.time.ZoneId.systemDefault()).toInstant().toEpochMilli()

        try {
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.S) {
                if (alarmManager.canScheduleExactAlarms()) {
                    alarmManager.setExactAndAllowWhileIdle(
                        android.app.AlarmManager.RTC_WAKEUP,
                        triggerMillis,
                        pendingIntent
                    )
                    Log.d("NotificationReceiver", "Next notification scheduled at $triggerMillis")
                } else {
                    Log.e("NotificationReceiver", "Cannot schedule exact alarms")
                }
            } else {
                alarmManager.setExact(
                    android.app.AlarmManager.RTC_WAKEUP,
                    triggerMillis,
                    pendingIntent
                )
                Log.d("NotificationReceiver", "Next notification scheduled at $triggerMillis")
            }
        } catch (e: SecurityException) {
            Log.e("NotificationReceiver", "Failed to schedule next alarm: ${e.message}")
        }
    }
