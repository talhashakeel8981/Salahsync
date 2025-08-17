package com.example.salahsync.ui.Screens.SettingsOptions

import android.widget.Button
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun LoveSalahsync(onBack:()->Unit)
{
    Column (
        modifier = Modifier
            .fillMaxSize()
    )
    {
        Text(text = "RateUs Screen")
        Button(onClick = onBack){
            Text(text = "Back")
        }
    }
}