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

class PrayerScreenViewModel(private val dao: PrayerDao) : ViewModel() {
    private val _prayedCount = mutableStateOf(0)
    val prayedCount: State<Int> = _prayedCount

    private val _notPrayedCount = mutableStateOf(0)
    val notPrayedCount: State<Int> = _notPrayedCount

    private val _onTimeCount = mutableStateOf(0)
    val onTimeCount: State<Int> = _onTimeCount

    private val _jamatCount=mutableStateOf(0)
    val jamatCount: State<Int> = _jamatCount

    private val _prayerStatusImages = mutableStateOf<Map<String, Int>>(emptyMap())
    val prayerStatusImages: State<Map<String, Int>> = _prayerStatusImages

    fun loadPrayers(date: LocalDate) {
        viewModelScope.launch {
            val prayers = dao.getPrayersByDate(date.toString())
            _prayerStatusImages.value = prayers.associate { it.name to it.statusRes }
        }
    }

    fun savePrayerStatus(name: String, statusRes: Int, date: LocalDate, statusResAgain: Int) {
        viewModelScope.launch {
            dao.insertPrayer(
                PrayerEntity(
                    name = name,
                    iconRes = statusRes,
                    date = date.toString(),
                    statusRes = statusResAgain
                )
            )
            _prayerStatusImages.value = _prayerStatusImages.value.toMutableMap().apply { put(name, statusResAgain) }
        }
    }

    // Added new function to load stats
    // COMMENT: This fetches the total counts for Prayed, Not Prayed, and On Time using the new DAO queries
    fun loadStats(date: LocalDate) {
        viewModelScope.launch {
            _prayedCount.value = dao.getTotalStatusCount(R.drawable.prayedontime)
            _notPrayedCount.value = dao.getTotalStatusCount(R.drawable.notprayed)
            _onTimeCount.value = dao.getTotalStatusCount(R.drawable.prayedontime)
            _jamatCount.value=dao.getTotalStatusCount(R.drawable.jamat)
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