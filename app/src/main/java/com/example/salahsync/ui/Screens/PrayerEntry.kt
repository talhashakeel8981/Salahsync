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
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.ui.text.font.FontWeight

// 🎨 Colors
private val PrimaryBlue = Color(0xFF007AFF)
private val BackgroundLightGray = Color(0xFFF3F5F8)
private val CardBackgroundGray = Color(0xFFF5F5F5)

// Updated: Removed static val prayerStatusImages = mapOf(...). Why: Hardcoded keys (e.g., "done") don't match prayer names (e.g., "Fajr"), causing fallback to invisible R.drawable.ic_launcher_background; now use dynamic ViewModel state for correct mapping after save.

@Composable
fun PrayerList(
    prayers: List<PrayerTilesData>,
    statusImages: Map<String, Int>,
    onPrayerClick: (PrayerTilesData) -> Unit
) {
    val view = LocalView.current
    val isDark = isSystemInDarkTheme()

    // Status color map (same as before)
    val statusResToTint = mapOf(
        R.drawable.notprayed to if (isDark) Color(0xFFB91C1C) else Color(0xFFEF4444),
        R.drawable.prayedlate to if (isDark) Color(0xFFE07B00) else Color(0xFFF59E0B),
        R.drawable.prayedontime to if (isDark) Color(0xFF1D4ED8) else Color(0xFF3B82F6),
        R.drawable.jamat to if (isDark) Color(0xFF15803D) else Color(0xFF22C55E),
        R.drawable.track to Color(0xFF8B5CF6)
    )

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(vertical = 12.dp)
    ) {
        items(prayers) { prayer ->

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 12.dp, vertical = 8.dp)
                    .height(110.dp)
                    .clip(RoundedCornerShape(18.dp))
                    .clickable {
                        onPrayerClick(prayer)
                        view.playSoundEffect(SoundEffectConstants.CLICK)
                        Log.d("PrayerList", "Clicked prayer: ${prayer.name}")
                    }
            ) {
                // 🆕 Background image for each prayer
                Image(
                    painter = painterResource(id = prayer.backgroundRes),
                    contentDescription = "${prayer.name} background",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.matchParentSize()
                )

                // Optional dim overlay to improve contrast
                Box(
                    modifier = Modifier
                        .matchParentSize()
                        .background(Color.Black.copy(alpha = 0.35f))
                )

                // Foreground content (your existing layout)
                Row(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(vertical = 12.dp, horizontal = 16.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Image(
                            painter = painterResource(id = prayer.iconRes),
                            contentDescription = prayer.name,
                            colorFilter = ColorFilter.tint(Color.White),
                            modifier = Modifier.size(60.dp),
                            contentScale = ContentScale.Fit
                        )
                        Spacer(modifier = Modifier.width(12.dp))
                        Text(
                            text = prayer.name,
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                    }

                    val statusIcon = statusImages[prayer.name]
                    Box(
                        modifier = Modifier
                            .size(32.dp)
                            .clip(CircleShape)
                            .background(Color.White.copy(alpha = 0.9f)) // ADDED: Solid white circular background for icon visibility
                    ) {
                        if (statusIcon != null) {
                            Image(
                                painter = painterResource(id = statusIcon),
                                contentDescription = "Prayer Status",
                                contentScale = ContentScale.Fit,
                                colorFilter = ColorFilter.tint(statusResToTint[statusIcon] ?: Color.Transparent),
                                modifier = Modifier
                                    .size(24.dp) // Slightly smaller to fit within the 32.dp circle
                                    .align(Alignment.Center)
                            )
                        }
                    }
                }
            }
        }
    }
}

// ---------------------------
// 🧩 PRAYER SCREEN
// ---------------------------

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    fun PrayerScreen(
        value: LocalDate,
        viewModel: PrayerScreenViewModel // Assuming you have this ViewModel
    ) {
        val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
        val coroutineScope = rememberCoroutineScope()
        var selectedPrayer by remember { mutableStateOf<PrayerTilesData?>(null) }

        val statusImages by viewModel.prayerStatusImages
        val prayers by viewModel.prayers
        val gender by viewModel.userGender

        LaunchedEffect(Unit) {
            viewModel.loadGender()
        }

        LaunchedEffect(value) {
            viewModel.loadPrayers(value)
            Log.d("PrayerScreen", "Current gender: $gender")
        }

        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
        ) {
            PrayerList(
                prayers = prayers,
                statusImages = statusImages,
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
                    containerColor = MaterialTheme.colorScheme.surface
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .fillMaxHeight(0.62f)
                            .padding(24.dp),
                        verticalArrangement = Arrangement.spacedBy(16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Image(
                            painter = painterResource(id = selectedPrayer!!.iconRes),
                            contentDescription = selectedPrayer!!.name,
                            modifier = Modifier.size(64.dp),
                            colorFilter = ColorFilter.tint(Color(0xFF1D4ED8)),
                            contentScale = ContentScale.Fit
                        )

                        Text(
                            text = "How did you complete ${selectedPrayer?.name} today?",
                            style = MaterialTheme.typography.titleMedium,
                            color = MaterialTheme.colorScheme.onSurface
                        )

                        // Your existing status grid
                        PrayerStatusGrid(
                            selectedPrayer = selectedPrayer!!,
                            value = value,
                            viewModel = viewModel,
                            gender = gender,
                            onClose = {
                                coroutineScope.launch {
                                    sheetState.hide()
                                }.invokeOnCompletion {
                                    if (!sheetState.isVisible) {
                                        selectedPrayer = null
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
    val isDark = isSystemInDarkTheme()

    // --- Step 1: Define all options (single source of truth)
    val options = listOf(
        Triple(
            "Not Prayed",
            R.drawable.notprayed,
            if (isDark) Color(0xFFB91C1C) else Color(0xFFEF4444) // Dark Red / Light Red
        ),
        Triple(
            "Prayed Late",
            R.drawable.prayedlate,
            if (isDark) Color(0xFFE07B00) else Color(0xFFF59E0B) // Dark Amber / Light Amber
        ),
        Triple(
            "On Time",
            R.drawable.prayedontime,
            if (isDark) Color(0xFF1D4ED8) else Color(0xFF3B82F6) // Dark Blue / Light Blue
        ),
        Triple(
            if (gender == "Woman") "Exempted" else "In Jamaat",
            if (gender == "Woman") R.drawable.track else R.drawable.jamat,
            if (gender == "Woman") {
                Color(0xFF8B5CF6) // Same purple for both modes
            } else {
                if (isDark) Color(0xFF15803D) else Color(0xFF22C55E) // Dark Green / Light Green
            }
        )
    )

    // ADDED: Wrap content in Box to overlay close button on top
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 8.dp, end = 8.dp)
    ) {

        // ADDED: Cross (Close) button at top-right corner
        IconButton(
            onClick = { onClose() },
            modifier = Modifier.align(Alignment.TopEnd)
        ) {
            Icon(
                imageVector = Icons.Default.Close,
                contentDescription = "Close",
                tint = if (isDark) Color.White else Color.Black // ADDED: Theme-based tint
            )
        }

        // ADDED: Shift grid down so it doesn’t overlap the close button
        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 48.dp, start = 16.dp, end = 16.dp, bottom = 16.dp),
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
                            coroutineScope.launch { onClose() } // ADDED: Close sheet after selection
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
}