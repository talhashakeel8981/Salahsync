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

    private val _prayerCounts = mutableStateOf<Map<String, Int>>(
        mapOf("Fajr" to 0, "Dhuhr" to 0, "Asr" to 0, "Maghrib" to 0, "Isha" to 0)
    )
    val prayerCounts: State<Map<String, Int>> = _prayerCounts

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

            if (oldStatus != null && oldStatus != statusResAgain) {
                when (oldStatus) {
                    R.drawable.prayedlate -> _prayedCount.value--
                    R.drawable.notprayed -> _notPrayedCount.value--
                    R.drawable.prayedontime -> _onTimeCount.value--
                    R.drawable.jamat -> _jamatCount.value--
                }
            }

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

            val oldPerformed = oldStatus != null && oldStatus != R.drawable.notprayed
            val newPerformed = statusResAgain != R.drawable.notprayed
            val currentMap = _prayerCounts.value.toMutableMap()
            if (!oldPerformed && newPerformed) {
                currentMap[name] = (currentMap[name] ?: 0) + 1
            } else if (oldPerformed && !newPerformed) {
                currentMap[name] = (currentMap[name] ?: 0).coerceAtLeast(0) - 1
            }
            _prayerCounts.value = currentMap

            _prayerStatusImages.value = _prayerStatusImages.value.toMutableMap().apply { put(name, statusResAgain) }
            _totalCounts.value = _prayedCount.value + _notPrayedCount.value + _onTimeCount.value + _jamatCount.value
        }
    }

    fun loadStats(date: LocalDate) {
        viewModelScope.launch {
            _prayedCount.value = dao.getTotalStatusCount(R.drawable.prayedlate)
            _notPrayedCount.value = dao.getTotalStatusCount(R.drawable.notprayed)
            _onTimeCount.value = dao.getTotalStatusCount(R.drawable.prayedontime)
            _jamatCount.value = dao.getTotalStatusCount(R.drawable.jamat)
            _totalCounts.value = dao.getTotalPrayers()

            _prayerCounts.value = mapOf(
                "Fajr" to dao.getPrayerPerformedCount("Fajr", R.drawable.notprayed),
                "Dhuhr" to dao.getPrayerPerformedCount("Dhuhr", R.drawable.notprayed),
                "Asr" to dao.getPrayerPerformedCount("Asr", R.drawable.notprayed),
                "Maghrib" to dao.getPrayerPerformedCount("Maghrib", R.drawable.notprayed),
                "Isha" to dao.getPrayerPerformedCount("Isha", R.drawable.notprayed)
            )
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