package com.example.salahsync.ui.Screens.SettingsOptions.DataBackup

import com.example.salahsync.DataBase.GenderDao
import com.example.salahsync.DataBase.PrayerDao
import android.util.Log
import com.example.salahsync.DataBase.Gender
import com.example.salahsync.DataBase.PrayerEntity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
// MODIFIED: Removed import com.google.firebase.firestore.FirebaseFirestore // Why: Switching to Realtime Database
import com.google.firebase.database.FirebaseDatabase // ADDED: Import FirebaseDatabase // Why: For Realtime Database operations
class PrayerRepository (
    private val prayerDao: PrayerDao,
    private val genderDao: GenderDao
)
{
    // 🔹 Firebase setup
    private val firebaseAuth = FirebaseAuth.getInstance()
    // MODIFIED: Changed firestore to db = FirebaseDatabase.getInstance().getReference() // Why: Use Realtime Database instead of Firestore
    private val db = FirebaseDatabase.getInstance().getReference()
    // ---------------- Save Prayer ----------------
    suspend fun savePrayer(prayer: PrayerEntity) {
        // ADDED: Check for existing prayer to decide insert or update // Why: Ensure correct handling since onConflict=REPLACE relies on unique index, but explicit check is safer
        val existing = prayerDao.getPrayersByDate(prayer.date).find { it.name == prayer.name }
        val toSave = prayer.copy(id = existing?.id ?: 0)
        if (existing != null) {
            prayerDao.updatePrayer(toSave)
        } else {
            // 1️⃣ Save to Room (local)
            prayerDao.insertPrayer(toSave)
        }
        // 2️⃣ Save to Firebase (cloud)
        val userId = firebaseAuth.currentUser?.uid ?: return
        try {
            // MODIFIED: Changed Firestore collection/document/set to Realtime child/setValue // Why: Adapt to Realtime Database structure
            db.child("Users")
                .child(userId)
                .child("prayers")
                .child("${prayer.date}_${prayer.name}") // unique by date + name
                .setValue(toSave)
                .addOnSuccessListener {
                    Log.d("PrayerRepository", "Prayer saved to Firebase: ${prayer.name}")
                }
                .addOnFailureListener { e ->
                    Log.e("PrayerRepository", "Failed to save prayer to Firebase", e)
                }
        } catch (e: Exception) {
            Log.e("PrayerRepository", "Error saving prayer", e)
        }
    }
    // ---------------- Get Prayers by Date ----------------
    suspend fun getPrayersByDate(date: String): List<PrayerEntity> {
        // ADDED: Delegate to DAO // Why: Centralize data access in repository for ViewModel
        return prayerDao.getPrayersByDate(date)
    }
    // ---------------- Gender ----------------
    suspend fun saveGender(gender: Gender) {
        // ADDED: Check for existing gender to decide insert or update // Why: Ensure only one gender entry, using explicit logic
        val existing = genderDao.getGender()
        val toSave = gender.copy(id = existing?.id ?: 0)
        if (existing != null) {
            genderDao.updateGender(toSave)
        } else {
            genderDao.insertGender(toSave)
        }
        val userId = firebaseAuth.currentUser?.uid ?: return
        try {
            // MODIFIED: Changed Firestore to Realtime Database // Why: Consistent with requirement
            db.child("Users")
                .child(userId)
                .child("settings")
                .child("gender")
                .setValue(toSave)
                .addOnSuccessListener {
                    Log.d("PrayerRepository", "Gender saved to Firebase: ${gender.genderName}")
                }
                .addOnFailureListener { e ->
                    Log.e("PrayerRepository", "Failed to save gender", e)
                }
        } catch (e: Exception) {
            Log.e("PrayerRepository", "Error saving gender", e)
        }
    }
    suspend fun getGender(): Gender? {
        // ADDED: Delegate to DAO // Why: Centralize access for ViewModel
        return genderDao.getGender()
    }
    // ADDED: Delegate count queries from PrayerDao // Why: ViewModel now uses repository instead of direct DAO
    suspend fun getPrayerStatusCount(prayerName: String, statusRes: Int): Int {
        return prayerDao.getPrayerStatusCount(prayerName, statusRes)
    }
    suspend fun getTotalStatusCount(statusRes: Int): Int {
        return prayerDao.getTotalStatusCount(statusRes)
    }
    suspend fun getTotalPrayers(): Int {
        return prayerDao.getTotalPrayers()
    }
    suspend fun getPrayerStatusCountByDateRange(prayerName: String, statusRes: Int, startDate: String, endDate: String): Int {
        return prayerDao.getPrayerStatusCountByDateRange(prayerName, statusRes, startDate, endDate)
    }
    suspend fun getStatusCountByDateRange(statusRes: Int, startDate: String, endDate: String): Int {
        return prayerDao.getStatusCountByDateRange(statusRes, startDate, endDate)
    }
    suspend fun getTotalPrayersByDateRange(startDate: String, endDate: String): Int {
        return prayerDao.getTotalPrayersByDateRange(startDate, endDate)
    }
    // ADDED: Get all prayers // Why: Needed for syncLocalToCloud to upload all local data
    suspend fun getAllPrayers(): List<PrayerEntity> {
        return prayerDao.getAllPrayers()
    }
    // ADDED: Private method to save prayer only to Firebase // Why: For sync without duplicating local insert
    private fun savePrayerToFirebase(prayer: PrayerEntity, userId: String) {
        db.child("Users")
            .child(userId)
            .child("prayers")
            .child("${prayer.date}_${prayer.name}")
            .setValue(prayer)
            .addOnSuccessListener {
                Log.d("PrayerRepository", "Prayer synced to Firebase: ${prayer.name}")
            }
            .addOnFailureListener { e ->
                Log.e("PrayerRepository", "Failed to sync prayer to Firebase", e)
            }
    }
    // ADDED: Private method to save gender only to Firebase // Why: For sync without duplicating local insert
    private fun saveGenderToFirebase(gender: Gender, userId: String) {
        db.child("Users")
            .child(userId)
            .child("settings")
            .child("gender")
            .setValue(gender)
            .addOnSuccessListener {
                Log.d("PrayerRepository", "Gender synced to Firebase: ${gender.genderName}")
            }
            .addOnFailureListener { e ->
                Log.e("PrayerRepository", "Failed to sync gender to Firebase", e)
            }
    }
    // ADDED: Sync local data to cloud // Why: Upload all gender and prayers to Firebase on login/register
    suspend fun syncLocalToCloud() {
        val userId = firebaseAuth.currentUser?.uid ?: return
        val gender = getGender()
        if (gender != null) {
            saveGenderToFirebase(gender, userId)
        }
        val allPrayers = getAllPrayers()
        for (prayer in allPrayers) {
            savePrayerToFirebase(prayer, userId)
        }
        Log.d("PrayerRepository", "Sync completed for user $userId")
    }
}