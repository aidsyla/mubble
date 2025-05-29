package com.aidsyla.mubble.feature.settings

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.unit.dp
import com.aidsyla.mubble.common.components.TabButtons
import com.aidsyla.mubble.ui.theme.MubbleTheme

@Composable
fun SettingsStartScreen(
    modifier: Modifier = Modifier,
    onNavigateToNotifications: () -> Unit,
    onNavigateToDevicePermissions: () -> Unit,
    onNavigateToManageAccount: () -> Unit,
    onLogoutClick: () -> Unit = {},
    onBackClick: () -> Unit,
) {
    Scaffold(
        topBar = {
            SettingsTopBar(title = "Settings", onBackClick = onBackClick)
        }
    ) {
        ReusableLazyColumn(paddingValues = it) {
            item {
                Column(
                    modifier = Modifier.padding(horizontal = 16.dp),
                ) {
                    Row(
                        modifier = Modifier.height(48.dp),
                        horizontalArrangement = Arrangement.spacedBy(
                            12.dp,
                            Alignment.CenterHorizontally
                        ),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(MubbleTheme.Icons.Appearance, null)
                        Text("Appearance", style = MaterialTheme.typography.bodyLarge)
                    }
                    TabButtons(
                        options = listOf("Auto", "Dark", "Light"),
                        selectedIcons = MubbleTheme.AppearanceTabs.iconsSelected,
                        unselectedIcons = MubbleTheme.AppearanceTabs.icons,
                        selectedIndex = 0,
                        onTabSelected = {}
                    )
                }
            }
            item {
                HorizontalDivider(modifier = Modifier.padding(top = 16.dp, bottom = 8.dp))
            }
            item {
                SettingItem(
                    description = "Notifications",
                    icon = MubbleTheme.Icons.Notification,
                    onClick = onNavigateToNotifications
                )
            }
            item {
                SettingItem(
                    description = "Device Permissions",
                    icon = MubbleTheme.Icons.DevicePermissions,
                    onClick = onNavigateToDevicePermissions
                )
            }
            item {
                SettingItem(
                    description = "Manage Account",
                    icon = MubbleTheme.Icons.ManageAccount,
                    onClick = onNavigateToManageAccount
                )
            }
            item {
                SettingItem(
                    description = "Log out",
                    icon = MubbleTheme.Icons.Logout,
                )
            }
        }
    }
}

@Composable
fun SettingsNotificationsScreen(
    onBackClick: () -> Unit,
) {
    Scaffold(
        topBar = {
            SettingsTopBar(title = "Notifications", onBackClick = onBackClick)
        }
    ) {
        ReusableLazyColumn(paddingValues = it) {
            item {
                Row(
                    modifier = Modifier.padding(start = 16.dp, end = 8.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(
                        12.dp,
                        Alignment.CenterHorizontally
                    )
                ) {
                    Icon(painter = MubbleTheme.Icons.Notification, null)
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
}

@Composable
fun SettingsDevicePermissionsScreen(
    onBackClick: () -> Unit,
) {
    Scaffold(
        topBar = {
            SettingsTopBar(title = "Device Permissions", onBackClick = onBackClick)
        }
    ) {
        ReusableLazyColumn(paddingValues = it) {
            item {
                SettingItem(
                    modifier = Modifier.padding(end = 8.dp),
                    description = "Photos",
                    icon = MubbleTheme.Icons.PhotoLibrary
                ) {
                    Text("Not allowed", style = MaterialTheme.typography.labelSmall)
                }
            }
            item {
                SettingItem(
                    modifier = Modifier.padding(end = 8.dp),
                    description = "Camera",
                    icon = MubbleTheme.Icons.Camera
                ) {
                    Text("Allowed", style = MaterialTheme.typography.labelSmall)
                }
            }
        }
    }
}

@Composable
fun SettingsManageAccountScreen(
    modifier: Modifier = Modifier,
    onBackClick: () -> Unit,
    onClick: () -> Unit = {},
) {
    val options = listOf("Public", "Private")
    Scaffold(
        topBar = {
            SettingsTopBar(title = "Manage Account", onBackClick = onBackClick)
        }
    ) {
        ReusableLazyColumn(paddingValues = it) {
            item {
                TabButtonWithDescription(
                    description = "Account visibility",
                    icon = MubbleTheme.Icons.AccountVisibility,
                    options = options,
                    selectedIcons = MubbleTheme.AccountVisibility.iconsSelected,
                    unselectedIcons = MubbleTheme.AccountVisibility.icons,
                    selectedIndex = 0,
                    onTabSelected = {}
                )
            }
            item {
                HorizontalDivider(modifier = Modifier.padding(top = 16.dp, bottom = 8.dp))
            }
            item {
                SettingItem(
                    onClick = onClick,
                    description = "Delete Account",
                    icon = MubbleTheme.Icons.Delete
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsTopBar(
    modifier: Modifier = Modifier,
    title: String,
    onBackClick: () -> Unit,
) {
    CenterAlignedTopAppBar(
        modifier = modifier,
        title = {
            Text(text = title)
        },
        navigationIcon = {
            IconButton(onClick = onBackClick) {
                Icon(MubbleTheme.Icons.ArrowBack, contentDescription = "Back")
            }
        }
    )
}

@Composable
private fun ReusableLazyColumn(
    modifier: Modifier = Modifier,
    paddingValues: PaddingValues,
    content: LazyListScope.() -> Unit,
) {
    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .padding(paddingValues),
        contentPadding = PaddingValues(top = 4.dp),
        content = content
    )
}

@Composable
private fun TabButtonWithDescription(
    modifier: Modifier = Modifier,
    description: String,
    icon: Painter,
    options: List<String>,
    selectedIcons: List<Painter>,
    unselectedIcons: List<Painter>,
    selectedIndex: Int,
    onTabSelected: (Int) -> Unit,
) {
    Column(
        modifier = modifier.padding(horizontal = 16.dp),
    ) {
        Row(
            modifier = Modifier.height(48.dp),
            horizontalArrangement = Arrangement.spacedBy(
                12.dp,
                Alignment.CenterHorizontally
            ),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(painter = icon, null)
            Text(text = description, style = MaterialTheme.typography.bodyLarge)
        }
        TabButtons(
            options = options,
            selectedIcons = selectedIcons,
            unselectedIcons = unselectedIcons,
            selectedIndex = selectedIndex,
            onTabSelected = onTabSelected
        )
    }
}

@Composable
private fun SettingItem(
    modifier: Modifier = Modifier,
    description: String,
    icon: Painter,
    onClick: (() -> Unit)? = null,
    trailingContent: @Composable () -> Unit = {},
) {
    Row(
        modifier = modifier
            .height(48.dp)
            .then(if (onClick != null) Modifier.clickable { onClick() } else Modifier)
            .padding(start = 16.dp, end = 4.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(12.dp, Alignment.CenterHorizontally)
    ) {
        Icon(painter = icon, contentDescription = null)
        Text(description, style = MaterialTheme.typography.bodyLarge)
        Spacer(modifier = Modifier.weight(1f))
        trailingContent()
    }
}