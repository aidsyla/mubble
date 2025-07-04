package com.aidsyla.mubble.feature.home

import androidx.compose.runtime.CompositionLocalProvider
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import com.aidsyla.mubble.common.navigation.HomeRoute
import com.aidsyla.mubble.common.navigation.LocalNavAnimatedVisibilityScope
import com.aidsyla.mubble.common.navigation.PostDetailsRoute
import com.aidsyla.mubble.common.navigation.shared_elements.PostOrigin
import com.aidsyla.mubble.feature.postdetails.PostDetailsScreen

fun NavController.navigateToHome(navOptions: NavOptions) {
    this.navigate(HomeRoute, navOptions)
}

fun NavController.navigateToPostDetails(
    postId: String,
    origin: PostOrigin,
    navOptions: NavOptions? = null,
) {
    this.navigate(PostDetailsRoute(postId = postId, origin = origin), navOptions)
}

fun NavGraphBuilder.postDetailsScreen(
    onUserClick: (String) -> Unit,
    onBackClick: () -> Unit,
) {
    composable<PostDetailsRoute> {
        CompositionLocalProvider(
            LocalNavAnimatedVisibilityScope provides this
        ) {
            PostDetailsScreen(
                origin = it.toRoute<PostDetailsRoute>().origin,
                onUserClick = onUserClick,
                onBackClick = onBackClick
            )
        }
    }
}

fun NavGraphBuilder.homeScreen(
    onUserClick: (String) -> Unit,
    onPostClick: (postId: String, origin: PostOrigin) -> Unit,
) {
    composable<HomeRoute> {
        CompositionLocalProvider(
            LocalNavAnimatedVisibilityScope provides this
        ) {
            HomeScreen(
                onUserClick = onUserClick,
                onPostClick = onPostClick
            )
        }
    }
}