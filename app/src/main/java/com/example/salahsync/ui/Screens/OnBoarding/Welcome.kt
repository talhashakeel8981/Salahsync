package com.example.salahsync.ui.Screens.OnBoarding

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController


/**
 * Welcome screen shown after splash / gender selection.
 */
@Composable
fun WelcomeScreen(
    navController: NavController // 👈 Add NavController for navigation
) {
    // Full page column centered both vertically and horizontally
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        // 👋 Greeting text
        Text(
            text = "Welcome to the app..",
            style = MaterialTheme.typography.headlineLarge
        )

        Spacer(modifier = Modifier.height(24.dp))

        // Heading
        Text(
            text = "Who are you?",
            style = MaterialTheme.typography.headlineMedium
        )

        Spacer(modifier = Modifier.height(24.dp))

        // Next Button → Navigate to TopBottom
        Button(onClick = {
            navController.navigate("topbottom") {  // 👈 destination key
                popUpTo("welcome") { inclusive = true } // remove welcome from back stack
            }
        }) {
            Text("Next")
        }
    }
}