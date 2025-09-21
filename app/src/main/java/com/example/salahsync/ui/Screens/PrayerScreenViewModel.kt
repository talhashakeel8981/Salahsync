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
import com.example.salahsync.DataBase.Gender
import com.example.salahsync.DataBase.GenderDao
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow


import androidx.compose.ui.graphics.Color // ADDED: Import Color for statusColorMap // Why: Needed to define colors for status icons, including Exempted
import android.util.Log // ADDED: Import Log for debugging // Why: To log gender loading and database errors

class PrayerScreenViewModel(
    private val dao: PrayerDao,
    private val genderDao: GenderDao // ✅ NEW: Inject GenderDao for gender-based UI logic
) : ViewModel() {

    // ------------------ Gender ------------------

    // ✅ NEW: Local state to hold the current user's gender
    // Before: No gender awareness, so app could not differentiate male/female prayer options.
    // After: Added `_userGender` as state to control which bottom sheet (male/female) to show.
    private val _userGender = mutableStateOf("Man")
    val userGender: State<String> = _userGender // Exposed to UI (read-only)

    init {
        // ✅ NEW: Load gender from DB once ViewModel is initialized
        // Why: Ensures UI knows whether to show "Jamaat" (male) or "Exempted" (female)
        viewModelScope.launch {
            try {
                val gender = genderDao.getGender()
                _userGender.value = gender?.genderName ?: "Man"
                // ADDED: Log gender loading // Why: Debug if gender is correctly retrieved from database
                Log.d("PrayerScreenViewModel", "Loaded gender: ${_userGender.value}")
            } catch (e: Exception) {
                // ADDED: Error handling // Why: Catches database errors to prevent crashes
                Log.e("PrayerScreenViewModel", "Error loading gender: ${e.message}")
                _userGender.value = "Man" // Fallback to "Man" on error
            }
        }
    }

    // ------------------ Counts ------------------
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

    // ------------------ Per-status Maps ------------------
    // CHANGED: Introduced per-status maps (instead of 1 global map)
    // Why: Needed accurate per-status counts for bar chart breakdown.
    private val _prayedLateCounts = mutableStateOf<Map<String, Int>>(
        mapOf("Fajr" to 0, "Dhuhr" to 0, "Asr" to 0, "Maghrib" to 0, "Isha" to 0)
    )
    val prayedLateCounts: State<Map<String, Int>> = _prayedLateCounts

    private val _notPrayedCounts = mutableStateOf<Map<String, Int>>(
        mapOf("Fajr" to 0, "Dhuhr" to 0, "Asr" to 0, "Maghrib" to 0, "Isha" to 0)
    )
    val notPrayedCounts: State<Map<String, Int>> = _notPrayedCounts

    private val _onTimeCounts = mutableStateOf<Map<String, Int>>(
        mapOf("Fajr" to 0, "Dhuhr" to 0, "Asr" to 0, "Maghrib" to 0, "Isha" to 0)
    )
    val onTimeCounts: State<Map<String, Int>> = _onTimeCounts

    private val _jamatCounts = mutableStateOf<Map<String, Int>>(
        mapOf("Fajr" to 0, "Dhuhr" to 0, "Asr" to 0, "Maghrib" to 0, "Isha" to 0)
    )
    val jamatCounts: State<Map<String, Int>> = _jamatCounts

    private val prayerSelections = mutableMapOf<String, Int>()

    // ------------------ Prayer Tiles ------------------
    private val _prayers = mutableStateOf(
        listOf(
            PrayerTilesData("Fajr", R.drawable.ic_fajr),
            PrayerTilesData("Dhuhr", R.drawable.ic_dhuhur),
            PrayerTilesData("Asr", R.drawable.ic_asr),
            PrayerTilesData("Maghrib", R.drawable.ic_maghrib),
            PrayerTilesData("Isha", R.drawable.ic_esha)
        )
    )
    val prayers: State<List<PrayerTilesData>> = _prayers

    // ADDED: Status color map for icon tinting // Why: Defines colors for status icons, including Exempted
    private val statusColorMap = mapOf(
        R.drawable.notprayed to Color(0xFF000000),   // Black for Not Prayed
        R.drawable.prayedlate to Color(0xFFD64F73),  // Pinkish-red for Prayed Late
        R.drawable.prayedontime to Color(0xFFFFD92E),// Yellow for On Time
        R.drawable.jamat to Color(0xFF1DD1A1),       // Teal for In Jamaat
        R.drawable.track to Color(0xFF8B5CF6)     // CHANGED: Replaced Menstruation with Exempted // Why: Matches female-specific status
    )

    // ADDED: State for status colors // Why: Maps prayer names to their status colors for dynamic tinting in PrayerList
    private val _statusColors = mutableStateOf<Map<String, Color>>(emptyMap())
    val statusColors: State<Map<String, Color>> = _statusColors

    // ------------------ Database Ops ------------------

    fun loadPrayers(date: LocalDate) {
        viewModelScope.launch {
            try {
                val prayers = dao.getPrayersByDate(date.toString())
                // Update prayer status images
                _prayerStatusImages.value = prayers.associate { it.name to it.statusRes }

                // ADDED: Update status colors // Why: Maps statusRes to colors for PrayerList icon tinting (e.g., Exempted as purple)
                _statusColors.value = prayers.associate { prayer ->
                    prayer.name to (statusColorMap[prayer.statusRes] ?: Color.Transparent)
                }

                // Keep a mutable cache for fast UI selection updates
                prayerSelections.clear()
                prayerSelections.putAll(prayers.associate { it.name to it.statusRes })
                // ADDED: Log loaded prayers // Why: Debug if prayers and statuses are loaded correctly
                Log.d("PrayerScreenViewModel", "Loaded prayers: ${_prayerStatusImages.value}")
            } catch (e: Exception) {
                // ADDED: Error handling // Why: Prevents crashes on database query failure
                Log.e("PrayerScreenViewModel", "Error loading prayers: ${e.message}")
            }
        }
    }

    fun savePrayerStatus(name: String, statusRes: Int, date: LocalDate, statusResAgain: Int) {
        viewModelScope.launch {
            try {
                val existingPrayer = dao.getPrayersByDate(date.toString()).find { it.name == name }

                // CHANGED: Map Exempted to jamatCount for females // Why: Reuses jamatCount for Exempted to simplify stats
                val finalStatusRes = if (_userGender.value == "Woman" && statusRes == R.drawable.track) {
                    statusRes // Store Exempted in database
                } else {
                    statusResAgain // Use original status for males or other cases
                }

                val prayerEntity = PrayerEntity(
                    id = existingPrayer?.id ?: 0,
                    name = name,
                    iconRes = statusRes,
                    date = date.toString(),
                    statusRes = finalStatusRes
                )

                if (existingPrayer != null) {
                    dao.updatePrayer(prayerEntity)
                } else {
                    dao.insertPrayer(prayerEntity)
                }

                // ✅ Update cached selection + notify UI
                prayerSelections[name] = finalStatusRes
                _prayerStatusImages.value = _prayerStatusImages.value.toMutableMap()
                    .apply { put(name, finalStatusRes) }
                // ADDED: Update status color // Why: Ensures PrayerList reflects new status color (e.g., purple for Exempted)
                _statusColors.value = _statusColors.value.toMutableMap()
                    .apply { put(name, statusColorMap[finalStatusRes] ?: Color.Transparent) }
                // ADDED: Log status save // Why: Debug if status (e.g., Exempted) is saved correctly
                Log.d("PrayerScreenViewModel", "Saved status for $name: $finalStatusRes")
            } catch (e: Exception) {
                // ADDED: Error handling // Why: Prevents crashes on database save failure
                Log.e("PrayerScreenViewModel", "Error saving prayer status: ${e.message}")
            }
        }
    }

    // ------------------ Helper Queries ------------------

    // Load counts for a given status (e.g., OnTime, NotPrayed) for ALL TIME
    private suspend fun loadStatusCounts(statusRes: Int): Map<String, Int> {
        try {
            return mapOf(
                "Fajr" to dao.getPrayerStatusCount("Fajr", statusRes),
                "Dhuhr" to dao.getPrayerStatusCount("Dhuhr", statusRes),
                "Asr" to dao.getPrayerStatusCount("Asr", statusRes),
                "Maghrib" to dao.getPrayerStatusCount("Maghrib", statusRes),
                "Isha" to dao.getPrayerStatusCount("Isha", statusRes)
            )
        } catch (e: Exception) {
            // ADDED: Error handling // Why: Prevents crashes on database query failure
            Log.e("PrayerScreenViewModel", "Error loading status counts: ${e.message}")
            return emptyMap()
        }
    }

    // Overload for RANGE-based counts (Week, Month, Year)
    private suspend fun loadStatusCounts(statusRes: Int, startDate: String, endDate: String): Map<String, Int> {
        try {
            return mapOf(
                "Fajr" to dao.getPrayerStatusCountByDateRange("Fajr", statusRes, startDate, endDate),
                "Dhuhr" to dao.getPrayerStatusCountByDateRange("Dhuhr", statusRes, startDate, endDate),
                "Asr" to dao.getPrayerStatusCountByDateRange("Asr", statusRes, startDate, endDate),
                "Maghrib" to dao.getPrayerStatusCountByDateRange("Maghrib", statusRes, startDate, endDate),
                "Isha" to dao.getPrayerStatusCountByDateRange("Isha", statusRes, startDate, endDate)
            )
        } catch (e: Exception) {
            // ADDED: Error handling // Why: Prevents crashes on database query failure
            Log.e("PrayerScreenViewModel", "Error loading status counts by range: ${e.message}")
            return emptyMap()
        }
    }

    // ------------------ Period Stats ------------------

    @RequiresApi(Build.VERSION_CODES.O)
    fun loadStatsForPeriod(period: String, now: LocalDate) {
        viewModelScope.launch {
            try {
                val endDate = now.toString()

                if (period == "All Time") {
                    // Load global counts (ignores date range)
                    _prayedCount.value = dao.getTotalStatusCount(R.drawable.prayedlate)
                    _notPrayedCount.value = dao.getTotalStatusCount(R.drawable.notprayed)
                    _onTimeCount.value = dao.getTotalStatusCount(R.drawable.prayedontime)
                    // CHANGED: Use jamatCount for both In Jamaat (men) and Exempted (women) // Why: Simplifies stats by reusing state
                    _jamatCount.value = dao.getTotalStatusCount(
                        if (_userGender.value == "Woman") R.drawable.track else R.drawable.jamat
                    )
                    _totalCounts.value = dao.getTotalPrayers()

                    _prayedLateCounts.value = loadStatusCounts(R.drawable.prayedlate)
                    _notPrayedCounts.value = loadStatusCounts(R.drawable.notprayed)
                    _onTimeCounts.value = loadStatusCounts(R.drawable.prayedontime)
                    // CHANGED: Load jamatCounts with gender-based status // Why: Maps Exempted to jamatCounts for women
                    _jamatCounts.value = loadStatusCounts(
                        if (_userGender.value == "Woman") R.drawable.track else R.drawable.jamat
                    )
                } else {
                    // Load within specific range
                    val startDate = when (period) {
                        "Week" -> now.minusDays(6).toString()
                        "Month" -> now.minusDays(29).toString()
                        "Year" -> now.minusDays(364).toString()
                        else -> "1970-01-01" // fallback for custom case
                    }

                    _prayedCount.value = dao.getStatusCountByDateRange(R.drawable.prayedlate, startDate, endDate)
                    _notPrayedCount.value = dao.getStatusCountByDateRange(R.drawable.notprayed, startDate, endDate)
                    _onTimeCount.value = dao.getStatusCountByDateRange(R.drawable.prayedontime, startDate, endDate)
                    // CHANGED: Use jamatCount for both In Jamaat and Exempted // Why: Simplifies stats
                    _jamatCount.value = dao.getStatusCountByDateRange(
                        if (_userGender.value == "Woman") R.drawable.track else R.drawable.jamat,
                        startDate,
                        endDate
                    )
                    _totalCounts.value = dao.getTotalPrayersByDateRange(startDate, endDate)

                    _prayedLateCounts.value = loadStatusCounts(R.drawable.prayedlate, startDate, endDate)
                    _notPrayedCounts.value = loadStatusCounts(R.drawable.notprayed, startDate, endDate)
                    _onTimeCounts.value = loadStatusCounts(R.drawable.prayedontime, startDate, endDate)
                    // CHANGED: Load jamatCounts with gender-based status // Why: Maps Exempted to jamatCounts for women
                    _jamatCounts.value = loadStatusCounts(
                        if (_userGender.value == "Woman") R.drawable.track else R.drawable.jamat,
                        startDate,
                        endDate
                    )
                }
                // ADDED: Log stats loading // Why: Debug if stats (including Exempted) are loaded correctly
                Log.d("PrayerScreenViewModel", "Loaded stats for $period: Jamaat/Exempted=${_jamatCount.value}")
            } catch (e: Exception) {
                // ADDED: Error handling // Why: Prevents crashes on stats loading failure
                Log.e("PrayerScreenViewModel", "Error loading stats: ${e.message}")
            }
        }
    }
}

// ✅ CHANGED: Factory updated to also accept GenderDao
class PrayerViewModelFactory(
    private val dao: PrayerDao,
    private val genderDao: GenderDao // NEW
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(PrayerScreenViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return PrayerScreenViewModel(dao, genderDao) as T // pass GenderDao too
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}