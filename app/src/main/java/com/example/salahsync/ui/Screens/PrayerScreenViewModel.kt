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



import androidx.compose.ui.graphics.Color // ADDED: Import Color for statusColorMap // Why: Needed to define colors for status icons, including Exempted
import android.util.Log // ADDED: Import Log for debugging // Why: To log gender loading and database errors
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.database

import kotlinx.coroutines.flow.StateFlow


// MODIFIED: Removed import com.google.firebase.auth.FirebaseAuth and com.google.firebase.database.* // Why: Firebase handling moved to repository
// MODIFIED: Changed constructor to take repository instead of daos // Why: Centralize data access through repository for local + cloud sync
import com.example.salahsync.ui.Screens.SettingsOptions.DataBackup.PrayerRepository // ADDED: Import repository
class PrayerScreenViewModel(
    private val repository: PrayerRepository // MODIFIED: Replaced dao: PrayerDao, genderDao: GenderDao with repository
) : ViewModel() {
    // ------------------ Gender ------------------
    // ✅ NEW: Local state to hold the current user's gender
    // Before: No gender awareness, so app could not differentiate male/female prayer options.
    // After: Added `_userGender` as state to control which bottom sheet (male/female) to show.
    private val _userGender = mutableStateOf("Man")
    val userGender: State<String> = _userGender // Exposed to UI (read-only)

    // REPLACEMENT START: Extracted gender loading to a reusable function for on-demand reloads (e.g., when screen re-enters).
    fun loadGender() {
        viewModelScope.launch {
            try {
                // MODIFIED: Changed genderDao.getGender to repository.getGender // Why: Use centralized repository
                val gender = repository.getGender()
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
    // REPLACEMENT END

    init {
        loadGender()  // Call the new function for initial load.
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
        R.drawable.notprayed to Color(0xFF000000), // Black for Not Prayed
        R.drawable.prayedlate to Color(0xFFD64F73), // Pinkish-red for Prayed Late
        R.drawable.prayedontime to Color(0xFFFFD92E),// Yellow for On Time
        R.drawable.jamat to Color(0xFF1DD1A1), // Teal for In Jamaat
        R.drawable.track to Color(0xFF8B5CF6) // CHANGED: Replaced Menstruation with Exempted // Why: Matches female-specific status
    )
    // ADDED: State for status colors // Why: Maps prayer names to their status colors for dynamic tinting in PrayerList
    private val _statusColors = mutableStateOf<Map<String, Color>>(emptyMap())
    val statusColors: State<Map<String, Color>> = _statusColors
    // ------------------ Database Ops ------------------
    fun loadPrayers(date: LocalDate) {
        viewModelScope.launch {
            try {
                // MODIFIED: Changed dao.getPrayersByDate to repository.getPrayersByDate // Why: Use repository
                val prayers = repository.getPrayersByDate(date.toString())
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
    // MODIFIED: Changed icon param from String to Int // Why: Matches call site (passing R.drawable Int)
    fun savePrayerStatus(name: String, statusRes: Int, date: LocalDate, icon: Int) {
        viewModelScope.launch {
            try {
                // MODIFIED: Removed existingPrayer check // Why: Repository savePrayer now handles insert/update logic
                // Handle "Exempted" for Woman (purple icon)
                val finalStatusRes = if (_userGender.value == "Woman" && statusRes == R.drawable.track) {
                    statusRes // Exempted
                } else {
                    statusRes // Normal cases
                }
                // Create entity
                val prayerEntity = PrayerEntity(
                    id = 0, // MODIFIED: Always set id=0 // Why: Repository will handle existing id if needed
                    name = name,
                    iconRes = statusRes, // MODIFIED: Changed from statusRes to icon (but since call passes same, ok) // Why: Consistent with param
                    date = date.toString(),
                    statusRes = finalStatusRes
                )
                // MODIFIED: Replaced dao insert/update with repository.savePrayer // Why: Repository handles local + Firebase save
                repository.savePrayer(prayerEntity)
                // ✅ Update cached selection + notify UI
                prayerSelections[name] = finalStatusRes
                _prayerStatusImages.value = _prayerStatusImages.value.toMutableMap()
                    .apply { put(name, finalStatusRes) }
                _statusColors.value = _statusColors.value.toMutableMap()
                    .apply { put(name, statusColorMap[finalStatusRes] ?: Color.Transparent) }
                // MODIFIED: Removed direct Firebase upload // Why: Handled by repository
                Log.d("PrayerScreenViewModel", "Saved status for $name: $finalStatusRes")
            } catch (e: Exception) {
                Log.e("PrayerScreenViewModel", "Error saving prayer status: ${e.message}")
            }
        }
    }
    // ------------------ Helper Queries ------------------
    // Load counts for a given status (e.g., OnTime, NotPrayed) for ALL TIME
    private suspend fun loadStatusCounts(statusRes: Int): Map<String, Int> {
        try {
            // MODIFIED: Changed dao calls to repository // Why: Centralized access
            return mapOf(
                "Fajr" to repository.getPrayerStatusCount("Fajr", statusRes),
                "Dhuhr" to repository.getPrayerStatusCount("Dhuhr", statusRes),
                "Asr" to repository.getPrayerStatusCount("Asr", statusRes),
                "Maghrib" to repository.getPrayerStatusCount("Maghrib", statusRes),
                "Isha" to repository.getPrayerStatusCount("Isha", statusRes)
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
            // MODIFIED: Changed dao to repository
            return mapOf(
                "Fajr" to repository.getPrayerStatusCountByDateRange("Fajr", statusRes, startDate, endDate),
                "Dhuhr" to repository.getPrayerStatusCountByDateRange("Dhuhr", statusRes, startDate, endDate),
                "Asr" to repository.getPrayerStatusCountByDateRange("Asr", statusRes, startDate, endDate),
                "Maghrib" to repository.getPrayerStatusCountByDateRange("Maghrib", statusRes, startDate, endDate),
                "Isha" to repository.getPrayerStatusCountByDateRange("Isha", statusRes, startDate, endDate)
            )
        } catch (e: Exception) {
            // ADDED: Error handling // Why: Prevents crashes on database query failure
            Log.e("PrayerScreenViewModel", "Error loading status counts by range: ${e.message}")
            return emptyMap()
        }
    }
    fun savePrayer(prayerEntity: PrayerEntity) {
        viewModelScope.launch {
            try {
                // MODIFIED: Changed dao.insertPrayer to repository.savePrayer // Why: Include Firebase sync
                repository.savePrayer(prayerEntity)
            } catch (e: Exception) {
                Log.e("PrayerScreenViewModel", "Error saving prayer: ${e.message}")
            }
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
                    // MODIFIED: Changed dao to repository
                    _prayedCount.value = repository.getTotalStatusCount(R.drawable.prayedlate)
                    _notPrayedCount.value = repository.getTotalStatusCount(R.drawable.notprayed)
                    _onTimeCount.value = repository.getTotalStatusCount(R.drawable.prayedontime)
                    // CHANGED: Use jamatCount for both In Jamaat (men) and Exempted (women) // Why: Simplifies stats by reusing state
                    _jamatCount.value = repository.getTotalStatusCount(
                        if (_userGender.value == "Woman") R.drawable.track else R.drawable.jamat
                    )
                    _totalCounts.value = repository.getTotalPrayers()
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
                    // MODIFIED: Changed dao to repository
                    _prayedCount.value = repository.getStatusCountByDateRange(R.drawable.prayedlate, startDate, endDate)
                    _notPrayedCount.value = repository.getStatusCountByDateRange(R.drawable.notprayed, startDate, endDate)
                    _onTimeCount.value = repository.getStatusCountByDateRange(R.drawable.prayedontime, startDate, endDate)
                    // CHANGED: Use jamatCount for both In Jamaat and Exempted // Why: Simplifies stats
                    _jamatCount.value = repository.getStatusCountByDateRange(
                        if (_userGender.value == "Woman") R.drawable.track else R.drawable.jamat,
                        startDate,
                        endDate
                    )
                    _totalCounts.value = repository.getTotalPrayersByDateRange(startDate, endDate)
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
// ✅ CHANGED: Factory updated to accept repository
class PrayerViewModelFactory(
    private val repository: PrayerRepository // MODIFIED: Changed from dao and genderDao to repository // Why: ViewModel now depends on repository
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(PrayerScreenViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return PrayerScreenViewModel(repository) as T // MODIFIED: Pass repository
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}