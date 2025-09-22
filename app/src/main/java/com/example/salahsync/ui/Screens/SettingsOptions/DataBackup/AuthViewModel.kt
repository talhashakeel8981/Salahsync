package com.example.salahsync.ui.Screens.SettingsOptions.DataBackup

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser

class AuthViewModel : ViewModel() {

    private val auth: FirebaseAuth = FirebaseAuth.getInstance()

    // observable states to drive the UI
    val currentUser = mutableStateOf<FirebaseUser?>(auth.currentUser)
    val isLoading = mutableStateOf(false)
    val errorMessage = mutableStateOf<String?>(null)

    fun signUp(email: String, password: String, onResult: (Boolean) -> Unit) {
        isLoading.value = true
        errorMessage.value = null

        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                isLoading.value = false
                if (task.isSuccessful) {
                    currentUser.value = auth.currentUser
                    onResult(true)
                } else {
                    errorMessage.value = task.exception?.localizedMessage ?: "Register failed"
                    onResult(false)
                }
            }
    }

    fun signIn(email: String, password: String, onResult: (Boolean) -> Unit) {
        isLoading.value = true
        errorMessage.value = null

        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                isLoading.value = false
                if (task.isSuccessful) {
                    currentUser.value = auth.currentUser
                    onResult(true)
                } else {
                    errorMessage.value = task.exception?.localizedMessage ?: "Login failed"
                    onResult(false)
                }
            }
    }

    fun signOut() {
        auth.signOut()
        currentUser.value = null
    }
}