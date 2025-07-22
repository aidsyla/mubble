package com.aidsyla.mubble.feature.home

import androidx.compose.runtime.CompositionLocalProvider
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import com.aidsyla.mubble.common.navigation.HomeRoute
import com.aidsyla.mubble.common.navigation.LocalNavAnimatedVisibilityScope
import com.aidsyla.mubble.common.navigation.shared_elements.PostOrigin

fun NavController.navigateToHome(navOptions: NavOptions) {
    this.navigate(HomeRoute, navOptions)
}

fun NavGraphBuilder.homeScreen(
    onCircleClick: (String, PostOrigin) -> Unit,
    onUserClick: (String) -> Unit,
    onPostClick: (postId: String, origin: PostOrigin) -> Unit,
) {
    composable<HomeRoute> {
        CompositionLocalProvider(
            LocalNavAnimatedVisibilityScope provides this
        ) {
            HomeScreen(
                onCircleClick = onCircleClick,
                onUserClick = onUserClick,
                onPostClick = onPostClick
            )
        }
    }
}