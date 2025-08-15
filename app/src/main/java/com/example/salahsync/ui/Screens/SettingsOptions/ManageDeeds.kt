package com.example.salahsync.ui.Screens.SettingsOptions

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun ManageDeedsScreen(onBack: () -> Unit) {
    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        Text(text = "DataBackup")
        Button(onClick = onBack) {
            Text(text = "Back")
        }
    }
}