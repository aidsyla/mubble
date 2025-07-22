package com.aidsyla.mubble.feature.explore

import androidx.compose.runtime.CompositionLocalProvider
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import com.aidsyla.mubble.common.navigation.ExploreRoute
import com.aidsyla.mubble.common.navigation.LocalNavAnimatedVisibilityScope
import com.aidsyla.mubble.common.navigation.shared_elements.PostOrigin

fun NavController.navigateToExplore(navOptions: NavOptions) = navigate(route = ExploreRoute, navOptions)

fun NavGraphBuilder.exploreScreen(
    onPostClick: (postId: String, origin: PostOrigin) -> Unit,
    onCircleClick: (String, PostOrigin) -> Unit,
) {
    composable<ExploreRoute> {
        CompositionLocalProvider(LocalNavAnimatedVisibilityScope provides this) {
            ExploreScreen(
                onPostClick = onPostClick,
                onCircleClick = onCircleClick
            )
        }
    }
}