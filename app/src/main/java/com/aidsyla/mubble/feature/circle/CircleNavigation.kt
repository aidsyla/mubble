package com.aidsyla.mubble.feature.circle

import androidx.compose.runtime.CompositionLocalProvider
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import com.aidsyla.mubble.common.navigation.CircleRoute
import com.aidsyla.mubble.common.navigation.LocalNavAnimatedVisibilityScope
import com.aidsyla.mubble.common.navigation.shared_elements.PostOrigin
import com.aidsyla.mubble.feature.profile.components.FullScreenMediaType

fun NavController.navigateToCircle(
    navOptions: NavOptions? = null,
    circleId: String,
    origin: PostOrigin,
) {
    this.navigate(CircleRoute(circleId = circleId, origin = origin), navOptions)
}

fun NavGraphBuilder.circleScreen(
    onUserClick: (String) -> Unit,
    onPostClick: (postId: String, origin: PostOrigin) -> Unit,
    onMediaClick: (Int, FullScreenMediaType) -> Unit,
    onBackClick: () -> Unit,
) {
    composable<CircleRoute> {
        CompositionLocalProvider(
            LocalNavAnimatedVisibilityScope provides this,
        ) {
            CircleScreen(
                origin = it.toRoute<CircleRoute>().origin,
                onUserClick = onUserClick,
                onPostClick = onPostClick,
                onMediaClick = onMediaClick,
                onBackClick = onBackClick,
            )
        }
    }
}
