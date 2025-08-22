package com.example.salahsync.ui.Screens

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.salahsync.DataBase.PrayerDao
import com.example.salahsync.DataBase.PrayerEntity
import kotlinx.coroutines.launch
import java.time.LocalDate

class PrayerScreenViewModel(private val dao: PrayerDao) : ViewModel() {
    private val _prayerStatusImages = mutableStateOf<Map<String, Int>>(emptyMap())
    val prayerStatusImages: androidx.compose.runtime.State<Map<String, Int>> = _prayerStatusImages

    fun loadPrayers(date: LocalDate) {
        viewModelScope.launch {
            val prayers = dao.getPrayersByDate(date.toString()) // 🛠️ CHANGED: Convert LocalDate to String
            _prayerStatusImages.value = prayers.associate { it.name to it.statusRes } // 🛠️ CHANGED: Use statusRes instead of iconRes
        }
    }

    fun savePrayerStatus(name: String, statusRes: Int, date: LocalDate, statusResAgain: Int) { // 🛠️ CHANGED: Renamed iconRes to statusRes for clarity
        viewModelScope.launch {
            dao.insertPrayer(
                PrayerEntity(
                    name = name,
                    iconRes = statusRes, // 🛠️ CHANGED: Use statusRes for iconRes in PrayerEntity
                    date = date.toString(),
                    statusRes = statusResAgain
                )
            )
            _prayerStatusImages.value = _prayerStatusImages.value + (name to statusResAgain)
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