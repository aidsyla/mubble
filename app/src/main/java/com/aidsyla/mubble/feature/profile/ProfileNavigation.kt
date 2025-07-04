package com.aidsyla.mubble.feature.profile

import androidx.compose.runtime.CompositionLocalProvider
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import com.aidsyla.mubble.common.navigation.LocalNavAnimatedVisibilityScope
import com.aidsyla.mubble.common.navigation.ProfileRoute
import com.aidsyla.mubble.common.navigation.shared_elements.PostOrigin

fun NavController.navigateToProfile(
    userId: String? = null,
    navOptions: NavOptions? = null,
) {
    this.navigate(ProfileRoute(userId = userId), navOptions)
}

fun NavGraphBuilder.profileScreen(
    onNavigateToSettings: () -> Unit,
    onBackClick: () -> Unit,
    onPostClick: (postId: String, origin: PostOrigin) -> Unit,
) {
    composable<ProfileRoute> {
        CompositionLocalProvider(LocalNavAnimatedVisibilityScope provides this) {
            ProfileScreen(
                onNavigateToSettings = onNavigateToSettings,
                onBackClick = onBackClick,
                onPostClick = onPostClick
            )
        }
    }
}