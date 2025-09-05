package com.example.salahsync.ui.Screens

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
    // CHANGED: Removed _prayerCounts since we now use per-status maps for bar charts. Before: Single map for total performed prayers, causing all bar charts to show the same data. After: Replaced with per-status maps for accurate per-status bar updates.
    // private val _prayerCounts = mutableStateOf<Map<String, Int>>(mapOf("Fajr" to 0, "Dhuhr" to 0, "Asr" to 0, "Maghrib" to 0, "Isha" to 0))
    // val prayerCounts: State<Map<String, Int>> = _prayerCounts
    // CHANGED: Added per-status count maps for each prayer type. Before: No per-status breakdown, leading to incorrect bar updates. After: Each status has its own map, allowing specific updates (e.g., only Fajr bar in Prayed Late updates when Fajr status changes to Prayed Late).
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
    // CHANGED: Added helper function to get the MutableState for a given status. Before: No such helper. After: Simplifies updating the correct map in savePrayerStatus.
    private fun getCountStateForStatus(status: Int): androidx.compose.runtime.MutableState<Map<String, Int>> {
        return when (status) {
            R.drawable.prayedlate -> _prayedLateCounts
            R.drawable.notprayed -> _notPrayedCounts
            R.drawable.prayedontime -> _onTimeCounts
            R.drawable.jamat -> _jamatCounts
            else -> throw IllegalArgumentException("Unknown status")
        }
    }
    fun savePrayerStatus(name: String, statusRes: Int, date: LocalDate, statusResAgain: Int) {
        viewModelScope.launch {
            val existingPrayer = dao.getPrayersByDate(date.toString()).find { it.name == name }
            val oldStatus = prayerSelections[name]
            // CHANGED: Adjusted decrement logic to always decrement old status if it exists, regardless of whether it matches new. Before: Only decremented if old != new, causing incorrect +1 increment when updating to same status. After: Always decrement old (if exists) and increment new, ensuring net 0 for same-status updates, correct changes for different, and +1 for new inserts.
            if (oldStatus != null) {
                when (oldStatus) {
                    R.drawable.prayedlate -> _prayedCount.value--
                    R.drawable.notprayed -> _notPrayedCount.value--
                    R.drawable.prayedontime -> _onTimeCount.value--
                    R.drawable.jamat -> _jamatCount.value--
                }
            }
            // CHANGED: Added per-status per-prayer map updates. Before: No per-status map updates, only performed logic. After: Decrement old map and increment new map for the specific prayer name, ensuring only the affected prayer's bar updates in the correct status chart.
            if (oldStatus != null) {
                val oldMapState = getCountStateForStatus(oldStatus)
                val newMap = oldMapState.value.toMutableMap()
                val current = newMap[name] ?: 0
                newMap[name] = (current - 1).coerceAtLeast(0)
                oldMapState.value = newMap
            }
            val newMapState = getCountStateForStatus(statusResAgain)
            val newMap = newMapState.value.toMutableMap()
            val current = newMap[name] ?: 0
            newMap[name] = current + 1
            newMapState.value = newMap
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
            when (statusResAgain) {
                R.drawable.prayedlate -> _prayedCount.value++
                R.drawable.notprayed -> _notPrayedCount.value++
                R.drawable.prayedontime -> _onTimeCount.value++
                R.drawable.jamat -> _jamatCount.value++
            }
            // CHANGED: Removed old performed count logic since it's replaced by per-status maps. Before: Updated a single performed map only when crossing not-prayed boundary. After: No need, as per-status maps handle all cases accurately.
            // val oldPerformed = oldStatus != null && oldStatus != R.drawable.notprayed
            // val newPerformed = statusResAgain != R.drawable.notprayed
            // val currentMap = _prayerCounts.value.toMutableMap()
            // if (!oldPerformed && newPerformed) {
            //     currentMap[name] = (currentMap[name] ?: 0) + 1
            // } else if (oldPerformed && !newPerformed) {
            //     currentMap[name] = (currentMap[name] ?: 0).coerceAtLeast(0) - 1
            // }
            // _prayerCounts.value = currentMap
            _prayerStatusImages.value = _prayerStatusImages.value.toMutableMap().apply { put(name, statusResAgain) }
            _totalCounts.value = _prayedCount.value + _notPrayedCount.value + _onTimeCount.value + _jamatCount.value
        }
    }
    // CHANGED: Added helper to load per-status counts. Before: No helper. After: Reuses code for loading each status map.
    private suspend fun loadStatusCounts(statusRes: Int): Map<String, Int> {
        return mapOf(
            "Fajr" to dao.getPrayerStatusCount("Fajr", statusRes),
            "Dhuhr" to dao.getPrayerStatusCount("Dhuhr", statusRes),
            "Asr" to dao.getPrayerStatusCount("Asr", statusRes),
            "Maghrib" to dao.getPrayerStatusCount("Maghrib", statusRes),
            "Isha" to dao.getPrayerStatusCount("Isha", statusRes)
        )
    }
    fun loadStats(date: LocalDate) {
        viewModelScope.launch {
            _prayedCount.value = dao.getTotalStatusCount(R.drawable.prayedlate)
            _notPrayedCount.value = dao.getTotalStatusCount(R.drawable.notprayed)
            _onTimeCount.value = dao.getTotalStatusCount(R.drawable.prayedontime)
            _jamatCount.value = dao.getTotalStatusCount(R.drawable.jamat)
            _totalCounts.value = dao.getTotalPrayers()
            // CHANGED: Replaced single performed counts with per-status maps using new DAO query. Before: Loaded total performed per prayer, causing all charts to use same data. After: Loads separate maps for each status, ensuring each chart shows status-specific prayer counts.
            _prayedLateCounts.value = loadStatusCounts(R.drawable.prayedlate)
            _notPrayedCounts.value = loadStatusCounts(R.drawable.notprayed)
            _onTimeCounts.value = loadStatusCounts(R.drawable.prayedontime)
            _jamatCounts.value = loadStatusCounts(R.drawable.jamat)
        }
    }
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
