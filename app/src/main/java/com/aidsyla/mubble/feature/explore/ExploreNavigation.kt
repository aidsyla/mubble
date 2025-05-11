package com.aidsyla.mubble.feature.explore

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import com.aidsyla.mubble.common.navigation.ExploreRoute

fun NavController.navigateToExplore(navOptions: NavOptions) = navigate(route = ExploreRoute, navOptions)

fun NavGraphBuilder.exploreScreen(
    onPostClick: (postId: String) -> Unit,
) {
    composable<ExploreRoute> {
        ExploreScreen(
            onPostClick = onPostClick
        )
    }
}