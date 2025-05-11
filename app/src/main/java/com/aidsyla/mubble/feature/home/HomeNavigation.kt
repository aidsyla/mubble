package com.aidsyla.mubble.feature.home

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import com.aidsyla.mubble.common.navigation.HomeRoute
import com.aidsyla.mubble.common.navigation.PostDetailsRoute
import com.aidsyla.mubble.feature.postdetails.PostDetailsScreen

fun NavController.navigateToHome(navOptions: NavOptions) {
    this.navigate(HomeRoute, navOptions)
}

fun NavController.navigateToPostDetails(
    postId: String,
    navOptions: NavOptions? = null,
) {
    this.navigate(PostDetailsRoute(postId = postId), navOptions)
}

fun NavGraphBuilder.postDetailsScreen(
    onUserClick: (String) -> Unit,
    onBackClick: () -> Unit,
) {
    composable<PostDetailsRoute> {
        PostDetailsScreen(
            onUserClick = onUserClick,
            onBackClick = onBackClick
        )
    }
}

fun NavGraphBuilder.homeScreen(
    onUserClick: (String) -> Unit,
    onPostClick: (postId: String) -> Unit,
) {
    composable<HomeRoute> {
        HomeScreen(
            onUserClick = onUserClick,
            onPostClick = onPostClick
        )
    }
}