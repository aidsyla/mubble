package com.aidsyla.mubble.feature.circle

import android.annotation.SuppressLint
import androidx.compose.animation.EnterExitState
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.animation.core.animateDp
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.DividerDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.input.nestedscroll.NestedScrollConnection
import androidx.compose.ui.input.nestedscroll.NestedScrollSource
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.aidsyla.mubble.common.components.ScrollReactingTopAppBar
import com.aidsyla.mubble.common.navigation.LocalNavAnimatedVisibilityScope
import com.aidsyla.mubble.common.navigation.LocalSharedTransitionScope
import com.aidsyla.mubble.common.navigation.shared_elements.PostOrigin
import com.aidsyla.mubble.common.navigation.shared_elements.PostSharedElementKey
import com.aidsyla.mubble.common.navigation.shared_elements.PostSharedElementType
import com.aidsyla.mubble.data.FeedItem
import com.aidsyla.mubble.feature.circle.model.Circle
import com.aidsyla.mubble.feature.profile.components.FullScreenMediaType
import com.aidsyla.mubble.feature.profile.components.STICKY_HEADER
import com.aidsyla.mubble.feature.profile.components.getScreenHeight
import com.aidsyla.mubble.feature.profile.components.rememberIsHeaderSticky
import com.aidsyla.mubble.ui.theme.MubbleTheme

@Composable
fun CircleScreen(
    modifier: Modifier = Modifier,
    viewModel: CircleViewModel = hiltViewModel(),
    origin: PostOrigin,
    onUserClick: (String) -> Unit,
    onPostClick: (postId: String, origin: PostOrigin) -> Unit,
    onMediaClick: (Int, FullScreenMediaType) -> Unit,
    onBackClick: () -> Unit,
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    when (val state = uiState) {
        CircleUiState.Loading -> {
        }
        is CircleUiState.Success -> {
            CircleContent(
                modifier = modifier,
                origin = origin,
                circle = state.circle,
                items = state.items,
                header = {
                    CircleHeader(
                        title = state.circle.name,
                        memberCount = state.circle.memberCount,
                        bannerResId = state.circle.bannerResId,
                        onMediaClick = onMediaClick,
                    )
                },
                onMoreClick = {},
                onUserClick = onUserClick,
                onPostClick = onPostClick,
                onBackClick = onBackClick,
            )
        }
    }
}

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class, ExperimentalSharedTransitionApi::class)
@Composable
fun CircleContent(
    modifier: Modifier = Modifier,
    origin: PostOrigin,
    circle: Circle,
    items: List<FeedItem>,
    header: @Composable () -> Unit,
    onMoreClick: () -> Unit,
    onUserClick: (String) -> Unit,
    onPostClick: (String, PostOrigin) -> Unit,
    onBackClick: () -> Unit,
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

    val lazyListState = rememberLazyListState()

    val nestedScrollConnection =
        remember {
            object : NestedScrollConnection {
                override fun onPreScroll(
                    available: Offset,
                    source: NestedScrollSource,
                ): Offset =
                    if (available.y > 0) {
                        Offset.Zero
                    } else {
                        Offset(
                            x = 0f,
                            y = -lazyListState.dispatchRawDelta(-available.y),
                        )
                    }
            }
        }

    val statusBars = WindowInsets.statusBars.asPaddingValues().calculateTopPadding()
    val offsetAmount = TopAppBarDefaults.TopAppBarExpandedHeight + statusBars

    val tabRowHeight = 48.dp - 1.dp
    val lazyColumnHeight = getScreenHeight() - offsetAmount - tabRowHeight

    val isHeaderDocked by rememberIsHeaderSticky(
        lazyListState = lazyListState,
        itemKey = STICKY_HEADER,
        offsetAmount = offsetAmount,
    )
    val dividerAlpha by animateFloatAsState(
        targetValue = if (isHeaderDocked) 1f else 0f,
    )

    with(sharedTransitionScope) {
        Scaffold(
            modifier =
                Modifier
                    .sharedBounds(
                        rememberSharedContentState(
                            key =
                                PostSharedElementKey(
                                    postId = circle.id,
                                    origin = origin,
                                    type = PostSharedElementType.Bounds,
                                ),
                        ),
                        resizeMode = SharedTransitionScope.ResizeMode.ScaleToBounds(contentScale = ContentScale.Crop),
                        clipInOverlayDuringTransition =
                            OverlayClip(
                                RoundedCornerShape(
                                    roundedCornerAnimation,
                                ),
                            ),
                        enter = EnterTransition.None,
                        exit = ExitTransition.None,
                        animatedVisibilityScope = animatedContentScope,
                    ),
            topBar = {
                ScrollReactingTopAppBar(
                    modifier = Modifier,
                    title = circle.name,
                    isCurrentUser = false,
                    lazyListState = lazyListState,
                    offsetAmount = offsetAmount,
                    onMoreClick = onMoreClick,
                    onBackClick = onBackClick,
                    onNavigateToSettings = {},
                    onEditClick = {},
                )
            },
            contentWindowInsets = WindowInsets(0, 0, 0, 0),
        ) {
            LazyColumn(
                state = lazyListState,
                modifier = modifier.fillMaxSize(),
            ) {
                item {
                    header()
                }
                item(key = STICKY_HEADER) {
                    Surface(
                        modifier =
                            Modifier
                                .zIndex(1f),
                        color = MaterialTheme.colorScheme.surface,
                    ) {
                        Column {
                            ChipsRow(
                                modifier =
                                    Modifier
                                        .padding(horizontal = 16.dp),
                            )
                            HorizontalDivider(
                                color = DividerDefaults.color.copy(alpha = dividerAlpha),
                            )
                        }
                    }
                }
                item {
                    LazyColumn(
                        modifier =
                            modifier
                                .fillMaxSize()
                                .height(lazyColumnHeight)
                                .nestedScroll(nestedScrollConnection),
                        contentPadding =
                            PaddingValues(
                                start = 8.dp,
                                end = 8.dp,
                                top = 4.dp,
                                bottom = 16.dp,
                            ),
                        verticalArrangement = Arrangement.spacedBy(16.dp),
                    ) {
                        circlePostFeed(
                            items = items,
                            origin = PostOrigin.HomeFollowing,
                            onUserClick = onUserClick,
                            onMoreClick = onMoreClick,
                            onPostClick = onPostClick,
                        )
                    }
                }
            }
        }
    }
}

@Preview
@Composable
private fun CircleScreenPreview() {
    MubbleTheme {
        Surface {
            CircleScreen(
                origin = PostOrigin.HomeMyCircles,
                onUserClick = { },
                onMediaClick = { _, _ -> },
                onPostClick = { _, _ -> },
                onBackClick = {},
            )
        }
    }
}
