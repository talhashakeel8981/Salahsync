package com.example.salahsync.ui.Screens.Setting.Notification

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.example.salahsync.R

object NotificationHelper {
    private const val CHANNEL_ID = "daily_reminder_channel"
    private const val CHANNEL_NAME = "Daily Reminder"
    private const val CHANNEL_DESCRIPTION = "Notifications for daily reminders"

    fun createNotificationChannel(context: Context) {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                CHANNEL_ID,
                CHANNEL_NAME,
                NotificationManager.IMPORTANCE_DEFAULT
            ).apply {
                description = CHANNEL_DESCRIPTION
            }
            val notificationManager: NotificationManager =
                context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
            Log.d("NotificationHelper", "Notification channel created: $CHANNEL_ID")
        }
    }

    fun showNotification(context: Context, title: String, message: String) {
        Log.d("NotificationHelper", "Attempting to show notification: $title - $message")
        val builder = NotificationCompat.Builder(context, CHANNEL_ID)
            .setSmallIcon(R.drawable.home)
            .setContentTitle(title)
            .setContentText(message)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setAutoCancel(true)

        with(NotificationManagerCompat.from(context)) {
            try {
                notify(1000, builder.build())
                Log.d("NotificationHelper", "Notification posted successfully")
            } catch (e: SecurityException) {
                Log.e("NotificationHelper", "Failed to post notification: ${e.message}")
            }
        }
    }
}