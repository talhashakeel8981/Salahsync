import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
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
import androidx.compose.runtime.remember


// Define the Prayer data class
data class Prayer(
    val name: String,
    val iconRes: androidx.compose.ui.graphics.vector.ImageVector
)

@Composable
fun PrayerTrackingScreen() {
    // State to hold the list of prayers and their selection status
    val prayers = remember {
        mutableStateListOf(
            Prayer("Fajr", Icons.Default.DateRange),
            Prayer("Dhuhr", Icons.Default.DateRange),
            Prayer("Asr", Icons.Default.DateRange),
            Prayer("Maghrib", Icons.Default.DateRange),
            Prayer("Isha", Icons.Default.DateRange)
        )
    }
    val selectedIndices = remember { mutableStateListOf<Int>() }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(16.dp)
    ) {
        item {
            Text(
                text = "PRAYERS",
                fontSize = 20.sp,
                color = colorResource(id = R.color.purple_500),
                modifier = Modifier.padding(bottom = 16.dp)
            )
        }
        items(prayers.indices) { index ->
            PrayerItem(
                prayer = prayers[index],
                isSelected = selectedIndices.contains(index),
                onClick = {
                    if (selectedIndices.contains(index)) {
                        selectedIndices.remove(index)
                    } else {
                        selectedIndices.add(index)
                    }
                }
            )
        }
    }
}

@Composable
fun PrayerItem(prayer: Prayer, isSelected: Boolean, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(vertical = 4.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (isSelected) colorResource(id = R.color.purple_500) else Color.LightGray
        ),
        shape = MaterialTheme.shapes.medium
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = prayer.name,
                fontSize = 16.sp,
                color = if (isSelected) Color.White else Color.Black
            )
            if (isSelected) {
                Icon(
                    imageVector = ,
                    contentDescription = "${prayer.name} selected",
                    tint = Color.White,
                    modifier = Modifier.size(24.dp)
                )
            }
        }
    }
}

@Preview(showSystemUi = true)
@Composable
fun PrayerTrackingScreenPreview() {
    PrayerTrackingScreen()
}
