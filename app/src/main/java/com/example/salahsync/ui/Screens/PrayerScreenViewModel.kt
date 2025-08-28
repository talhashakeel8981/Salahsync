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

    // Added: Local map to track previous status for each prayer
    // COMMENT: Helps manage updates without double-counting by storing the last known status
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
            val oldStatus = prayerSelections[name]

            // Decrement old status count if it exists and is different from the new status
            // COMMENT: Prevents double-counting by adjusting the counter only if the status changes
            if (oldStatus != null && oldStatus != statusResAgain) {
                when (oldStatus) {
                    R.drawable.prayedlate -> _prayedCount.value--
                    R.drawable.notprayed -> _notPrayedCount.value--
                    R.drawable.prayedontime -> _onTimeCount.value--
                    R.drawable.jamat -> _jamatCount.value--
                }
            }

            // Create or update the prayer entity
            val prayerEntity = PrayerEntity(
                id = existingPrayer?.id ?: 0, // Use existing ID if available, otherwise 0 for new entry
                name = name,
                iconRes = statusRes,
                date = date.toString(),
                statusRes = statusResAgain
            )

            // Update or insert based on existence
            // COMMENT: Uses update if the prayer exists, insert if new, to avoid duplicates
            if (existingPrayer != null) {
                dao.updatePrayer(prayerEntity)
            } else {
                dao.insertPrayer(prayerEntity)
            }

            // Update local selection map
            prayerSelections[name] = statusResAgain

            // Increment new status count
            // COMMENT: Adds to the new category only if the status changed or is a new entry
            when (statusResAgain) {
                R.drawable.prayedlate -> _prayedCount.value++
                R.drawable.notprayed -> _notPrayedCount.value++
                R.drawable.prayedontime -> _onTimeCount.value++
                R.drawable.jamat -> _jamatCount.value++
            }

            _prayerStatusImages.value = _prayerStatusImages.value.toMutableMap().apply { put(name, statusResAgain) }
            _totalCounts.value = _prayedCount.value + _notPrayedCount.value + _onTimeCount.value + _jamatCount.value
        }
    }

    // Added new function to load stats
    // COMMENT: This fetches the total counts for each category using the correct DAO queries
    fun loadStats(date: LocalDate) {
        viewModelScope.launch {
            // Fixed: Correct drawable mappings to distinguish "Prayed Late" and "On Time"
            // COMMENT: Updated prayedCount to use prayedlate instead of prayedontime to separate categories
            _prayedCount.value = dao.getTotalStatusCount(R.drawable.prayedlate) // "Prayed Late"
            _notPrayedCount.value = dao.getTotalStatusCount(R.drawable.notprayed)
            _onTimeCount.value = dao.getTotalStatusCount(R.drawable.prayedontime) // "On Time"
            _jamatCount.value = dao.getTotalStatusCount(R.drawable.jamat)
            _totalCounts.value = dao.getTotalPrayers() // Set total from database
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