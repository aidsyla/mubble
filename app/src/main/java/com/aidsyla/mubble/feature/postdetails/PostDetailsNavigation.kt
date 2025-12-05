package com.aidsyla.mubble.feature.postdetails

import androidx.compose.runtime.CompositionLocalProvider
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import com.aidsyla.mubble.common.navigation.LocalNavAnimatedVisibilityScope
import com.aidsyla.mubble.common.navigation.PostDetailsRoute
import com.aidsyla.mubble.common.navigation.lifecycleIsResumed
import com.aidsyla.mubble.common.navigation.shared_elements.PostOrigin

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
            LocalNavAnimatedVisibilityScope provides this,
        ) {
            PostDetailsScreen(
                origin = it.toRoute<PostDetailsRoute>().origin,
                onUserClick = onUserClick,
                onBackClick = { if (it.lifecycleIsResumed()) onBackClick() },
            )
        }
    }
}
