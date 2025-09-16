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

/**
 * Onboarding → Gender selection screen.
 * Shown after Welcome screen.
 */
@Composable
fun GenderSelectionScreen(
    onBrotherClick: () -> Unit,
    onSisterClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "Select your gender",
            style = MaterialTheme.typography.headlineMedium
        )

        Spacer(modifier = Modifier.height(32.dp))

        Button(
            onClick = onBrotherClick,
            modifier = Modifier
                .fillMaxWidth()
                .height(52.dp)
        ) {
            Text("Brother")
        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = onSisterClick,
            modifier = Modifier
                .fillMaxWidth()
                .height(52.dp)
        ) {
            Text("Sister")
        }
    }
}

@Preview(showBackground = true)
@Composable
fun GenderSelectionPreview() {
    MaterialTheme {
        GenderSelectionScreen(
            onBrotherClick = {},
            onSisterClick = {}
        )
    }
}