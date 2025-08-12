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


//  Composable to show the list of 5 prayers
@Composable
fun PrayerList(
    prayers: List<PrayerTilesData>, // Your existing prayer list
    prayerStatuses: Map<String, String>, // New: Map prayer name -> emoji for now
    onPrayerClick: (PrayerTilesData) -> Unit
) {
    LazyColumn {
        items(prayers) { prayer -> // For each prayer, show a card
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 16.dp, start = 10.dp, end = 10.dp)
                    .height(85.dp)
                    .clip(RoundedCornerShape(18.dp))

                    .clickable { onPrayerClick(prayer) } // When tapped, call the callback
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(color = Color.White)
                        .padding(vertical = 12.dp, horizontal = 16.dp),


                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Prayer icon
                    Image(
                        painter = painterResource(id = prayer.iconRes),
                        colorFilter = ColorFilter.tint(Color(0xFF2196F3)), // Material Blue 500
                        contentDescription = prayer.name,
                        modifier = Modifier.size(60.dp)
                    )

                    Spacer(modifier = Modifier.width(12.dp))

                    // Prayer name
                    Text(
                        text = prayer.name,
                        modifier = Modifier.weight(1f), // pushes status to the right
                        fontSize = 20.sp
                    )

                    // Status with slanted background
                    Surface(
                        shape = RowDesign(cornerRadius = 8.dp, slantWidth = 16.dp),
                        color = Color(186, 186, 191)
                    ) {
                        Text(
                            text = prayerStatuses[prayer.name] ?: "          ",
                            modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                            fontSize = 14.sp
                        )
                    }
                }
            }
        }
    }
}
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PrayerScreen(value: LocalDate) {
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    val coroutineScope = rememberCoroutineScope()

    var selectedPrayer by remember { mutableStateOf<PrayerTilesData?>(null) }

    // ⬅️ ✅ NEW — This stores the prayer status for each prayer
    var prayerStatuses by remember { mutableStateOf<Map<String, String>>(emptyMap()) }


    val prayers = listOf(
        PrayerTilesData("Fajr", R.drawable.ic_fajr),
        PrayerTilesData("Dhuhr", R.drawable.ic_dhuhur),
        PrayerTilesData("Asr", R.drawable.ic_asr),
        PrayerTilesData("Maghrib", R.drawable.ic_maghrib),
        PrayerTilesData("Isha", R.drawable.ic_esha)
    )

    //  Main container for the screen
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(top = 80.dp)
            .background(Color(243, 245, 248)) //
    ) {
        //  Pass statuses to PrayerList
        PrayerList(prayers, prayerStatuses) { clickedPrayer ->
            selectedPrayer = clickedPrayer
            coroutineScope.launch { sheetState.show() }
        }

        //  Show bottom sheet when user taps a prayer
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


                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable {
                                // ⬅ CHANGE — Update prayerStatuses before closing
                                prayerStatuses = prayerStatuses + (selectedPrayer!!.name to "❌")
                                coroutineScope.launch { sheetState.hide() }
                            }
                            .padding(vertical = 8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.notprayed),
                            contentDescription = "Not Prayed",
                            modifier = Modifier.size(24.dp)
                        )
                        Spacer(modifier = Modifier.width(12.dp))
                        Text(text = "Not Prayed", fontSize = 16.sp)
                    }


                    Row(

                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable {
                                // ⬅️ ✅ CHANGE
                                prayerStatuses = prayerStatuses + (selectedPrayer!!.name to "")
                                coroutineScope.launch { sheetState.hide() }
                            }

                            .padding(vertical = 8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.prayedlate) ,
                            contentDescription = "Prayed Late",
                            modifier = Modifier.size(24.dp)
                        )
                        Spacer(modifier = Modifier.width(12.dp))
                        Text(text = "Prayed Late", fontSize = 16.sp)
                    }


                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable {

                                prayerStatuses = prayerStatuses + (selectedPrayer!!.name to "✅")
                                coroutineScope.launch { sheetState.hide() }
                            }
                            .padding(vertical = 8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.prayedontime),
                            contentDescription = "Prayed On Time",
                            modifier = Modifier.size(24.dp)
                        )
                        Spacer(modifier = Modifier.width(12.dp))
                        Text(text = "On Time", fontSize = 16.sp)
                    }


                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable {
                                // ⬅️ ✅ CHANGE
                                prayerStatuses = prayerStatuses + (selectedPrayer!!.name to "🕌")
                                coroutineScope.launch { sheetState.hide() }
                            }
                            .padding(vertical = 8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.jamat) ,
                            contentDescription = "In Jamaat",
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


//@Preview(showSystemUi = true)
//@Composable
//fun PrayerTrackingScreenPreview() {
//    PrayerScreen(selectedDate.value)
//}