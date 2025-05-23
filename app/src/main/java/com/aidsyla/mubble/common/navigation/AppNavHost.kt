package com.aidsyla.mubble.common.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import com.aidsyla.mubble.feature.activity.activityScreen
import com.aidsyla.mubble.feature.chats.chatDetailsScreen
import com.aidsyla.mubble.feature.chats.chatListScreen
import com.aidsyla.mubble.feature.chats.chatScreen
import com.aidsyla.mubble.feature.chats.navigateToChat
import com.aidsyla.mubble.feature.chats.navigateToChatDetails
import com.aidsyla.mubble.feature.explore.exploreScreen
import com.aidsyla.mubble.feature.home.homeScreen
import com.aidsyla.mubble.feature.home.navigateToPostDetails
import com.aidsyla.mubble.feature.home.postDetailsScreen
import com.aidsyla.mubble.feature.profile.navigateToProfile
import com.aidsyla.mubble.feature.profile.profileScreen
import com.aidsyla.mubble.feature.settings.navigateToSettings
import com.aidsyla.mubble.feature.settings.navigateToSettingsDevicePermissions
import com.aidsyla.mubble.feature.settings.navigateToSettingsManageAccount
import com.aidsyla.mubble.feature.settings.navigateToSettingsNotifications
import com.aidsyla.mubble.feature.settings.settingsDevicePermissionsScreen
import com.aidsyla.mubble.feature.settings.settingsManageAccountScreen
import com.aidsyla.mubble.feature.settings.settingsNotificationsScreen
import com.aidsyla.mubble.feature.settings.settingsStartScreen
import com.aidsyla.mubble.ui.AppState

@Composable
fun AppNavHost(
    appState: AppState,
    modifier: Modifier = Modifier,
    startDestination: Any = HomeRoute,
) {
    val navController = appState.navController
    NavHost(
        navController = navController,
        startDestination = startDestination,
        modifier = modifier,
    ) {
        homeScreen(
            onUserClick = { userId ->
                navController.navigateToProfile(userId)
            },
            onPostClick = { postId -> navController.navigateToPostDetails(postId) }
        )
        exploreScreen(
            onPostClick = { postId -> navController.navigateToPostDetails(postId) }
        )
        chatListScreen(
            onChatClick = { chatId, otherUserId ->
                navController.navigateToChat(chatId = chatId, otherUserId = otherUserId)
            }
        )
        chatScreen(
            onBackClick = navController::popBackStack,
            onProfileClick = {},
            onMoreClick = { navController.navigateToChatDetails(it) }
        )
        chatDetailsScreen(
            onBackClick = navController::popBackStack,
            onProfileClick = {}
        )
        activityScreen()
        profileScreen(
            onNavigateToSettings = navController::navigateToSettings,
            onBackClick = navController::popBackStack
        )

        postDetailsScreen(
            onUserClick = { userId ->
                navController.navigateToProfile(userId)
            },
            onBackClick = { navController.popBackStack() }
        )

        settingsStartScreen(
            onNavigateToNotifications = navController::navigateToSettingsNotifications,
            onNavigateToDevicePermissions = navController::navigateToSettingsDevicePermissions,
            onNavigateToManageAccount = navController::navigateToSettingsManageAccount,
            onLogoutClick = {},
            onBackClick = navController::popBackStack
        )
        settingsNotificationsScreen(
            onBackClick = navController::popBackStack
        )
        settingsDevicePermissionsScreen(
            onBackClick = navController::popBackStack
        )
        settingsManageAccountScreen(
            onBackClick = navController::popBackStack
        )
    }
}