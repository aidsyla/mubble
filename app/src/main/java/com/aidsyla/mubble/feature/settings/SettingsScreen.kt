package com.aidsyla.mubble.feature.settings

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.ThumbUp
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SegmentedButton
import androidx.compose.material3.SegmentedButtonDefaults
import androidx.compose.material3.SingleChoiceSegmentedButtonRow
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsStartContent(
    modifier: Modifier = Modifier,
    onNavigateToNotifications: () -> Unit,
    onNavigateToDevicePermissions: () -> Unit,
    onNavigateToManageAccount: () -> Unit,
    onLogoutClick: () -> Unit = {},
    onBackClick: () -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text("Settings")
                },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) {
        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(it),
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Spacer(modifier = Modifier.height(8.dp))
            SegmentedButtonDetails(
                icon = Icons.Default.Refresh,
                description = "Display Mode",
                buttons = listOf("Auto", "Dark", "Light")
            )
            SettingItem(
                description = "Notifications",
                icon = Icons.Default.Notifications,
                onClick = onNavigateToNotifications
            )
            SettingItem(
                description = "Device Permissions",
                icon = Icons.Default.ThumbUp,
                onClick = onNavigateToDevicePermissions
            )
            SettingItem(
                description = "Manage Account",
                icon = Icons.Default.Edit,
                onClick = onNavigateToManageAccount
            )
            SettingItem(
                description = "Log out",
                icon = Icons.Default.ExitToApp,
                clickable = false
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsNotificationsContent(
    onBackClick: () -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text("Notifications")
                },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(it)
        ) {
            Row(
                modifier = Modifier.padding(start = 16.dp, end = 12.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp, Alignment.CenterHorizontally)
            ) {
                Icon(imageVector = Icons.Default.Notifications, null)
                Text("Enable Notifications", style = MaterialTheme.typography.bodyLarge)
                Spacer(modifier = Modifier.weight(1f))
                Switch(
                    checked = true,
                    thumbContent = {
                        Icon(
                            Icons.Default.Check,
                            contentDescription = null,
                            modifier = Modifier.size(16.dp)
                        )
                    },
                    onCheckedChange = {}
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsDevicePermissionsContent(
    onBackClick: () -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text("Device Permissions")
                },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(it)
        ) {
            SettingItem(
                clickable = false,
                description = "Photos",
                isAllowed = "Allowed",
                icon = Icons.Default.Phone
            )
            SettingItem(
                clickable = false,
                description = "Camera",
                isAllowed = "Not allowed",
                icon = Icons.Default.Star
            )
            SettingItem(
                clickable = false,
                description = "Microphone",
                isAllowed = "Allowed",
                icon = Icons.Default.AccountCircle
            )
        }
    }

}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsManageAccountContent(
    modifier: Modifier = Modifier,
    onBackClick: () -> Unit,
    onClick: () -> Unit = {},
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text("Manage Account")
                },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(it),
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Spacer(modifier = Modifier.height(8.dp))
            SegmentedButtonDetails(
                icon = Icons.Default.Lock,
                description = "Account visibility",
                buttons = listOf("Private", "Public")
            )
            SettingItem(
                onClick = onClick,
                description = "Delete Account",
                icon = Icons.Default.Delete
            )
        }
    }
}

@Composable
fun SegmentedButtonDetails(
    modifier: Modifier = Modifier,
    icon: ImageVector,
    description: String,
    buttons: List<String>,
) {
    Column(
        modifier = Modifier.padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(icon, null)
            Text(description, style = MaterialTheme.typography.bodyLarge)
        }
        SingleChoiceSegmentedButtonRow {
            buttons.forEachIndexed { index, s ->
                SegmentedButton(
                    selected = index == 0,
                    onClick = {},
                    shape = SegmentedButtonDefaults.itemShape(index, buttons.size)
                ) { Text(s) }
            }
        }
    }
}

@Composable
fun SettingItem(
    modifier: Modifier = Modifier,
    clickable: Boolean = true,
    onClick: () -> Unit = {},
    description: String,
    isAllowed: String = "",
    icon: ImageVector,
) {
    val rowModifier = if (clickable) {
        modifier
            .clickable { onClick() }
            .padding(start = 16.dp, end = 4.dp)
    } else {
        modifier.padding(start = 16.dp, end = 4.dp)
    }

    Row(
        modifier = rowModifier,
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp, Alignment.CenterHorizontally)
    ) {
        Icon(imageVector = icon, contentDescription = null)
        Text(description, style = MaterialTheme.typography.bodyLarge)
        Spacer(modifier = Modifier.weight(1f))
        Text(isAllowed, style = MaterialTheme.typography.labelSmall)
        IconButton(onClick = {}) {
            Icon(Icons.AutoMirrored.Filled.ArrowForward, contentDescription = null)
        }
    }
}
