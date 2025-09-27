package com.example.salahsync.ui.Screens
import android.R.attr.contentDescription
import android.graphics.Color.rgb
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
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
import androidx.compose.foundation.lazy.grid.items // CHANGED: Explicitly import the correct 'items' for LazyVerticalGrid
import androidx.compose.ui.layout.ContentScale
import android.view.SoundEffectConstants
import androidx.compose.ui.platform.LocalView
import androidx.navigation.NavController


// 🎨 Colors

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn

import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items

import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.foundation.lazy.grid.items // CHANGED: Explicitly import the correct 'items' for LazyVerticalGrid

import android.util.Log // ADDED: Import Log for debugging // Why: To log UI interactions and state changes

// 🎨 Colors
private val PrimaryBlue = Color(0xFF007AFF)
private val BackgroundLightGray = Color(0xFFF3F5F8)
private val CardBackgroundGray = Color(0xFFF5F5F5)

// Updated: Removed static val prayerStatusImages = mapOf(...). Why: Hardcoded keys (e.g., "done") don't match prayer names (e.g., "Fajr"), causing fallback to invisible R.drawable.ic_launcher_background; now use dynamic ViewModel state for correct mapping after save.

@Composable
fun PrayerList(
    prayers: List<PrayerTilesData>,
    statusImages: Map<String, Int>, // Updated: Renamed parameter to statusImages for clarity; now expects dynamic map from ViewModel (prayerName → statusRes).
    statusColors: Map<String, Color>, // ADDED: Parameter for status colors // Why: For dynamic icon tinting (e.g., Exempted in purple)
    onPrayerClick: (PrayerTilesData) -> Unit
) {
    val view = LocalView.current
    LazyColumn {
        items(prayers) { prayer ->
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 16.dp, start = 10.dp, end = 10.dp)
                    .height(85.dp)
                    .clip(RoundedCornerShape(18.dp))
                    .clickable {
                        onPrayerClick(prayer)
                        view.playSoundEffect(SoundEffectConstants.CLICK)
                        // ADDED: Log prayer click // Why: Debug which prayer is clicked
                        Log.d("PrayerList", "Clicked prayer: ${prayer.name}")
                    }
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(MaterialTheme.colorScheme.surface) // ✅ dynamic background
                        .padding(vertical = 12.dp, horizontal = 16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Image(
                        painter = painterResource(id = prayer.iconRes),
                        contentDescription = prayer.name,
                        colorFilter = ColorFilter.tint(PrimaryBlue),
                        modifier = Modifier.size(60.dp),
                        contentScale = ContentScale.Fit
                    )
                    Spacer(modifier = Modifier.width(12.dp))
                    Text(
                        text = prayer.name,
                        modifier = Modifier.weight(1f),
                        fontSize = 20.sp,
                        color = MaterialTheme.colorScheme.onSurface // ✅ adapts to dark mode
                    )
                    val statusIcon = statusImages[prayer.name]
                    if (statusIcon != null) {
                        Image(
                            painter = painterResource(id = statusIcon),
                            contentDescription = "Prayer Status",
                            contentScale = ContentScale.Fit,
                            // CHANGED: Apply dynamic color tint from ViewModel // Why: Ensures correct tinting (e.g., purple for Exempted)
                            colorFilter = ColorFilter.tint(statusColors[prayer.name] ?: Color.Transparent),
                            modifier = Modifier.size(32.dp)
                        )
                    } else {
                        // Keeps size but stays transparent
                        Box(
                            modifier = Modifier
                                .size(32.dp)
                                .background(Color.Transparent)
                        )
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PrayerScreen(
    value: LocalDate,
    viewModel: PrayerScreenViewModel
) {
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    val coroutineScope = rememberCoroutineScope()
    var selectedPrayer by remember { mutableStateOf<PrayerTilesData?>(null) }
    val statusImages by viewModel.prayerStatusImages // Updated: Observe the dynamic state from ViewModel to get latest status icons after load/save.
    val prayers by viewModel.prayers // NEW: Observe prayers from ViewModel
    // ADDED: Observe userGender // Why: Determines whether to show Exempted (female) or In Jamaat (male) in PrayerStatusGrid
    val gender by viewModel.userGender

    // REPLACEMENT START: Add this to reload gender from DB when the screen enters composition (e.g., after returning from settings).
    LaunchedEffect(Unit) {
        viewModel.loadGender()
    }
    // REPLACEMENT END

    LaunchedEffect(value) {
        viewModel.loadPrayers(value)
        // Updated: Removed viewModel.loadStats(value). Why: Fixes "Unresolved reference 'loadStats'" error—original loadStats was replaced by loadStatsForPeriod in ViewModel for tab-based period loading; this call is redundant as StatsScreen loads its own data independently. PrayerScreen only needs daily prayer data.
        // ADDED: Log gender in UI // Why: Debug if gender is correctly passed to UI
        Log.d("PrayerScreen", "Current gender: $gender")
    }
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(top = 0.dp)
            .background(MaterialTheme.colorScheme.background) // ✅ theme-based background
    ) {
        PrayerList(
            prayers = prayers, // CHANGED: Use prayers from ViewModel instead of undefined 'prayer'
            statusImages = statusImages, // Updated: Pass dynamic statusImages from ViewModel instead of static map. Why: Ensures selected status icons update and show visibly in the list after bottom sheet selection/save.
            statusColors = viewModel.statusColors.value, // ADDED: Pass statusColors // Why: For dynamic icon tinting (e.g., Exempted in purple)
            onPrayerClick = { clickedPrayer ->
                selectedPrayer = clickedPrayer
                coroutineScope.launch { sheetState.show() }
            }
        )
        if (sheetState.isVisible && selectedPrayer != null) {
            ModalBottomSheet(
                onDismissRequest = {
                    coroutineScope.launch { sheetState.hide() }
                },
                sheetState = sheetState,
                containerColor = MaterialTheme.colorScheme.surface // ✅ sheet color adapts to dark/light
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .fillMaxHeight(0.8f)
                        .padding(24.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Image(
                        painter = painterResource(id = selectedPrayer!!.iconRes),
                        contentDescription = selectedPrayer!!.name,
                        modifier = Modifier.size(64.dp),
                        colorFilter = ColorFilter.tint(PrimaryBlue),
                        contentScale = ContentScale.Fit
                    )
                    Text(
                        text = "How did you complete ${selectedPrayer?.name} today?",
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.onSurface // ✅ readable in both themes
                    )
                    PrayerStatusGrid(
                        selectedPrayer = selectedPrayer!!,
                        value = value,
                        viewModel = viewModel,
                        gender = gender, // ADDED: Pass gender // Why: Enables female-specific Exempted option in grid
                        onClose = {
                            coroutineScope.launch {
                                sheetState.hide() // 👈 plays hide animation smoothly
                            }.invokeOnCompletion {
                                if (!sheetState.isVisible) {
                                    selectedPrayer = null // reset after animation
                                }
                            }
                        }
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun PrayerStatusGrid(
    selectedPrayer: PrayerTilesData,
    value: LocalDate,
    viewModel: PrayerScreenViewModel,
    gender: String, // ADDED: Gender parameter // Why: Determines whether to show Exempted (female) or In Jamaat (male)
    onClose: () -> Unit
) {
    val coroutineScope = rememberCoroutineScope()
    // CHANGED: Replaced Menstruation with Exempted for females // Why: Matches requirement for female-specific status
    val options = listOf(
        Triple("Not Prayed", R.drawable.notprayed, Color(0xFF000000)),
        Triple("Prayed Late", R.drawable.prayedlate, Color(0xFFD64F73)),
        Triple("On Time", R.drawable.prayedontime, Color(0xFFFFD92E)),
        Triple(if (gender == "Woman") "Exempted" else "In Jamaat",
            if (gender == "Woman") R.drawable.track else R.drawable.jamat,
            if (gender == "Woman") Color(0xFF8B5CF6) else Color(0xFF1DD1A1))
    )
    // ADDED: Log options // Why: Debug which options are displayed based on gender
    Log.d("PrayerStatusGrid", "Gender: $gender, Options: ${options.map { it.first }}")
    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(20.dp),
        horizontalArrangement = Arrangement.spacedBy(20.dp)
    ) {
        items(options) { (title, icon, tint) ->
            Column(
                modifier = Modifier
                    .clip(RoundedCornerShape(12.dp))
                    .background(MaterialTheme.colorScheme.surface)
                    .clickable {
                        viewModel.savePrayerStatus(
                            selectedPrayer.name,
                            icon,
                            value,
                            icon
                        )
                        Log.d("PrayerStatusGrid", "Selected status: $title for ${selectedPrayer.name}")
                        coroutineScope.launch { onClose() }
                    }
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Image(
                    painter = painterResource(id = icon),
                    contentDescription = title,
                    modifier = Modifier.size(40.dp),
                    colorFilter = ColorFilter.tint(tint), // ✅ uses same color from your Triple
                    contentScale = ContentScale.Fit
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = title,
                    fontSize = 14.sp,
                    color = tint // ✅ matches icon color
                )
            }
        }
    }
}