package com.aidsyla.mubble.feature.profile

import androidx.annotation.DrawableRes
import androidx.compose.material3.LocalRippleConfiguration
import androidx.compose.runtime.CompositionLocalProvider
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import com.aidsyla.mubble.common.navigation.FollowScreenRoute
import com.aidsyla.mubble.common.navigation.FullScreenMediaViewer
import com.aidsyla.mubble.common.navigation.LocalNavAnimatedVisibilityScope
import com.aidsyla.mubble.common.navigation.NewPostScreenRoute
import com.aidsyla.mubble.common.navigation.OtherFollowScreenRoute
import com.aidsyla.mubble.common.navigation.OtherProfileRoute
import com.aidsyla.mubble.common.navigation.ProfileRoute
import com.aidsyla.mubble.common.navigation.lifecycleIsResumed
import com.aidsyla.mubble.common.navigation.sharedelements.PostOrigin
import com.aidsyla.mubble.feature.post.NewPostScreen
import com.aidsyla.mubble.feature.profile.components.FullScreenMediaType
import com.aidsyla.mubble.feature.profile.components.FullScreenMediaViewer
import com.aidsyla.mubble.feature.profile.currentuser.ProfileScreen
import com.aidsyla.mubble.feature.profile.follow.FollowScreen
import com.aidsyla.mubble.feature.profile.otheruser.OtherUserProfileScreen
import com.aidsyla.mubble.model.FollowType

fun NavController.navigateToFullScreenMediaViewer(
    navOptions: NavOptions? = null,
    @DrawableRes imageId: Int,
    type: FullScreenMediaType
) {
    this.navigate(FullScreenMediaViewer(imageId = imageId, type = type), navOptions)
}

fun NavGraphBuilder.fullScreenMediaViewer(onBackClick: () -> Unit) {
    composable<FullScreenMediaViewer> { it ->
        val args: FullScreenMediaViewer = it.toRoute()
        val imageId = args.imageId
        val type = args.type

        CompositionLocalProvider(
            LocalNavAnimatedVisibilityScope provides this,
            LocalRippleConfiguration provides null
        ) {
            FullScreenMediaViewer(
                type = type,
                imageId = imageId,
                onBackClick = { if (it.lifecycleIsResumed()) onBackClick() }
            )
        }
    }
}

fun NavController.navigateToProfile(navOptions: NavOptions? = null) {
    this.navigate(ProfileRoute, navOptions)
}

fun NavGraphBuilder.profileScreen(
    onNavigateToSettings: () -> Unit,
    onPostClick: (postId: String, origin: PostOrigin) -> Unit,
    onMediaClick: (Int, FullScreenMediaType) -> Unit,
    onFollowersClick: () -> Unit,
    onFollowingClick: () -> Unit,
    onNewPostClick: () -> Unit
) {
    composable<ProfileRoute> { backStack ->
        CompositionLocalProvider(LocalNavAnimatedVisibilityScope provides this) {
            ProfileScreen(
                onNavigateToSettings = onNavigateToSettings,
                onPostClick = onPostClick,
                onMediaClick = { p1, p2 ->
                    if (backStack.lifecycleIsResumed()) onMediaClick(p1, p2)
                },
                onFollowersClick = onFollowersClick,
                onFollowingClick = onFollowingClick,
                onNewPostClick = onNewPostClick
            )
        }
    }
}

fun NavController.navigateToOtherProfile(
    userId: String? = null,
    navOptions: NavOptions? = null
) {
    this.navigate(OtherProfileRoute(userId = userId), navOptions)
}

fun NavGraphBuilder.otherUserProfileScreen(
    onBackClick: () -> Unit,
    onPostClick: (postId: String, origin: PostOrigin) -> Unit,
    onMediaClick: (Int, FullScreenMediaType) -> Unit,
    onFollowersClick: () -> Unit,
    onFollowingClick: () -> Unit
) {
    composable<OtherProfileRoute> {
        CompositionLocalProvider(LocalNavAnimatedVisibilityScope provides this) {
            OtherUserProfileScreen(
                onBackClick = { if (it.lifecycleIsResumed()) onBackClick() },
                onPostClick = onPostClick,
                onMediaClick = onMediaClick,
                onFollowersClick = onFollowersClick,
                onFollowingClick = onFollowingClick
            )
        }
    }
}

fun NavController.navigateToFollowScreen(
    navOptions: NavOptions? = null,
    type: FollowType
) {
    this.navigate(FollowScreenRoute(type = type), navOptions)
}

fun NavGraphBuilder.followScreen(
    onUserClick: (String) -> Unit,
    onBackClick: () -> Unit
) {
    composable<FollowScreenRoute> {
        val args: FollowScreenRoute = it.toRoute()
        val type = args.type
        CompositionLocalProvider(LocalNavAnimatedVisibilityScope provides this) {
            FollowScreen(
                isCurrentUser = true,
                type = type,
                onUserClick = onUserClick,
                onMessageClick = {},
                onBackClick = onBackClick
            )
        }
    }
}

fun NavController.navigateToOtherFollowScreen(
    navOptions: NavOptions? = null,
    type: FollowType
) {
    this.navigate(OtherFollowScreenRoute(type = type), navOptions)
}

fun NavGraphBuilder.otherFollowScreen(
    onUserClick: (String) -> Unit,
    onBackClick: () -> Unit
) {
    composable<OtherFollowScreenRoute> {
        val args: OtherFollowScreenRoute = it.toRoute()
        val type = args.type
        CompositionLocalProvider(LocalNavAnimatedVisibilityScope provides this) {
            FollowScreen(
                isCurrentUser = false,
                type = type,
                onUserClick = onUserClick,
                onMessageClick = {},
                onBackClick = onBackClick
            )
        }
    }
}

fun NavController.navigateToNewPostScreen(
    navOptions: NavOptions? = null
) {
    this.navigate(NewPostScreenRoute, navOptions)
}

fun NavGraphBuilder.newPostScreen(
    onBackClick: () -> Unit
) {
    composable<NewPostScreenRoute> {
        CompositionLocalProvider(LocalNavAnimatedVisibilityScope provides this) {
            NewPostScreen(
                onBackClick = onBackClick
            )
        }
    }
}
