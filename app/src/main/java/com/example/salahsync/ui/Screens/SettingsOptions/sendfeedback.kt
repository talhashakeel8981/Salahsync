package com.example.salahsync.ui.Screens.SettingsOptions

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun EmailFeedback(onBack:()->Unit)
{
    Column (
        modifier = Modifier.fillMaxSize()
    )
    {
        Text(text = "Email feedback Screen")
        Button(onClick = onBack) {
            Text(text = "Back")
        }
    }
}