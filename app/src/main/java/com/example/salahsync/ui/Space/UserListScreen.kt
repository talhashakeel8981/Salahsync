package com.example.salahsync.ui.Space

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.foundation.lazy.items
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun UserListScreen(userDao: UserDao) {
    val users = remember { mutableStateListOf<User>() }

    // LaunchedEffect ko ek function banake refresh karo
    @Composable
    fun loadUsers() {
        // background me coroutine
        LaunchedEffect(Unit) {
            users.clear()
            users.addAll(userDao.getAllUsers())
        }
    }

    // Call first time
    loadUsers()

    Column(modifier = Modifier.padding(16.dp)) {
        Text("Users in Database:")

        LazyColumn {
            items(users) { user ->
                Text(text = "Name: ${user.name}, Pass: ${user.password}")
            }
        }
    }
}


