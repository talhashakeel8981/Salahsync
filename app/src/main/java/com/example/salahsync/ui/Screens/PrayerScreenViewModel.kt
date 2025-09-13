package com.example.salahsync.ui.Screens

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.salahsync.DataBase.PrayerDao
import com.example.salahsync.DataBase.PrayerEntity
import com.example.salahsync.R
import kotlinx.coroutines.launch
import java.time.LocalDate
import androidx.compose.runtime.State




import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow


import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking


class PrayerScreenViewModel(private val dao: PrayerDao) : ViewModel() {
    private val _prayedCount = mutableStateOf(0)
    val prayedCount: State<Int> = _prayedCount
    private val _notPrayedCount = mutableStateOf(0)
    val notPrayedCount: State<Int> = _notPrayedCount
    private val _onTimeCount = mutableStateOf(0)
    val onTimeCount: State<Int> = _onTimeCount
    private val _jamatCount = mutableStateOf(0)
    val jamatCount: State<Int> = _jamatCount
    private val _prayerStatusImages = mutableStateOf<Map<String, Int>>(emptyMap())
    val prayerStatusImages: State<Map<String, Int>> = _prayerStatusImages
    private val _totalCounts = mutableStateOf(0)
    val totalCounts: State<Int> = _totalCounts
    // CHANGED: Removed _prayerCounts since we now use per-status maps for bar charts.
    // Before: Single map for total performed prayers, causing all bar charts to show the same data.
    // After: Replaced with per-status maps for accurate per-status bar updates.
    // private val _prayerCounts = mutableStateOf<Map<String, Int>>(mapOf("Fajr" to 0, "Dhuhr" to 0, "Asr" to 0, "Maghrib" to 0, "Isha" to 0))
    // val prayerCounts: State<Map<String, Int>> = _prayerCounts
    // CHANGED: Added per-status count maps for each prayer type.
    // Before: No per-status breakdown, leading to incorrect bar updates.
    // After: Each status has its own map, allowing specific updates (e.g., only Fajr bar in Prayed Late updates when Fajr status changes to Prayed Late).
    private val _prayedLateCounts = mutableStateOf<Map<String, Int>>(mapOf("Fajr" to 0, "Dhuhr" to 0, "Asr" to 0, "Maghrib" to 0, "Isha" to 0))
    val prayedLateCounts: State<Map<String, Int>> = _prayedLateCounts
    private val _notPrayedCounts = mutableStateOf<Map<String, Int>>(mapOf("Fajr" to 0, "Dhuhr" to 0, "Asr" to 0, "Maghrib" to 0, "Isha" to 0))
    val notPrayedCounts: State<Map<String, Int>> = _notPrayedCounts
    private val _onTimeCounts = mutableStateOf<Map<String, Int>>(mapOf("Fajr" to 0, "Dhuhr" to 0, "Asr" to 0, "Maghrib" to 0, "Isha" to 0))
    val onTimeCounts: State<Map<String, Int>> = _onTimeCounts
    private val _jamatCounts = mutableStateOf<Map<String, Int>>(mapOf("Fajr" to 0, "Dhuhr" to 0, "Asr" to 0, "Maghrib" to 0, "Isha" to 0))
    val jamatCounts: State<Map<String, Int>> = _jamatCounts
    private val prayerSelections = mutableMapOf<String, Int>()
    fun loadPrayers(date: LocalDate) {
        viewModelScope.launch {
            val prayers = dao.getPrayersByDate(date.toString())
            _prayerStatusImages.value = prayers.associate { it.name to it.statusRes }
            prayerSelections.clear()
            prayerSelections.putAll(prayers.associate { it.name to it.statusRes })
        }
    }
    fun savePrayerStatus(name: String, statusRes: Int, date: LocalDate, statusResAgain: Int) {
        viewModelScope.launch {
            val existingPrayer = dao.getPrayersByDate(date.toString()).find { it.name == name }
            val prayerEntity = PrayerEntity(
                id = existingPrayer?.id ?: 0,
                name = name,
                iconRes = statusRes,
                date = date.toString(),
                statusRes = statusResAgain
            )
            if (existingPrayer != null) {
                dao.updatePrayer(prayerEntity)
            } else {
                dao.insertPrayer(prayerEntity)
            }
            prayerSelections[name] = statusResAgain
            _prayerStatusImages.value = _prayerStatusImages.value.toMutableMap().apply { put(name, statusResAgain) }
        }
    } // Updated: Removed all manual count/map increments/decrements (e.g., _prayedCount++, map updates). Why: Conflicts with period-specific loading; now rely on loadStatsForPeriod to fetch from DB for accuracy.
    // Updated: Removed getCountStateForStatus helper. Why: Unused after removing manual updates.
    private suspend fun loadStatusCounts(statusRes: Int): Map<String, Int> {
        return mapOf(
            "Fajr" to dao.getPrayerStatusCount("Fajr", statusRes),
            "Dhuhr" to dao.getPrayerStatusCount("Dhuhr", statusRes),
            "Asr" to dao.getPrayerStatusCount("Asr", statusRes),
            "Maghrib" to dao.getPrayerStatusCount("Maghrib", statusRes),
            "Isha" to dao.getPrayerStatusCount("Isha", statusRes)
        )
    }
    private suspend fun loadStatusCounts(statusRes: Int, startDate: String, endDate: String): Map<String, Int> {
        return mapOf(
            "Fajr" to dao.getPrayerStatusCountByDateRange("Fajr", statusRes, startDate, endDate),
            "Dhuhr" to dao.getPrayerStatusCountByDateRange("Dhuhr", statusRes, startDate, endDate),
            "Asr" to dao.getPrayerStatusCountByDateRange("Asr", statusRes, startDate, endDate),
            "Maghrib" to dao.getPrayerStatusCountByDateRange("Maghrib", statusRes, startDate, endDate),
            "Isha" to dao.getPrayerStatusCountByDateRange("Isha", statusRes, startDate, endDate)
        )
    } // Updated: Added overload for range-based loading. Why: Enables using date range queries for period-specific bar charts (e.g., weekly counts).
    @RequiresApi(Build.VERSION_CODES.O)
    fun loadStatsForPeriod(period: String, now: LocalDate) {
        viewModelScope.launch {
            val endDate = now.toString()
            if (period == "All Time") {
                _prayedCount.value = dao.getTotalStatusCount(R.drawable.prayedlate)
                _notPrayedCount.value = dao.getTotalStatusCount(R.drawable.notprayed)
                _onTimeCount.value = dao.getTotalStatusCount(R.drawable.prayedontime)
                _jamatCount.value = dao.getTotalStatusCount(R.drawable.jamat)
                _totalCounts.value = dao.getTotalPrayers()
                _prayedLateCounts.value = loadStatusCounts(R.drawable.prayedlate)
                _notPrayedCounts.value = loadStatusCounts(R.drawable.notprayed)
                _onTimeCounts.value = loadStatusCounts(R.drawable.prayedontime)
                _jamatCounts.value = loadStatusCounts(R.drawable.jamat)
            } else {
                val startDate = when (period) {
                    "Week" -> now.minusDays(6).toString()
                    "Month" -> now.minusDays(29).toString()
                    "Year" -> now.minusDays(364).toString()
                    else -> "1970-01-01"
                }
                _prayedCount.value = dao.getStatusCountByDateRange(R.drawable.prayedlate, startDate, endDate)
                _notPrayedCount.value = dao.getStatusCountByDateRange(R.drawable.notprayed, startDate, endDate)
                _onTimeCount.value = dao.getStatusCountByDateRange(R.drawable.prayedontime, startDate, endDate)
                _jamatCount.value = dao.getStatusCountByDateRange(R.drawable.jamat, startDate, endDate)
                _totalCounts.value = dao.getTotalPrayersByDateRange(startDate, endDate)
                _prayedLateCounts.value = loadStatusCounts(R.drawable.prayedlate, startDate, endDate)
                _notPrayedCounts.value = loadStatusCounts(R.drawable.notprayed, startDate, endDate)
                _onTimeCounts.value = loadStatusCounts(R.drawable.prayedontime, startDate, endDate)
                _jamatCounts.value = loadStatusCounts(R.drawable.jamat, startDate, endDate)
            }
        }
    } // Updated: Added new function to load stats for a specific period using range queries. Why: Enables functional tabs; calculates start/end dates and loads percentages/bars accordingly (e.g., last 7 days for "Week").
    // CHANGED: Replaced single performed counts with per-status maps using new DAO query.
    // Before: Loaded total performed per prayer, causing all charts to use same data.
    // After: Loads separate maps for each status, ensuring each chart shows status-specific prayer counts.
}
class PrayerViewModelFactory(private val dao: PrayerDao) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(PrayerScreenViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return PrayerScreenViewModel(dao) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}