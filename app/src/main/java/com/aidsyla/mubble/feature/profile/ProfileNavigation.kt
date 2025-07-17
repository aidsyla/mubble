package com.aidsyla.mubble.feature.profile

import androidx.annotation.DrawableRes
import androidx.compose.material3.LocalRippleConfiguration
import androidx.compose.runtime.CompositionLocalProvider
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import com.aidsyla.mubble.common.navigation.FullScreenMediaViewer
import com.aidsyla.mubble.common.navigation.LocalNavAnimatedVisibilityScope
import com.aidsyla.mubble.common.navigation.OtherProfileRoute
import com.aidsyla.mubble.common.navigation.ProfileRoute
import com.aidsyla.mubble.common.navigation.lifecycleIsResumed
import com.aidsyla.mubble.common.navigation.shared_elements.PostOrigin
import com.aidsyla.mubble.feature.profile.components.FullScreenMediaType
import com.aidsyla.mubble.feature.profile.components.FullScreenMediaViewer
import com.aidsyla.mubble.feature.profile.current_user.ProfileScreen
import com.aidsyla.mubble.feature.profile.other_user.OtherUserProfileScreen

fun NavController.navigateToFullScreenMediaViewer(
    navOptions: NavOptions? = null,
    @DrawableRes imageId: Int,
    type: FullScreenMediaType,
) {
    this.navigate(FullScreenMediaViewer(imageId = imageId, type = type), navOptions)
}

fun NavGraphBuilder.fullScreenMediaViewer(
    onBackClick: () -> Unit,
) {
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

fun NavController.navigateToProfile(
    navOptions: NavOptions? = null,
) {
    this.navigate(ProfileRoute, navOptions)
}

fun NavGraphBuilder.profileScreen(
    onNavigateToSettings: () -> Unit,
    onPostClick: (postId: String, origin: PostOrigin) -> Unit,
    onMediaClick: (Int, FullScreenMediaType) -> Unit,
) {
    composable<ProfileRoute> {backStack ->
        CompositionLocalProvider(LocalNavAnimatedVisibilityScope provides this) {
            ProfileScreen(
                onNavigateToSettings = onNavigateToSettings,
                onPostClick = onPostClick,
                onMediaClick = { p1, p2 -> if (backStack.lifecycleIsResumed()) onMediaClick(p1, p2) }
            )
        }
    }
}

fun NavController.navigateToOtherProfile(
    userId: String? = null,
    navOptions: NavOptions? = null,
) {
    this.navigate(OtherProfileRoute(userId = userId), navOptions)
}

fun NavGraphBuilder.otherUserProfileScreen(
    onBackClick: () -> Unit,
    onPostClick: (postId: String, origin: PostOrigin) -> Unit,
    onMediaClick: (Int, FullScreenMediaType) -> Unit
) {
    composable<OtherProfileRoute> {
        CompositionLocalProvider(LocalNavAnimatedVisibilityScope provides this) {
            OtherUserProfileScreen(
                onBackClick = { if (it.lifecycleIsResumed()) onBackClick() },
                onPostClick = onPostClick,
                onMediaClick = onMediaClick
            )
        }
    }
}
