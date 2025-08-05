import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.salahsync.R
import com.example.salahsync.ui.Screens.Prayer

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import kotlinx.coroutines.launch

import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue

// Step 2: Composable to show list
@Composable
fun PrayerList(
    prayers: List<Prayer>,
    onPrayerClick: (Prayer) -> Unit
) {
    LazyColumn {
        items(prayers) { prayer ->
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp)
                    .clickable { onPrayerClick(prayer) }
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(text = prayer.name)
                    Text(text = prayer.time)
                }
            }
        }
    }
}

// Step 3: Main screen with bottom sheet
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PrayerScreen() {
    val sheetState = rememberModalBottomSheetState()
    val coroutineScope = rememberCoroutineScope()
    var selectedPrayer by remember { mutableStateOf<Prayer?>(null) }

    val prayers = listOf(
        Prayer("Fajr", "05:00 AM"),
        Prayer("Dhuhr", "12:30 PM"),
        Prayer("Asr", "03:45 PM"),
        Prayer("Maghrib", "06:30 PM"),
        Prayer("Isha", "08:00 PM")
    )

    Box(modifier = Modifier.fillMaxSize()) {
        PrayerList(prayers) { clickedPrayer ->
            selectedPrayer = clickedPrayer
            coroutineScope.launch { sheetState.show() }
        }

        if (sheetState.isVisible && selectedPrayer != null) {
            ModalBottomSheet(
                onDismissRequest = {
                    coroutineScope.launch { sheetState.hide() }
                },
                sheetState = sheetState
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(24.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    Text(
                        text = "How did you complete ${selectedPrayer?.name} today?",
                        style = MaterialTheme.typography.titleMedium
                    )

                    Row(
                        horizontalArrangement = Arrangement.spacedBy(12.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        IconButton(onClick = {
                            // TODO: handle Not Prayed
                            coroutineScope.launch { sheetState.hide() }
                        }) {
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Icon(Icons.Default.Close, contentDescription = "Not Prayed")
                                Text("Not", style = MaterialTheme.typography.labelSmall)
                            }
                        }

                        IconButton(onClick = {
                            // TODO: handle Late
                            coroutineScope.launch { sheetState.hide() }
                        }) {
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Icon(Icons.Default.Info, contentDescription = "Late")
                                Text("Late", style = MaterialTheme.typography.labelSmall)
                            }
                        }

                        IconButton(onClick = {
                            // TODO: handle On Time
                            coroutineScope.launch { sheetState.hide() }
                        }) {
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Icon(Icons.Default.Check, contentDescription = "On Time")
                                Text("On Time", style = MaterialTheme.typography.labelSmall)
                            }
                        }

                        IconButton(onClick = {
                            // TODO: handle In Jamaat
                            coroutineScope.launch { sheetState.hide() }
                        }) {
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Icon(Icons.Default.Home, contentDescription = "In Jamaat")
                                Text("Jamaat", style = MaterialTheme.typography.labelSmall)
                            }
                        }
                    }
                }
            }
        }}}
            @Preview(showSystemUi = true)
            @Composable
            fun PrayerTrackingScreenPreview() {
                PrayerScreen()
            }
