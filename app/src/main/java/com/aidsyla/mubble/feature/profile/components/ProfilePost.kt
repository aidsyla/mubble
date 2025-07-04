package com.aidsyla.mubble.feature.profile.components

import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import com.aidsyla.mubble.common.navigation.LocalNavAnimatedVisibilityScope
import com.aidsyla.mubble.common.navigation.LocalSharedTransitionScope
import com.aidsyla.mubble.common.navigation.shared_elements.PostOrigin
import com.aidsyla.mubble.common.navigation.shared_elements.PostSharedElementKey
import com.aidsyla.mubble.common.navigation.shared_elements.PostSharedElementType
import com.aidsyla.mubble.feature.explore.model.ImagePostFeedItem

@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
fun ProfilePost(
    modifier: Modifier = Modifier,
    item: ImagePostFeedItem,
    onPostClick: (postId: String) -> Unit,
) {
    val sharedTransitionScope = LocalSharedTransitionScope.current
        ?: throw IllegalStateException("No SharedElementScope found")
    val animatedContentScope = LocalNavAnimatedVisibilityScope.current
        ?: throw IllegalStateException("No AnimatedVisibility found")

    with(sharedTransitionScope) {
        Box(
            modifier = Modifier.sharedBounds(
                rememberSharedContentState(
                    key = PostSharedElementKey(
                        postId = item.id,
                        origin = PostOrigin.ProfileMedia,
                        type = PostSharedElementType.Bounds
                    )
                ),
                resizeMode = SharedTransitionScope.ResizeMode.RemeasureToBounds,
                animatedVisibilityScope = animatedContentScope
            )
        ) {
            Image(
                painter = painterResource(item.postImageResId), contentDescription = null,
                modifier = modifier
                    .fillMaxWidth()
                    .clickable { onPostClick(item.id) }
                    .aspectRatio(1f)
                    .sharedElement(
                        rememberSharedContentState(
                            key = PostSharedElementKey(
                                postId = item.id,
                                origin = PostOrigin.ProfileMedia,
                                type = PostSharedElementType.Image
                            )
                        ),
                        animatedVisibilityScope = animatedContentScope
                    )
                    .clip(shape = MaterialTheme.shapes.medium),
                contentScale = ContentScale.Crop
            )
        }
    }
}