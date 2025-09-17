package com.example.salahsync.ui.Screens

import android.util.Log
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier


import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text

import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue

import com.google.firebase.database.FirebaseDatabase   // ✅ ye line zaroori hai

@Composable
fun FirebaseSyncCheck() {
    var message by remember { mutableStateOf("Checking Firebase...") }
    val database = FirebaseDatabase.getInstance()
    val myRef = database.getReference("testNode")

    LaunchedEffect(Unit) {
        myRef.setValue("Firbase test data has been sync with Firbase") // write example
        myRef.get().addOnSuccessListener {
            message = "✅ Firebase Sync Ho gaya!"
        }.addOnFailureListener {
            message = "❌ Firebase Sync Failed!"
        }
    }

    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Text(text = message)
    }
}


internal fun testFirebase() {
    val database = FirebaseDatabase.getInstance()
    val myRef = database.getReference("testNode")

    myRef.setValue("Hello Firebase from testFirebase()")
        .addOnSuccessListener {
            Log.d("FirebaseTest", "✅ Firebase write success")
        }
        .addOnFailureListener {
            Log.e("FirebaseTest", "❌ Firebase write failed", it)
        }
}