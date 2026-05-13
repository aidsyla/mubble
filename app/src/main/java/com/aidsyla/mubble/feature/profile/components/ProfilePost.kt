package com.aidsyla.mubble.feature.profile.components

import androidx.compose.animation.EnterExitState
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.animation.core.animateDp
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.aidsyla.mubble.common.navigation.LocalNavAnimatedVisibilityScope
import com.aidsyla.mubble.common.navigation.LocalSharedTransitionScope
import com.aidsyla.mubble.common.navigation.sharedelements.PostOrigin
import com.aidsyla.mubble.common.navigation.sharedelements.PostSharedElementKey
import com.aidsyla.mubble.common.navigation.sharedelements.PostSharedElementType
import com.aidsyla.mubble.data.ImagePostFeedItem

@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
fun ProfilePost(
    modifier: Modifier = Modifier,
    item: ImagePostFeedItem,
    onPostClick: (postId: String) -> Unit
) {
    val sharedTransitionScope =
        LocalSharedTransitionScope.current
            ?: throw IllegalStateException("No SharedElementScope found")
    val animatedContentScope =
        LocalNavAnimatedVisibilityScope.current
            ?: throw IllegalStateException("No AnimatedVisibility found")

    val roundedCornerAnimation by animatedContentScope.transition.animateDp {
        when (it) {
            EnterExitState.PreEnter -> 0.dp
            EnterExitState.Visible -> 12.dp
            EnterExitState.PostExit -> 12.dp
        }
    }

    with(sharedTransitionScope) {
        Box(
            modifier =
            Modifier.sharedBounds(
                rememberSharedContentState(
                    key =
                    PostSharedElementKey(
                        postId = item.id,
                        origin = PostOrigin.ProfileMedia,
                        type = PostSharedElementType.Bounds
                    )
                ),
                resizeMode = SharedTransitionScope.ResizeMode.scaleToBounds(contentScale = ContentScale.Crop),
                clipInOverlayDuringTransition =
                OverlayClip(
                    RoundedCornerShape(
                        roundedCornerAnimation
                    )
                ),
                enter = EnterTransition.None,
                exit = ExitTransition.None,
                animatedVisibilityScope = animatedContentScope
            )
        ) {
            Image(
                painter = painterResource(item.postImageResId),
                contentDescription = null,
                modifier =
                modifier
                    .fillMaxWidth()
                    .clickable { onPostClick(item.id) }
                    .aspectRatio(1f)
                    .sharedElement(
                        rememberSharedContentState(
                            key =
                            PostSharedElementKey(
                                postId = item.id,
                                origin = PostOrigin.ProfileMedia,
                                type = PostSharedElementType.Image
                            )
                        ),
                        animatedVisibilityScope = animatedContentScope
                    ).clip(shape = MaterialTheme.shapes.medium),
                contentScale = ContentScale.Crop
            )
        }
    }
}
