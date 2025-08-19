package com.example.salahsync
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
//import com.example.salahsync.ui.Screens.SettingNavigation
import com.example.salahsync.ui.Screens.TopBottom

import com.example.salahsync.ui.Space.DatabaseProvider
import com.example.salahsync.ui.Space.User
import com.example.salahsync.ui.Space.UserListScreen
//import com.example.salahsync.ui.Screens.
import com.example.salahsync.ui.theme.SalahsyncTheme
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlin.text.get
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.launch
//class MainActivity : ComponentActivity() {
//    @RequiresApi(Build.VERSION_CODES.O)
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        enableEdgeToEdge()
//        setContent {
////            PrayerScreen()
//            TopBottom()
//
//        }
//    }
//}
import androidx.lifecycle.lifecycleScope
import com.example.salahsync.ui.Space.UserInputScreen
import kotlinx.coroutines.launch
import androidx.compose.foundation.lazy.items
import kotlinx.coroutines.withContext

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val db = DatabaseProvider.getDatabase(this)
        val userDao = db.userDao()

        setContent {
            val usersState = remember { mutableStateListOf<User>() }

            // Load users initially
            LaunchedEffect(Unit) {
                usersState.clear()
                usersState.addAll(userDao.getAllUsers())
            }

            Column {
                UserInputScreen(userDao, this@MainActivity) {
                    // Refresh when a new user is saved
                    CoroutineScope(Dispatchers.IO).launch {
                        val newUsers = userDao.getAllUsers()
                        withContext(Dispatchers.Main) {
                            usersState.clear()
                            usersState.addAll(newUsers)
                        }
                    }
                }

                LazyColumn {
                    items(usersState) { user ->
                        Text(text = "Name: ${user.name}, Pass: ${user.password}")
                    }
                }
            }
        }
    }
}






//@Preview(showBackground = true)
//@Composable
//fun GreetingPreview() {
//    SalahsyncTheme {
//        Greeting("Android")
//    }
//}