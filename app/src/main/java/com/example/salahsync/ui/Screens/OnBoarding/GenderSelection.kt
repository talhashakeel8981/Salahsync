package com.example.salahsync.ui.Screens.OnBoarding

import android.content.Context
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults

import androidx.compose.material3.Text
import androidx.compose.ui.platform.LocalContext

import androidx.compose.ui.unit.dp
import androidx.navigation.NavController // NEW: Import NavController
import com.example.salahsync.DataBase.Gender
import com.example.salahsync.DataBase.GenderDao
import kotlinx.coroutines.runBlocking

@Composable
fun GenderSelectionScreen(
    navController: NavController,
    genderDao: GenderDao
) {
    val context = LocalContext.current

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background) // 🔥 ADDED
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "Select your gender",
            style = MaterialTheme.typography.headlineMedium,
            color = MaterialTheme.colorScheme.onBackground // 🔥 FIXED
        )

        Spacer(modifier = Modifier.height(32.dp))

        Button(
            onClick = {
                runBlocking {
                    genderDao.insert(Gender(id = 0, genderName = "Man"))
                }

                context.getSharedPreferences("app_prefs", Context.MODE_PRIVATE)
                    .edit()
                    .putBoolean("onboarding_done", true)
                    .apply()

                navController.navigate("topbottom") {
                    popUpTo("welcome") { inclusive = true }
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(52.dp),
            colors = ButtonDefaults.buttonColors( // 🔥 THEME COLORS
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = MaterialTheme.colorScheme.onPrimary
            )
        ) {
            Text("Man")
        }

        Spacer(modifier = Modifier.height(32.dp))

        Button(
            onClick = {
                runBlocking {
                    genderDao.insert(Gender(id = 0, genderName = "Woman"))
                }

                context.getSharedPreferences("app_prefs", Context.MODE_PRIVATE)
                    .edit()
                    .putBoolean("onboarding_done", true)
                    .apply()

                navController.navigate("topbottom") {
                    popUpTo("welcome") { inclusive = true }
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(52.dp),
            colors = ButtonDefaults.buttonColors( // 🔥 THEME COLORS
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = MaterialTheme.colorScheme.onPrimary
            )
        ) {
            Text("Woman")
        }
    }
}