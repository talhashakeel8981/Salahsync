//package com.example.salahsync.ui.Screens.Setting.Appearence
//
//import android.content.Context
//import android.content.res.Resources.Theme
//import androidx.compose.runtime.Composable
//
//
//class ThemeManager(context: Context) {
//    private val pref = context.getSharedPreferences("themePreference", Context.MODE_PRIVATE)
//
//    fun saveTheme(themeMode: Themes) {
//        pref.edit().putString("themeMode", themeMode.name).apply()
//    }
//
//    fun getTheme(): Themes {
//        val value = pref.getString("themeMode", Themes.SYSTEM.name)
//        return Themes.valueOf(value ?: Themes.SYSTEM.name)
//    }
//}