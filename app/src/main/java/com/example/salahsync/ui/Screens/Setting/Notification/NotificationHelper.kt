package com.example.salahsync.ui.Screens.Setting.Notification

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.example.salahsync.MainActivity
import com.example.salahsync.R



object NotificationHelper {

    private const val CHANNEL_ID = "daily_reminder_channel"
    private const val CHANNEL_NAME = "Daily Reminder"
    private const val CHANNEL_DESC = "Reminder to mark your Salah"

    @RequiresApi(Build.VERSION_CODES.O)
    fun createNotificationChannel(context: Context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                CHANNEL_ID,
                CHANNEL_NAME,
                NotificationManager.IMPORTANCE_HIGH
            ).apply {
                description = CHANNEL_DESC
            }
            val manager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            manager.createNotificationChannel(channel)
        }
    }

    fun showNotification(context: Context, title: String, message: String) {
        // ✅ Intent: when user taps → open MainActivity
        val activityIntent = Intent(context, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            putExtra("navigateTo", "prayer") // 👈 custom extra so MainActivity knows where to go
        }

        val activityPendingIntent = PendingIntent.getActivity(
            context,
            0,
            activityIntent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        // ✅ Build the notification
        val builder = NotificationCompat.Builder(context, CHANNEL_ID)
            .setSmallIcon(R.drawable.next) // ⚠️ use proper small icon
            .setContentTitle(title)
            .setContentText(message)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setContentIntent(activityPendingIntent) // 👈 tap → open MainActivity
            .setAutoCancel(true)

        // ✅ Permission check before showing notification
        val notificationManager = NotificationManagerCompat.from(context)
        if (notificationManager.areNotificationsEnabled()) {
            try {
                notificationManager.notify(System.currentTimeMillis().toInt(), builder.build())
                Log.d("NotificationHelper", "Notification shown: $title - $message")
            } catch (e: SecurityException) {
                // 👇 catches case where POST_NOTIFICATIONS is denied on Android 13+
                Log.e("NotificationHelper", "Notification permission denied: ${e.message}")
            }
        } else {
            Log.e("NotificationHelper", "Notifications are disabled by the user")
        }
    }
}