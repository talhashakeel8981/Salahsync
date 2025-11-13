package com.example.salahsync.ui.widget

import android.content.Context
import android.graphics.Color
import androidx.compose.material3.Text
import androidx.glance.GlanceComposable
import androidx.glance.GlanceId
import androidx.glance.appwidget.GlanceAppWidget
import androidx.glance.layout.Column
import androidx.glance.layout.fillMaxSize
import androidx.glance.layout.padding
import androidx.glance.text.Text
import androidx.glance.unit.ColorProvider
import androidx.glance.unit.dp
import androidx.glance.background

class PrayerWidget : GlanceAppWidget() {

    override suspend fun provideGlance(context: Context, id: GlanceId) {
        GlanceComposable {
            Column(
                modifier = fillMaxSize()
                    .background(ColorProvider(Color.WHITE))
                    .padding(16.dp)
            ) {
                Text(text = "Prayer Times", color = ColorProvider(Color.BLACK))
                Text(text = "Fajr - 05:00 AM")
                Text(text = "Dhuhr - 12:30 PM")
                Text(text = "Asr - 04:15 PM")
                Text(text = "Maghrib - 06:45 PM")
                Text(text = "Isha - 08:15 PM")
            }
        }
    }
}
