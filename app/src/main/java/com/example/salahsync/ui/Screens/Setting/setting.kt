package com.example.salahsync.ui.Screens.Setting

//import androidx.compose.foundation.R
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.salahsync.ui.Screens.SettingsOptions.SettingItem
import com.example.salahsync.ui.Screens.SettingsOptions.SettingSection
import com.example.salahsync.R
import androidx.compose.material.*
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.TopAppBarDefaults

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingScreen(
    onNotificationsClick: () -> Unit,
    onManageDeedsClick: () -> Unit,
    appearence:()->Unit,
    onHapticClick: () -> Unit,
    onPrivacyPolicyClick: () -> Unit,
    feedback:()->Unit,
    tracking:()->Unit,
    invite:()-> Unit,
    emailfeedback:()->Unit,
    databackup:()-> Unit,
    rateus:()-> Unit
) {
    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("Settings") },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surface,
                    titleContentColor = MaterialTheme.colorScheme.onSurface
                )
                // If you want a shadow: add `modifier = Modifier.shadow(4.dp)`
            )
        }
    ) { innerPadding ->

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp) // ⬅ space between items
        ) {

            item {
                SettingItem(
                    icon = rememberVectorPainter(Icons.Filled.Notifications),
                    title = "Notifications",
                    onClick = onNotificationsClick
                )
            }
            item {
                SettingItem(
                    icon = rememberVectorPainter(Icons.Default.Menu),
                    title = "Manage Deeds",
                    onClick = onManageDeedsClick
                )
            }
            item{ SettingItem(icon= painterResource(R.drawable.cloud),title="Data Backup",onClick=databackup) }

            item { SettingItem(icon = painterResource(R.drawable.appearence), title = "Appearance", onClick = appearence) }
            item { SettingItem(icon = painterResource(R.drawable.track), title = "Tracking Reason", onClick = tracking)}
            item {
                SettingItem(
                    icon = painterResource(R.drawable.invite),
                    title = "Invite Friends",
                    onClick = invite
                )
            }
            item{ SettingItem(
                icon = painterResource(R.drawable.sendfeedback),
                title = "Send Feedback",
                onClick = emailfeedback)
            }
            item { SettingItem(icon = painterResource(R.drawable.rate), title = "Love Salahsync? Rate Us", onClick = rateus )}
            item { SettingItem(icon = painterResource(R.drawable.privacypolicy), title = "Privacy policy", onClick = onPrivacyPolicyClick) }
        }
    }
}
