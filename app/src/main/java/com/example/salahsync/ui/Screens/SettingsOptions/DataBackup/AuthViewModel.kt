package com.example.salahsync.ui.Screens.SettingsOptions.DataBackup

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel


import androidx.lifecycle.viewModelScope // ADDED: Import viewModelScope // Why: To launch coroutine for sync after auth success
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import kotlinx.coroutines.launch // ADDED: Import launch // Why: For coroutine in signIn/signUp
import com.example.salahsync.ui.Screens.SettingsOptions.DataBackup.PrayerRepository // ADDED: Import PrayerRepository // Why: To call syncLocalToCloud after auth
class AuthViewModel(
    // ADDED: Constructor parameter for repository // Why: To access syncLocalToCloud after successful auth
    private val repository: PrayerRepository
) : ViewModel() {
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
                    // ADDED: Sync local data to Firebase after successful sign-up // Why: Upload user data (gender, prayers) along with email registration
                    viewModelScope.launch {
                        repository.syncLocalToCloud()
                    }
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
                    // ADDED: Sync local data to Firebase after successful sign-in // Why: Upload user data (gender, prayers) when logging in
                    viewModelScope.launch {
                        repository.syncLocalToCloud()
                    }
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
// ADDED: Factory for AuthViewModel // Why: To provide repository dependency when creating the ViewModel
class AuthViewModelFactory(
    private val repository: PrayerRepository
) : androidx.lifecycle.ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(AuthViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return AuthViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}