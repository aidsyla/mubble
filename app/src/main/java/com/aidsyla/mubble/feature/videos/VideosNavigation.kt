package com.aidsyla.mubble.feature.videos

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import com.aidsyla.mubble.common.navigation.VideosRoute
import com.aidsyla.mubble.common.navigation.lifecycleIsResumed

fun NavController.navigateToVideos(navOptions: NavOptions) = navigate(route = VideosRoute, navOptions)

fun NavGraphBuilder.videosScreen(onBackClick: () -> Unit) {
    composable<VideosRoute> {
        VideosScreen(
            onBackClick = { if (it.lifecycleIsResumed()) onBackClick() }
        )
    }
}
