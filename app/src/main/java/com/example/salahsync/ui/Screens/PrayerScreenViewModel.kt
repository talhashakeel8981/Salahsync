package com.example.salahsync.ui.Screens

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.salahsync.DataBase.PrayerDao
import kotlinx.coroutines.launch
import java.time.LocalDate

class PrayerScreenViewModel(private val dao: PrayerDao) : ViewModel() {

    private val _prayerStatusImages = androidx.compose.runtime.mutableStateOf<Map<String, Int>>(emptyMap())
    val prayerStatusImages: androidx.compose.runtime.State<Map<String, Int>> = _prayerStatusImages

    fun loadPrayers(date: LocalDate) {
        viewModelScope.launch {
            val prayers = dao.getPrayersByDate(date.toString())
            _prayerStatusImages.value = prayers.associate { it.name to it. }
        }
    }

    fun savePrayerStatus(name: String, iconRes: Int, date: LocalDate, statusRes: Int) {
        viewModelScope.launch {
            dao.insertPrayer(
                Prayer(
                    name = name,
                    iconRes = iconRes,
                    date = date.toString(),
                    statusRes = statusRes
                )
            )
            _prayerStatusImages.value = _prayerStatusImages.value + (name to statusRes)
        }
    }
}

// ✅ Factory (for MainActivity injection)
class PrayerViewModelFactory(private val dao: PrayerDao) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(PrayerScreenViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return PrayerScreenViewModel(dao) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}