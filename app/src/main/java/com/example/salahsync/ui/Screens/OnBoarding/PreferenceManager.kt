package com.example.salahsync.ui.Screens.OnBoarding

import android.content.Context

object PrefsManager {
    private const val PREFS_NAME = "salahsync_prefs"
    private const val KEY_ONBOARDING_DONE = "onboarding_done"

    fun setOnboardingDone(context: Context, done: Boolean) {
        val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        prefs.edit().putBoolean(KEY_ONBOARDING_DONE, done).apply()
    }

    fun isOnboardingDone(context: Context): Boolean {
        val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        return prefs.getBoolean(KEY_ONBOARDING_DONE, false)
    }
}