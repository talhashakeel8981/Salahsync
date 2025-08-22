package com.example.salahsync.ui.Screens
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.salahsync.R
import kotlinx.coroutines.launch
import java.time.LocalDate




@Composable
fun PrayerList(
    prayers: List<PrayerTilesData>,
    prayerStatusImages: Map<String, Int>,
    onPrayerClick: (PrayerTilesData) -> Unit
) {
    LazyColumn {
        items(prayers) { prayer ->
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 16.dp, start = 10.dp, end = 10.dp)
                    .height(85.dp)
                    .clip(RoundedCornerShape(18.dp))
                    .clickable { onPrayerClick(prayer) }
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Color.White)
                        .padding(vertical = 12.dp, horizontal = 16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Image(
                        painter = painterResource(id = prayer.iconRes),
                        contentDescription = prayer.name,
                        colorFilter = ColorFilter.tint(Color(0, 122, 255)),
                        modifier = Modifier.size(60.dp)
                    )
                    Spacer(modifier = Modifier.width(12.dp))
                    Text(
                        text = prayer.name,
                        modifier = Modifier.weight(1f),
                        fontSize = 20.sp
                    )
                    val statusIcon = prayerStatusImages[prayer.name] ?: R.drawable.ic_launcher_background
                    Image(
                        painter = painterResource(id = statusIcon),
                        contentDescription = "Prayer Status",
                        modifier = Modifier.size(32.dp)
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PrayerScreen(value: LocalDate, viewModel: PrayerScreenViewModel) {
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    val coroutineScope = rememberCoroutineScope()
    var selectedPrayer by remember { mutableStateOf<PrayerTilesData?>(null) }
    val prayerStatusImages by viewModel.prayerStatusImages
    val prayers = listOf(
        PrayerTilesData("Fajr", R.drawable.ic_fajr),
        PrayerTilesData("Dhuhr", R.drawable.ic_dhuhur),
        PrayerTilesData("Asr", R.drawable.ic_asr),
        PrayerTilesData("Maghrib", R.drawable.ic_maghrib),
        PrayerTilesData("Isha", R.drawable.ic_esha)
    )

    LaunchedEffect(value) {
        viewModel.loadPrayers(value)
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(top = 80.dp)
            .background(Color(243, 245, 248))
    ) {
        PrayerList(
            prayers,
            prayerStatusImages
        ) { clickedPrayer ->
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
                        .fillMaxHeight(0.8f)
                        .padding(24.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    Text(
                        text = "How did you complete ${selectedPrayer?.name} today?",
                        style = MaterialTheme.typography.titleMedium
                    )
                    // Not Prayed
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable {
                                viewModel.savePrayerStatus(
                                    selectedPrayer!!.name,
                                    R.drawable.notprayed,
                                    value,
                                    R.drawable.notprayed
                                )
                                coroutineScope.launch { sheetState.hide() }
                            }
                            .padding(vertical = 8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.notprayed),
                            contentDescription = "Not Prayed",
                            modifier = Modifier.size(24.dp),
                            colorFilter = ColorFilter.tint(Color.Black)
                        )
                        Spacer(modifier = Modifier.width(12.dp))
                        Text(text = "Not Prayed", fontSize = 16.sp)
                    }
                    // Prayed Late
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable {
                                viewModel.savePrayerStatus(
                                    selectedPrayer!!.name,
                                    R.drawable.prayedlate,
                                    value,
                                    R.drawable.prayedlate
                                )
                                coroutineScope.launch { sheetState.hide() }
                            }
                            .padding(vertical = 8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.prayedlate),
                            contentDescription = "Prayed Late",
                            modifier = Modifier.size(24.dp),
                            colorFilter = ColorFilter.tint(Color.Red)
                        )
                        Spacer(modifier = Modifier.width(12.dp))
                        Text(text = "Prayed Late", fontSize = 16.sp)
                    }
                    // On Time
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable {
                                viewModel.savePrayerStatus(
                                    selectedPrayer!!.name,
                                    R.drawable.prayedontime,
                                    value,
                                    R.drawable.prayedontime
                                )
                                coroutineScope.launch { sheetState.hide() }
                            }
                            .padding(vertical = 8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.prayedontime),
                            contentDescription = "On Time",
                            modifier = Modifier.size(24.dp),
                            colorFilter = ColorFilter.tint(Color.Yellow)
                        )
                        Spacer(modifier = Modifier.width(12.dp))
                        Text(text = "On Time", fontSize = 16.sp)
                    }
                    // In Jamaat
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable {
                                viewModel.savePrayerStatus(
                                    selectedPrayer!!.name,
                                    R.drawable.jamat,
                                    value,
                                    R.drawable.jamat
                                )
                                coroutineScope.launch { sheetState.hide() }
                            }
                            .padding(vertical = 8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.jamat),
                            contentDescription = "In Jamaat",
                            modifier = Modifier.size(24.dp),
                            colorFilter = ColorFilter.tint(Color.Green)
                        )
                        Spacer(modifier = Modifier.width(12.dp))
                        Text(text = "In Jamaat", fontSize = 16.sp)
                    }
                }
            }
        }
    }
}

//@Preview(showSystemUi = true)
//@Composable
//fun PrayerTrackingScreenPreview() {
//    PrayerScreen(selectedDate.value)
//}