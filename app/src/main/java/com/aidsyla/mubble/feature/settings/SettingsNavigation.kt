package com.aidsyla.mubble.feature.settings

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import com.aidsyla.mubble.common.navigation.SettingsDevicePermissionsRoute
import com.aidsyla.mubble.common.navigation.SettingsManageAccountRoute
import com.aidsyla.mubble.common.navigation.SettingsNotificationsRoute
import com.aidsyla.mubble.common.navigation.SettingsStartRoute
import com.aidsyla.mubble.common.navigation.lifecycleIsResumed

fun NavController.navigateToSettings(navOptions: NavOptions? = null) {
    this.navigate(SettingsStartRoute, navOptions)
}

fun NavGraphBuilder.settingsStartScreen(
    onNavigateToNotifications: () -> Unit,
    onNavigateToDevicePermissions: () -> Unit,
    onNavigateToManageAccount: () -> Unit,
    onLogoutClick: () -> Unit,
    onBackClick: () -> Unit
) {
    composable<SettingsStartRoute> {
        SettingsStartScreen(
            onNavigateToNotifications = onNavigateToNotifications,
            onNavigateToDevicePermissions = onNavigateToDevicePermissions,
            onNavigateToManageAccount = onNavigateToManageAccount,
            onLogoutClick = onLogoutClick,
            onBackClick = { if (it.lifecycleIsResumed()) onBackClick() },
        )
    }
}

fun NavController.navigateToSettingsNotifications(navOptions: NavOptions? = null) {
    this.navigate(SettingsNotificationsRoute, navOptions)
}

fun NavGraphBuilder.settingsNotificationsScreen(
    onBackClick: () -> Unit
) {
    composable<SettingsNotificationsRoute> {
        SettingsNotificationsScreen(
            onBackClick = { if (it.lifecycleIsResumed()) onBackClick() },
        )
    }
}

fun NavController.navigateToSettingsDevicePermissions(navOptions: NavOptions? = null) {
    this.navigate(SettingsDevicePermissionsRoute, navOptions)
}

fun NavGraphBuilder.settingsDevicePermissionsScreen(
    onBackClick: () -> Unit
) {
    composable<SettingsDevicePermissionsRoute> {
        SettingsDevicePermissionsScreen(
            onBackClick = { if (it.lifecycleIsResumed()) onBackClick() },
        )
    }
}

fun NavController.navigateToSettingsManageAccount(navOptions: NavOptions? = null) {
    this.navigate(SettingsManageAccountRoute, navOptions)
}

fun NavGraphBuilder.settingsManageAccountScreen(
    onBackClick: () -> Unit
) {
    composable<SettingsManageAccountRoute> {
        SettingsManageAccountScreen(
            onBackClick = { if (it.lifecycleIsResumed()) onBackClick() },
        )
    }
}