package com.aidsyla.mubble.common.navigation

import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionLayout
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.ui.Modifier
import androidx.lifecycle.Lifecycle
import androidx.navigation.NavBackStackEntry
import androidx.navigation.compose.NavHost
import com.aidsyla.mubble.feature.chats.chatDetailsScreen
import com.aidsyla.mubble.feature.chats.chatListScreen
import com.aidsyla.mubble.feature.chats.chatScreen
import com.aidsyla.mubble.feature.chats.navigateToChat
import com.aidsyla.mubble.feature.chats.navigateToChatDetails
import com.aidsyla.mubble.feature.circle.circleScreen
import com.aidsyla.mubble.feature.circle.navigateToCircle
import com.aidsyla.mubble.feature.explore.exploreScreen
import com.aidsyla.mubble.feature.home.homeScreen
import com.aidsyla.mubble.feature.postdetails.navigateToPostDetails
import com.aidsyla.mubble.feature.postdetails.postDetailsScreen
import com.aidsyla.mubble.feature.profile.followScreen
import com.aidsyla.mubble.feature.profile.fullScreenMediaViewer
import com.aidsyla.mubble.feature.profile.navigateToFollowScreen
import com.aidsyla.mubble.feature.profile.navigateToFullScreenMediaViewer
import com.aidsyla.mubble.feature.profile.navigateToOtherFollowScreen
import com.aidsyla.mubble.feature.profile.navigateToOtherProfile
import com.aidsyla.mubble.feature.profile.otherFollowScreen
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
import com.aidsyla.mubble.model.FollowType
import com.aidsyla.mubble.ui.AppState

@OptIn(ExperimentalSharedTransitionApi::class)
val LocalSharedTransitionScope = compositionLocalOf<SharedTransitionScope?> { null }
val LocalNavAnimatedVisibilityScope = compositionLocalOf<AnimatedVisibilityScope?> { null }

/**
 * If the lifecycle is not resumed it means this NavBackStackEntry already processed a nav event.
 *
 * This is used to de-duplicate navigation events.
 */
fun NavBackStackEntry.lifecycleIsResumed() = this.lifecycle.currentState == Lifecycle.State.RESUMED

@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
fun AppNavHost(
    appState: AppState,
    modifier: Modifier = Modifier,
    startDestination: Any = HomeRoute
) {
    val navController = appState.navController
    SharedTransitionLayout(
        modifier = modifier
    ) {
        CompositionLocalProvider(LocalSharedTransitionScope provides this) {
            NavHost(
                navController = navController,
                startDestination = startDestination,
                modifier = Modifier,
                enterTransition = {
                    fadeIn(
                        animationSpec =
                        tween(
                            durationMillis = 300,
                            easing = FastOutSlowInEasing
                        )
                    )
                },
                exitTransition = {
                    fadeOut(
                        animationSpec =
                        tween(
                            durationMillis = 300,
                            easing = FastOutSlowInEasing
                        )
                    )
                },
                popEnterTransition = {
                    fadeIn(
                        animationSpec =
                        tween(
                            durationMillis = 300,
                            easing = FastOutSlowInEasing
                        )
                    )
                },
                popExitTransition = {
                    fadeOut(
                        animationSpec =
                        tween(
                            durationMillis = 300,
                            easing = FastOutSlowInEasing
                        )
                    )
                }
            ) {
                homeScreen(
                    onCircleClick = { circleId, origin ->
                        navController.navigateToCircle(
                            circleId = circleId,
                            origin = origin
                        )
                    },
                    onUserClick = { userId ->
                        navController.navigateToOtherProfile(userId)
                    },
                    onPostClick = { postId, origin ->
                        navController.navigateToPostDetails(
                            postId,
                            origin
                        )
                    }
                )
                exploreScreen(
                    onPostClick = { postId, origin ->
                        navController.navigateToPostDetails(
                            postId,
                            origin
                        )
                    },
                    onCircleClick = { circleId, origin ->
                        navController.navigateToCircle(
                            circleId = circleId,
                            origin = origin
                        )
                    }
                )
                videosScreen(
                    onBackClick = navController::popBackStack
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

                profileScreen(
                    onNavigateToSettings = navController::navigateToSettings,
                    onPostClick = { postId, origin ->
                        navController.navigateToPostDetails(
                            postId,
                            origin
                        )
                    },
                    onMediaClick = { imageId, type ->
                        navController.navigateToFullScreenMediaViewer(
                            imageId = imageId,
                            type = type
                        )
                    },
                    onFollowersClick = { navController.navigateToFollowScreen(type = FollowType.FOLLOWERS) },
                    onFollowingClick = { navController.navigateToFollowScreen(type = FollowType.FOLLOWING) }
                )

                otherUserProfileScreen(
                    onBackClick = navController::popBackStack,
                    onPostClick = { postId, origin ->
                        navController.navigateToPostDetails(
                            postId,
                            origin
                        )
                    },
                    onMediaClick = { imageId, type ->
                        navController.navigateToFullScreenMediaViewer(
                            imageId = imageId,
                            type = type
                        )
                    },
                    onFollowersClick = { navController.navigateToOtherFollowScreen(type = FollowType.FOLLOWERS) },
                    onFollowingClick = { navController.navigateToOtherFollowScreen(type = FollowType.FOLLOWING) }
                )

                fullScreenMediaViewer(
                    onBackClick = navController::popBackStack
                )

                postDetailsScreen(onUserClick = { userId ->
                    navController.navigateToOtherProfile(userId)
                }, onBackClick = { navController.popBackStack() })

                followScreen(
                    onUserClick = { userId ->
                        navController.navigateToOtherProfile(userId)
                    },
                    onBackClick = navController::popBackStack
                )

                otherFollowScreen(
                    onUserClick = { userId ->
                        navController.navigateToOtherProfile(userId)
                    },
                    onBackClick = navController::popBackStack
                )

                circleScreen(
                    onUserClick = { userId ->
                        navController.navigateToOtherProfile(userId)
                    },
                    onPostClick = { postId, origin ->
                        navController.navigateToPostDetails(
                            postId,
                            origin
                        )
                    },
                    onMediaClick = { imageId, type ->
                        navController.navigateToFullScreenMediaViewer(
                            imageId = imageId,
                            type = type
                        )
                    },
                    onBackClick = navController::popBackStack
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
    }
}
