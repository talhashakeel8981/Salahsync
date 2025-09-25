package com.example.salahsync.ui.Screens.SettingsOptions.DataBackup

import com.example.salahsync.DataBase.GenderDao
import com.example.salahsync.DataBase.PrayerDao
import android.util.Log
import com.example.salahsync.DataBase.Gender
import com.example.salahsync.DataBase.PrayerEntity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
class PrayerRepository (
    private val prayerDao: PrayerDao,
    private val genderDao: GenderDao
)
{
    // 🔹 Firebase setup
    private val firebaseAuth = FirebaseAuth.getInstance()
    private val firestore = FirebaseFirestore.getInstance()


    // ---------------- Save Prayer ----------------
    suspend fun savePrayer(prayer: PrayerEntity) {
        // 1️⃣ Save to Room (local)
        prayerDao.insertPrayer(prayer)

        // 2️⃣ Save to Firebase (cloud)
        val userId = firebaseAuth.currentUser?.uid ?: return
        try {
            firestore.collection("users")
                .document(userId)
                .collection("prayers")
                .document("${prayer.date}_${prayer.name}") // unique by date + name
                .set(prayer)
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
        return prayerDao.getPrayersByDate(date)
    }


    // ---------------- Gender ----------------
    suspend fun saveGender(gender: Gender) {
        genderDao.insertGender(gender)

        val userId = firebaseAuth.currentUser?.uid ?: return
        try {
            firestore.collection("users")
                .document(userId)
                .collection("settings")
                .document("gender")
                .set(gender)
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
        return genderDao.getGender()
    }
}