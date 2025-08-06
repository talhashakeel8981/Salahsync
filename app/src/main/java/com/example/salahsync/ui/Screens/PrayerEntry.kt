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
import com.example.salahsync.ui.Screens.TopBottom

// ✅ Composable to show the list of 5 prayers
@Composable
fun PrayerList(
    prayers: List<Prayer>,                         // Input: list of prayer names + times
    onPrayerClick: (Prayer) -> Unit                // Callback: when user taps a prayer
) {


//    TopBottom()
    LazyColumn {
        items(prayers) { prayer ->                 // For each prayer, show a card


                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 16.dp)
                            .clickable { onPrayerClick(prayer) } // When tapped, call the callback
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Text(text = prayer.name)      // Show prayer name (e.g. Fajr)
                        }
                    }
                }


        }
    }



// ✅ Main Composable screen where bottom sheet is handled
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PrayerScreen() {
    val sheetState = rememberModalBottomSheetState(
        skipPartiallyExpanded = true
    )  // BottomSheet state
    val coroutineScope = rememberCoroutineScope()     // To launch coroutines
    var selectedPrayer by remember { mutableStateOf<Prayer?>(null) } // Currently selected prayer

    // ✅ Hardcoded list of 5 daily prayers (can be dynamic later)
    val prayers = listOf(
        Prayer("Fajr"),
        Prayer("Dhuhr"),
        Prayer("Asr"),
        Prayer("Maghrib"),
        Prayer("Isha")
    )

    // ✅ Main container for the screen
    Box(modifier = Modifier.fillMaxSize()
        .padding(top =80.dp )
    ) {
        // ✅ Show prayer list
        PrayerList(prayers) { clickedPrayer ->
            selectedPrayer = clickedPrayer              // Set the tapped prayer
            coroutineScope.launch { sheetState.show() } // Open bottom sheet
        }

        // ✅ Show bottom sheet when user taps a prayer
        if (sheetState.isVisible && selectedPrayer != null) {
            ModalBottomSheet(
                onDismissRequest = {
                    coroutineScope.launch { sheetState.hide() } // Close bottom sheet
                },
                sheetState = sheetState
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .fillMaxHeight(0.8f) // 🔼 60% of screen height
                        .padding(24.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    // ✅ Title text: asks user about selected prayer
                    Text(
                        text = "How did you complete ${selectedPrayer?.name} today?",
                        style = MaterialTheme.typography.titleMedium
                    )


                    Row (
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable{
                                //To Do :Handle "Not Prayed"
                                coroutineScope.launch { sheetState.hide() }
                            }
                            .padding(vertical = 8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    )
                    {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = "Not Prayed",
                            tint = Color.Red,
                            modifier = Modifier.size(24.dp)
                        )
                        Spacer(modifier = Modifier.width(12.dp))
                        Text(text = "Not Prayed", fontSize = 16.sp)
                    }


                    // ✅ Option 2 — Prayed Late
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable {
                                // TODO: Handle "Late"
                                coroutineScope.launch { sheetState.hide() }
                            }
                            .padding(vertical = 8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.Info,
                            contentDescription = "Late",
                            tint = Color(0xFFFF9800),
                            modifier = Modifier.size(24.dp)
                        )
                        Spacer(modifier = Modifier.width(12.dp))
                        Text(text = "Prayed Late", fontSize = 16.sp)
                    }

                    // ✅ Option 3 — On Time
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable {
                                // TODO: Handle "On Time"
                                coroutineScope.launch { sheetState.hide() }
                            }
                            .padding(vertical = 8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.Check,
                            contentDescription = "On Time",
                            tint = Color.Green,
                            modifier = Modifier.size(24.dp)
                        )
                        Spacer(modifier = Modifier.width(12.dp))
                        Text(text = "On Time", fontSize = 16.sp)
                    }

                    // ✅ Option 4 — In Jamaat
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable {
                                // TODO: Handle "In Jamaat"
                                coroutineScope.launch { sheetState.hide() }
                            }
                            .padding(vertical = 8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.Home,
                            contentDescription = "In Jamaat",
                            tint = Color.Blue,
                            modifier = Modifier.size(24.dp)
                        )
                        Spacer(modifier = Modifier.width(12.dp))
                        Text(text = "In Jamaat", fontSize = 16.sp)
                    }
                }


                    }
                }
            }
        }

@Preview(showSystemUi = true)
@Composable
fun PrayerTrackingScreenPreview() {
    PrayerScreen()
}
