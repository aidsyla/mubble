package com.aidsyla.mubble.feature.videos

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.*
import androidx.navigation.compose.*
import com.aidsyla.mubble.common.navigation.VideosRoute

fun NavController.navigateToVideos(navOptions: NavOptions) = navigate(route = VideosRoute, navOptions)

fun NavGraphBuilder.videosScreen() {
    composable<VideosRoute> {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Text("Videos Screen")
        }
    }
}