package com.aidsyla.mubble.feature.explore

import androidx.annotation.DrawableRes
import androidx.compose.animation.EnterExitState
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.animation.core.animateDp
import androidx.compose.animation.core.animateFloat
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.staggeredgrid.LazyStaggeredGridState
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.aidsyla.mubble.common.components.CircleImage
import com.aidsyla.mubble.common.components.circle.CircleItem
import com.aidsyla.mubble.common.components.layout.MubbleGridTabPager
import com.aidsyla.mubble.common.components.layout.MubbleTabRow
import com.aidsyla.mubble.common.navigation.LocalNavAnimatedVisibilityScope
import com.aidsyla.mubble.common.navigation.LocalSharedTransitionScope
import com.aidsyla.mubble.common.navigation.shared_elements.PostOrigin
import com.aidsyla.mubble.common.navigation.shared_elements.PostSharedElementKey
import com.aidsyla.mubble.common.navigation.shared_elements.PostSharedElementType
import com.aidsyla.mubble.feature.circle.model.Circle
import com.aidsyla.mubble.feature.circle.model.CircleRepo
import com.aidsyla.mubble.feature.explore.model.BubbleFeedItem
import com.aidsyla.mubble.feature.explore.model.ImagePostFeedItem
import com.aidsyla.mubble.feature.home.data.DummyPostRepository
import com.aidsyla.mubble.ui.LocalBottomBarPadding
import com.aidsyla.mubble.ui.theme.MubbleTheme

@Composable
fun ExploreScreen(
    viewModel: ExploreViewModel = hiltViewModel(),
    onPostClick: (postId: String, origin: PostOrigin) -> Unit,
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val tabTitles = remember { listOf("Circles", "Media", "Bubbles") }
    val pagerState = rememberPagerState(initialPage = 1) { tabTitles.size }
    val bottomPadding = LocalBottomBarPadding.current

    MubbleGridTabPager(
        modifier = Modifier.padding(bottom = bottomPadding),
        pagerState = pagerState,
        firstPage = {
            ExploreCircleGrid(
                items = CircleRepo.dummyCircles
            )
        },
        secondPage = {
            ExplorePostGrid(
                items = uiState.media, state = it, onPostClick = onPostClick
            )
        },
        thirdPage = {
            ExploreBubbleGrid(
                items = uiState.bubbles, state = it, onPostClick = onPostClick
            )
        },
        navigationIcon = {
            IconButton(onClick = {}, enabled = false) {
                Icon(
                    painter = MubbleTheme.Icons.MubbleIcon,
                    contentDescription = null,
                    tint = Color.Unspecified
                )
            }
        },
        actions = {
            IconButton(onClick = {}) {
                Icon(painter = MubbleTheme.Icons.Search, contentDescription = null)
            }
        }) {
        MubbleTabRow(
            tabTitles = tabTitles, pagerState = pagerState
        )
    }
}

@Composable
fun ExploreCircleGrid(
    modifier: Modifier = Modifier,
    items: List<Circle>,
) {
    LazyVerticalGrid(
        modifier = modifier,
        columns = GridCells.Fixed(2),
        contentPadding = PaddingValues(top = 4.dp, start = 8.dp, end = 8.dp, bottom = 8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        items(items) {
            CircleItem(circle = it, showIcon = true, modifier = Modifier.fillMaxWidth())
        }
    }
}

@Composable
fun ExploreBubbleGrid(
    modifier: Modifier = Modifier,
    items: List<BubbleFeedItem>,
    state: LazyStaggeredGridState,
    onPostClick: (postId: String, origin: PostOrigin) -> Unit,
) {
    LazyVerticalStaggeredGrid(
        state = state,
        columns = StaggeredGridCells.Fixed(2),
        contentPadding = PaddingValues(top = 4.dp, start = 8.dp, end = 8.dp, bottom = 8.dp),
        verticalItemSpacing = 8.dp,
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        modifier = modifier.fillMaxSize()
    ) {
        exploreBubblesFeed(
            items = items, onProfileClick = {}, onPostClick = onPostClick
        )
    }
}

@Composable
fun ExplorePostGrid(
    modifier: Modifier = Modifier,
    items: List<ImagePostFeedItem>,
    state: LazyStaggeredGridState,
    onPostClick: (postId: String, origin: PostOrigin) -> Unit,
) {
    LazyVerticalStaggeredGrid(
        state = state,
        columns = StaggeredGridCells.Fixed(2),
        contentPadding = PaddingValues(top = 4.dp, start = 8.dp, end = 8.dp, bottom = 8.dp),
        verticalItemSpacing = 8.dp,
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        modifier = modifier.fillMaxSize()
    ) {
        explorePostsFeed(
            items = items, onProfileClick = {}, onPostClick = onPostClick
        )
    }
}

@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
fun ExplorePost(
    modifier: Modifier = Modifier,
    item: ImagePostFeedItem,
    onPostClick: (postId: String, origin: PostOrigin) -> Unit,
) {
    val sharedTransitionScope = LocalSharedTransitionScope.current
        ?: throw IllegalStateException("No SharedElementScope found")
    val animatedContentScope = LocalNavAnimatedVisibilityScope.current
        ?: throw IllegalStateException("No AnimatedVisibility found")

    val roundedCornerAnimation by animatedContentScope.transition.animateDp {
        when (it) {
            EnterExitState.PreEnter -> 0.dp
            EnterExitState.Visible -> 12.dp
            EnterExitState.PostExit -> 12.dp
        }
    }

    val gradientAlpha2 by animatedContentScope.transition.animateFloat {
        when (it) {
            EnterExitState.PreEnter -> 0f
            EnterExitState.Visible -> 1f
            EnterExitState.PostExit -> 1f
        }
    }

    with(sharedTransitionScope) {
        Box(
            modifier = modifier
                .clickable { onPostClick(item.id, PostOrigin.ExploreMedia) }
                .sharedBounds(
                    rememberSharedContentState(
                        key = PostSharedElementKey(
                            postId = item.id,
                            origin = PostOrigin.ExploreMedia,
                            type = PostSharedElementType.Bounds
                        )
                    ),
                    animatedVisibilityScope = animatedContentScope,
                    resizeMode = SharedTransitionScope.ResizeMode.RemeasureToBounds,
                    clipInOverlayDuringTransition = OverlayClip(
                        RoundedCornerShape(
                            roundedCornerAnimation
                        )
                    )
                )
                .clip(MaterialTheme.shapes.medium)) {
            Image(
                painter = painterResource(item.postImageResId),
                contentDescription = "Post media",
                modifier = Modifier
                    .fillMaxWidth()
                    .sharedElement(
                        rememberSharedContentState(
                            key = PostSharedElementKey(
                                postId = item.id,
                                origin = PostOrigin.ExploreMedia,
                                type = PostSharedElementType.Image
                            )
                        ), animatedVisibilityScope = animatedContentScope
                    )
                    .clip(
                        shape = RoundedCornerShape(roundedCornerAnimation)
                    )
                    .drawWithContent {
                        drawContent()
                        drawRect(
                            MubbleTheme.Gradients.fadingBlackGradient,
                            alpha = gradientAlpha2
                        )
                    }
            )
            ExploreHeader(
                modifier = Modifier.align(Alignment.TopStart),
                origin = PostOrigin.ExploreMedia,
                avatarResId = item.userAvatarResId,
                id = item.id,
                username = item.username
            )
        }
    }
}

@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
private fun ExploreHeader(
    modifier: Modifier = Modifier,
    origin: PostOrigin,
    id: String,
    username: String,
    textColor: Color = Color.White,
    @DrawableRes avatarResId: Int,
) {
    val sharedTransitionScope = LocalSharedTransitionScope.current
        ?: throw IllegalStateException("No SharedElementScope found")
    val animatedContentScope = LocalNavAnimatedVisibilityScope.current
        ?: throw IllegalStateException("No AnimatedVisibility found")

    Row(
        modifier = modifier.padding(8.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        with(sharedTransitionScope) {
            CircleImage(
                modifier = Modifier.sharedElement(
                    rememberSharedContentState(
                        key = PostSharedElementKey(
                            postId = id, origin = origin, type = PostSharedElementType.ProfileAvatar
                        )
                    ), animatedVisibilityScope = animatedContentScope
                ), painter = painterResource(avatarResId), size = 24.dp
            )
            Text(
                modifier = Modifier.sharedBounds(
                    rememberSharedContentState(
                        key = PostSharedElementKey(
                            postId = id, origin = origin, type = PostSharedElementType.DisplayName
                        )
                    ), animatedVisibilityScope = animatedContentScope
                ), text = username, style = MaterialTheme.typography.labelMedium.copy(
                    fontWeight = FontWeight.Bold
                ), color = textColor
            )
        }
    }
}

@Composable
fun ExploreBubble(
    modifier: Modifier = Modifier,
    item: BubbleFeedItem,
    onPostClick: (postId: String, origin: PostOrigin) -> Unit,
) {
    ExploreBubble(
        modifier = modifier,
        postId = item.id,
        username = item.username,
        description = item.postDescription,
        avatarResId = item.userAvatarResId,
        onPostClick = onPostClick
    )
}

@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
fun ExploreBubble(
    modifier: Modifier = Modifier,
    postId: String,
    username: String,
    description: String,
    @DrawableRes avatarResId: Int,
    onPostClick: (postId: String, origin: PostOrigin) -> Unit,
) {
    val sharedTransitionScope = LocalSharedTransitionScope.current
        ?: throw IllegalStateException("No SharedElementScope found")
    val animatedContentScope = LocalNavAnimatedVisibilityScope.current
        ?: throw IllegalStateException("No AnimatedVisibility found")

    with(sharedTransitionScope) {
        Card(
            modifier = modifier
                .fillMaxWidth()
                .sharedBounds(
                    rememberSharedContentState(
                        key = PostSharedElementKey(
                            postId = postId,
                            origin = PostOrigin.ExploreBubbles,
                            type = PostSharedElementType.Bounds
                        )
                    ),
                    animatedVisibilityScope = animatedContentScope,
                ),
            onClick = { onPostClick(postId, PostOrigin.ExploreBubbles) },
            shape = MaterialTheme.shapes.medium
        ) {
            ExploreHeader(
                origin = PostOrigin.ExploreBubbles,
                avatarResId = avatarResId,
                id = postId,
                username = username,
                textColor = MaterialTheme.colorScheme.onSurface,
            )
            Box(
                modifier = Modifier.sharedBounds(
                    rememberSharedContentState(
                        key = PostSharedElementKey(
                            postId = postId,
                            origin = PostOrigin.ExploreBubbles,
                            type = PostSharedElementType.Bubble
                        )
                    ),
                    animatedVisibilityScope = animatedContentScope,
                )
            ) {
                Text(
                    text = description,
                    style = MaterialTheme.typography.bodySmall,
                    modifier = Modifier
                        .padding(horizontal = 8.dp)
                        .padding(bottom = 8.dp)

                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun ExplorePostPreview() {
    val item = DummyPostRepository.dummyFeedItems.filterIsInstance<ImagePostFeedItem>().first()
    MubbleTheme {
        ExplorePost(
            item = item, onPostClick = { _, _ -> })
    }
}

@Preview(showBackground = true)
@Composable
private fun ExploreBubblePreview() {
    val item = DummyPostRepository.dummyFeedItems.filterIsInstance<BubbleFeedItem>().first()
    MubbleTheme {
        Surface {
            ExploreBubble(
                item = item, onPostClick = { _, _ -> })
        }
    }
}