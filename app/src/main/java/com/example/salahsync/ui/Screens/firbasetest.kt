package com.example.salahsync.ui.Screens

import com.google.firebase.firestore.FirebaseFirestore

class firbasetest {

    private val db = FirebaseFirestore.getInstance()

    // Add user
    fun addUser(name: String, age: Int, onResult: (Boolean) -> Unit) {
        val user = hashMapOf(
            "name" to name,
            "age" to age
        )

        db.collection("users")
            .add(user)
            .addOnSuccessListener {
                onResult(true)
            }
            .addOnFailureListener {
                onResult(false)
            }
    }

    // Get users
    fun getUsers(onResult: (List<Map<String, Any>>) -> Unit) {
        db.collection("users")
            .get()
            .addOnSuccessListener { result ->
                val users = result.map { it.data }
                onResult(users)
            }
            .addOnFailureListener {
                onResult(emptyList())
            }
    }
}