package com.example.salahsync.ui.Screens.SettingsOptions
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.salahsync.R
import com.example.salahsync.ui.Screens.Setting.Appearence.Themes


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppearenceScreen(
    onBack: () -> Unit,
    selectedTheme: Themes,
    onThemeChange: (Themes) -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Text("Appearance")
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
            Text("Choose Theme", style = MaterialTheme.typography.titleMedium)
            Spacer(modifier = Modifier.height(16.dp))

            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { onThemeChange(Themes.SYSTEM) }
                    .padding(8.dp)
            ) {
                RadioButton(
                    selected = selectedTheme == Themes.SYSTEM,
                    onClick = { onThemeChange(Themes.SYSTEM) }
                )
                Text("System", modifier = Modifier.padding(start = 8.dp))
            }

            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { onThemeChange(Themes.LIGHT) }
                    .padding(8.dp)
            ) {
                RadioButton(
                    selected = selectedTheme == Themes.LIGHT,
                    onClick = { onThemeChange(Themes.LIGHT) }
                )
                Text("Light", modifier = Modifier.padding(start = 8.dp))
            }

            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { onThemeChange(Themes.DARK) }
                    .padding(8.dp)
            ) {
                RadioButton(
                    selected = selectedTheme == Themes.DARK,
                    onClick = { onThemeChange(Themes.DARK) }
                )
                Text("Dark", modifier = Modifier.padding(start = 8.dp))
            }
        }
    }
}