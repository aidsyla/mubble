package com.aidsyla.mubble.feature.profile

import androidx.compose.runtime.CompositionLocalProvider
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import com.aidsyla.mubble.common.navigation.LocalNavAnimatedVisibilityScope
import com.aidsyla.mubble.common.navigation.OtherProfileRoute
import com.aidsyla.mubble.common.navigation.ProfileRoute
import com.aidsyla.mubble.common.navigation.shared_elements.PostOrigin
import com.aidsyla.mubble.feature.profile.current_user.ProfileScreen
import com.aidsyla.mubble.feature.profile.other_user.OtherUserProfileScreen

fun NavController.navigateToProfile(
    navOptions: NavOptions? = null,
) {
    this.navigate(ProfileRoute, navOptions)
}

fun NavGraphBuilder.profileScreen(
    onNavigateToSettings: () -> Unit,
    onPostClick: (postId: String, origin: PostOrigin) -> Unit,
) {
    composable<ProfileRoute> {
        CompositionLocalProvider(LocalNavAnimatedVisibilityScope provides this) {
            ProfileScreen(
                onNavigateToSettings = onNavigateToSettings,
                onPostClick = onPostClick
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
    onMoreClick: () -> Unit,
    onEditClick: () -> Unit,
    onBackClick: () -> Unit,
    onPostClick: (postId: String, origin: PostOrigin) -> Unit,
) {
    composable<OtherProfileRoute> {
        CompositionLocalProvider(LocalNavAnimatedVisibilityScope provides this) {
            OtherUserProfileScreen(
                onMoreClick = onMoreClick,
                onEditClick = onEditClick,
                onBackClick = onBackClick,
                onPostClick = onPostClick
            )
        }
    }
}
