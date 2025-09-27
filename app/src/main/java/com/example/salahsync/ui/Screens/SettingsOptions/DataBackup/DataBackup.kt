package com.example.salahsync.ui.Screens.SettingsOptions.DataBackup

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import com.example.salahsync.R


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DataBackupScreen(
    onBack: () -> Unit,
    viewModel: AuthViewModel // Inject AuthViewModel
) {
    // State to manage which screen to show
    var currentScreen by remember { mutableStateOf("DataBackup") }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Text("Data Backup")
                    }
                },
                navigationIcon = {
                    IconButton(onClick = {
                        if (currentScreen == "DataBackup") {
                            onBack() // Go back to previous screen
                        } else {
                            currentScreen = "DataBackup" // Go back to DataBackup content
                        }
                    }) {
                        Icon(
                            painter = painterResource(id = R.drawable.leftarrow),
                            contentDescription = "Back",
                            tint = Color.Black
                        )
                    }
                }
            )
        }
    ) { innerPadding ->
        when (currentScreen) {
            "DataBackup" -> {
                // Default DataBackupScreen content
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(innerPadding)
                        .padding(16.dp),
                    verticalArrangement = Arrangement.Center
                ) {
                    // Check if user is logged in
                    if (viewModel.currentUser.value != null) {
                        // Show HomeScreen content if logged in
                        HomeScreen(
                            onSignOut = { viewModel.signOut() },
                            viewModel = viewModel
                        )
                    } else {
                        // Show button to trigger LoginScreen
                        Button(
                            onClick = { currentScreen = "Login" },
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text("Login to Enable Data Backup")
                        }
                    }
                }
            }
            "Login" -> {
                LoginScreen(
                    onLoginClick = { email, password ->
                        viewModel.signIn(email, password) { success ->
                            if (success) {
                                currentScreen = "DataBackup" // Go back to DataBackup after login
                            }
                        }
                    },
                    onNavigateToRegister = { currentScreen = "Register" },
                    viewModel = viewModel
                )
            }
            "Register" -> {
                RegisterScreen(
                    onRegisterClick = { email, password, confirmPassword ->
                        if (password == confirmPassword) {
                            viewModel.signUp(email, password) { success ->
                                if (success) {
                                    currentScreen = "DataBackup" // Go back to DataBackup after register
                                }
                            }
                        } else {
                            viewModel.errorMessage.value = "Your Passwords Don't Matched!"
                        }
                    },
                    onNavigateToLogin = { currentScreen = "Login" },
                    viewModel = viewModel
                )
            }
        }
    }
}