package com.aidsyla.mubble.common.navigation

import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionLayout
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import com.aidsyla.mubble.feature.chats.chatDetailsScreen
import com.aidsyla.mubble.feature.chats.chatListScreen
import com.aidsyla.mubble.feature.chats.chatScreen
import com.aidsyla.mubble.feature.chats.navigateToChat
import com.aidsyla.mubble.feature.chats.navigateToChatDetails
import com.aidsyla.mubble.feature.explore.exploreScreen
import com.aidsyla.mubble.feature.home.homeScreen
import com.aidsyla.mubble.feature.postdetails.navigateToPostDetails
import com.aidsyla.mubble.feature.postdetails.postDetailsScreen
import com.aidsyla.mubble.feature.profile.navigateToOtherProfile
import com.aidsyla.mubble.feature.profile.otherUserProfileScreen
import com.aidsyla.mubble.feature.profile.profileScreen
import com.aidsyla.mubble.feature.settings.navigateToSettings
import com.aidsyla.mubble.feature.settings.navigateToSettingsDevicePermissions
import com.aidsyla.mubble.feature.settings.navigateToSettingsManageAccount
import com.aidsyla.mubble.feature.settings.navigateToSettingsNotifications
import com.aidsyla.mubble.feature.settings.settingsDevicePermissionsScreen
import com.aidsyla.mubble.feature.settings.settingsManageAccountScreen
import com.aidsyla.mubble.feature.settings.settingsNotificationsScreen
import com.aidsyla.mubble.feature.settings.settingsStartScreen
import com.aidsyla.mubble.feature.videos.videosScreen
import com.aidsyla.mubble.ui.AppState

@OptIn(ExperimentalSharedTransitionApi::class)
val LocalSharedTransitionScope = compositionLocalOf<SharedTransitionScope?> { null }
val LocalNavAnimatedVisibilityScope = compositionLocalOf<AnimatedVisibilityScope?> { null }

@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
fun AppNavHost(
    appState: AppState,
    modifier: Modifier = Modifier,
    startDestination: Any = HomeRoute,
) {
    val navController = appState.navController
    SharedTransitionLayout(
        modifier = modifier
    ) {
        CompositionLocalProvider(LocalSharedTransitionScope provides this) {
            NavHost(
                navController = navController,
                startDestination = startDestination,
                modifier = Modifier
            ) {
                homeScreen(onUserClick = { userId ->
                    navController.navigateToOtherProfile(userId)
                }, onPostClick = { postId, origin ->
                    navController.navigateToPostDetails(
                        postId, origin
                    )
                })
                exploreScreen(
                    onPostClick = { postId, origin ->
                        navController.navigateToPostDetails(
                            postId, origin
                        )
                    })
                chatListScreen(
                    onChatClick = { chatId, otherUserId ->
                        navController.navigateToChat(chatId = chatId, otherUserId = otherUserId)
                    })
                chatScreen(
                    onBackClick = navController::popBackStack,
                    onProfileClick = {},
                    onMoreClick = { navController.navigateToChatDetails(it) })
                chatDetailsScreen(
                    onBackClick = navController::popBackStack, onProfileClick = {})
                videosScreen(
                    onBackClick = navController::popBackStack,
                )
                profileScreen(
                    onNavigateToSettings = navController::navigateToSettings,
                    onPostClick = { postId, origin ->
                        navController.navigateToPostDetails(
                            postId, origin
                        )
                    })

                otherUserProfileScreen(
                    onMoreClick = {},
                    onEditClick = {},
                    onBackClick = navController::popBackStack,
                    onPostClick = { postId, origin ->
                        navController.navigateToPostDetails(
                            postId, origin
                        )
                    })

                postDetailsScreen(onUserClick = { userId ->
                    navController.navigateToOtherProfile(userId)
                }, onBackClick = { navController.popBackStack() })

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
    }
}