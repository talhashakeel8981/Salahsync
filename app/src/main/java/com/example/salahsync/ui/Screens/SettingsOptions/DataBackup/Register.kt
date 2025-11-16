package com.example.salahsync.ui.Screens.SettingsOptions.DataBackup

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.layout.size
import androidx.compose.ui.unit.sp

@SuppressLint("UnrememberedMutableState")
@Composable
fun RegisterScreen(
    onRegisterClick: (String, String, String) -> Unit,
    onNavigateToLogin: () -> Unit,
    viewModel: AuthViewModel
) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }

    // validation error messages for each field
    var emailError by remember { mutableStateOf<String?>(null) }       // shows email-related error
    var passwordError by remember { mutableStateOf<String?>(null) }   // shows password-related error
    var confirmError by remember { mutableStateOf<String?>(null) }    // shows confirm password error

    val isLoading by derivedStateOf { viewModel.isLoading.value }
    val error by derivedStateOf { viewModel.errorMessage.value }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(20.dp),
        verticalArrangement = Arrangement.Center
    ) {
        Text("Register", style = MaterialTheme.typography.titleLarge)

        Spacer(modifier = Modifier.height(16.dp))

        // ---------------- EMAIL FIELD ----------------
        OutlinedTextField(
            value = email,
            onValueChange = {
                email = it
                emailError = null // clear error when user types again
            },
            label = { Text("Email") },
            modifier = Modifier.fillMaxWidth(),
            isError = emailError != null  // highlight red border if error exists
        )
        if (emailError != null) {
            Text(emailError ?: "", color = MaterialTheme.colorScheme.error, fontSize = 12.sp)
        }

        Spacer(modifier = Modifier.height(8.dp))

        // ---------------- PASSWORD FIELD ----------------
        OutlinedTextField(
            value = password,
            onValueChange = {
                password = it
                passwordError = null
            },
            label = { Text("Password") },
            modifier = Modifier.fillMaxWidth(),
            visualTransformation = PasswordVisualTransformation(),
            isError = passwordError != null
        )
        if (passwordError != null) {
            Text(passwordError ?: "", color = MaterialTheme.colorScheme.error, fontSize = 12.sp)
        }

        Spacer(modifier = Modifier.height(8.dp))

        // ---------------- CONFIRM PASSWORD FIELD ----------------
        OutlinedTextField(
            value = confirmPassword,
            onValueChange = {
                confirmPassword = it
                confirmError = null
            },
            label = { Text("Confirm Password") },
            modifier = Modifier.fillMaxWidth(),
            visualTransformation = PasswordVisualTransformation(),
            isError = confirmError != null
        )
        if (confirmError != null) {
            Text(confirmError ?: "", color = MaterialTheme.colorScheme.error, fontSize = 12.sp)
        }

        Spacer(modifier = Modifier.height(16.dp))

        // --------------- GENERAL ERROR MESSAGE ---------------
        if (error != null) {
            Text(text = error ?: "", color = MaterialTheme.colorScheme.error)
            Spacer(Modifier.height(8.dp))
        }

        // --------------- REGISTER BUTTON ---------------
        Button(
            onClick = {
                // VALIDATION LOGIC
                var valid = true

                // 1️⃣ Email validation
                if (email.isBlank()) {
                    emailError = "Email cannot be empty"
                    valid = false
                } else if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                    emailError = "Enter a valid email"
                    valid = false
                }

                // 2️⃣ Password validation
                if (password.isBlank()) {
                    passwordError = "Password cannot be empty"
                    valid = false
                } else if (password.length < 6) {
                    passwordError = "Password must be at least 6 characters"
                    valid = false
                }

                // 3️⃣ Confirm Password validation
                if (confirmPassword.isBlank()) {
                    confirmError = "Please confirm your password"
                    valid = false
                } else if (confirmPassword != password) {
                    confirmError = "Passwords do not match"
                    valid = false
                }

                // if all are valid, then call register
                if (valid) {
                    onRegisterClick(email.trim(), password, confirmPassword)
                }
            },
            enabled = !isLoading,
            modifier = Modifier.fillMaxWidth()
        ) {
            if (isLoading) CircularProgressIndicator(modifier = Modifier.size(20.dp))
            else Text("Register")
        }

        // --------------- NAVIGATION LINK ---------------
        TextButton(
            onClick = onNavigateToLogin,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Already have an account? Login")
        }
    }
}
