package com.example.salahsync.ui.Space


import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.launch
import androidx.activity.ComponentActivity

@Composable
fun UserInputScreen(
    userDao: UserDao,
    activity: ComponentActivity,
    onSaved: () -> Unit // ❌ remove @Composable
) {
    var username by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    Column(modifier = Modifier.padding(16.dp)) {
        OutlinedTextField(
            value = username,
            onValueChange = { username = it },
            label = { Text("Username") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = password,
            onValueChange = { password = it },
            label = { Text("Password") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        Button(onClick = {
            activity.lifecycleScope.launch {
                userDao.insertUser(User(name = username, password = password))
                onSaved() // call to refresh list
            }
            username = ""
            password = ""
        }, modifier = Modifier.fillMaxWidth()) {
            Text("Save User")
        }
    }
}
