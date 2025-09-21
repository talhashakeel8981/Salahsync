package com.example.salahsync.ui.Screens.OnBoarding

import android.content.Context
import android.util.Log
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
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.platform.LocalContext

import androidx.compose.ui.unit.dp
import androidx.navigation.NavController // NEW: Import NavController
import com.example.salahsync.DataBase.Gender
import com.example.salahsync.DataBase.GenderDao
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

@Composable
fun GenderSelectionScreen(
    navController: NavController,
    genderDao: GenderDao
) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()

    fun saveGenderAndNavigate(gender: String) {
        // ADDED: Log button tap // Why: Confirm button click is detected
        Log.d("GenderSelection", "Button tapped for gender: $gender")
        scope.launch {
            try {
                // ADDED: Log before insert // Why: Check if coroutine starts
                Log.d("GenderSelection", "Starting coroutine for insert")
                genderDao.insert(Gender(id = 0, genderName = gender))
                // ADDED: Log after insert // Why: Confirm insert completes
                Log.d("GenderSelection", "Insert completed for $gender")

                context.getSharedPreferences("app_prefs", Context.MODE_PRIVATE)
                    .edit()
                    .putBoolean("onboarding_done", true)
                    .apply()
                // ADDED: Log before navigation // Why: Check if navigation is reached
                Log.d("GenderSelection", "Setting onboarding_done and navigating")
                navController.navigate("topbottom") {
                    popUpTo("welcome") { inclusive = true }
                }
                // ADDED: Log after navigation // Why: Confirm navigation call
                Log.d("GenderSelection", "Navigation called for $gender")
            } catch (e: Exception) {
                // ADDED: Error handling // Why: Catch and log any exceptions preventing navigation
                Log.e("GenderSelection", "Error in saveGenderAndNavigate: ${e.message}", e)
            }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "Select your gender",
            style = MaterialTheme.typography.headlineMedium,
            color = MaterialTheme.colorScheme.onBackground
        )

        Spacer(modifier = Modifier.height(32.dp))

        Button(
            onClick = { saveGenderAndNavigate("Man") },
            modifier = Modifier
                .fillMaxWidth()
                .height(52.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = MaterialTheme.colorScheme.onPrimary
            )
        ) {
            Text("Man")
        }

        Spacer(modifier = Modifier.height(32.dp))

        Button(
            onClick = { saveGenderAndNavigate("Woman") },
            modifier = Modifier
                .fillMaxWidth()
                .height(52.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = MaterialTheme.colorScheme.onPrimary
            )
        ) {
            Text("Woman")
        }
    }
}