package com.aidsyla.mubble.feature.profile.currentuser

import androidx.compose.animation.EnterExitState
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.animation.core.animateDp
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.BottomAppBarDefaults
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.aidsyla.mubble.common.navigation.LocalNavAnimatedVisibilityScope
import com.aidsyla.mubble.common.navigation.LocalSharedTransitionScope
import com.aidsyla.mubble.common.navigation.sharedelements.NewPostSharedElementKey
import com.aidsyla.mubble.common.navigation.sharedelements.PostOrigin
import com.aidsyla.mubble.data.BubbleFeedItem
import com.aidsyla.mubble.data.ImagePostFeedItem
import com.aidsyla.mubble.feature.explore.exploreBubblesFeed
import com.aidsyla.mubble.feature.profile.components.FullScreenMediaType
import com.aidsyla.mubble.feature.profile.components.MubbleProfileTabPager
import com.aidsyla.mubble.feature.profile.components.ProfileHeader
import com.aidsyla.mubble.feature.profile.components.postGrid
import com.aidsyla.mubble.ui.LocalBottomBarPadding
import com.aidsyla.mubble.ui.theme.MubbleDesignSystem

@OptIn(ExperimentalMaterial3ExpressiveApi::class, ExperimentalSharedTransitionApi::class)
@Composable
fun ProfileScreen(
    viewModel: ProfileViewModel = hiltViewModel(),
    onNavigateToSettings: () -> Unit,
    onPostClick: (postId: String, origin: PostOrigin) -> Unit,
    onMediaClick: (Int, FullScreenMediaType) -> Unit,
    onFollowersClick: () -> Unit,
    onFollowingClick: () -> Unit,
    onNewPostClick: () -> Unit
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

    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    var isClicked by rememberSaveable { mutableStateOf(false) }

    when (val state = uiState) {
        ProfileScreenUiState.Loading -> {
        }

        is ProfileScreenUiState.Success -> {
            MubbleProfileTabPager(
                modifier = Modifier,
                title = state.user.displayName,
                isCurrentUser = true,
                header = {
                    ProfileHeader(
                        user = state.user,
                        isCurrentUser = true,
                        hasAvatarOrBannerBeenClicked = isClicked,
                        onHasBeenClickedChange = { isClicked = it },
                        onMediaClick = onMediaClick,
                        onFollowersClick = onFollowersClick,
                        onFollowingClick = onFollowingClick
                    )
                },
                firstPage = {
                    ProfilePostGrid(
                        items = state.posts,
                        onPostClick = { p1, p2 ->
                            isClicked = false
                            onPostClick(p1, p2)
                        }
                    )
                },
                secondPage = {
                    ProfileBubbleGrid(
                        items = state.bubbles,
                        onPostClick = onPostClick
                    )
                },
                floatingActionButton = {
                    with(sharedTransitionScope) {
                        FloatingActionButton(
                            onClick = onNewPostClick,
                            modifier = Modifier
                                .navigationBarsPadding()
                                .padding(bottom = BottomAppBarDefaults.FlexibleBottomAppBarHeight)
                                .sharedBounds(
                                    sharedContentState = rememberSharedContentState(
                                        key = NewPostSharedElementKey
                                    ),
                                    animatedVisibilityScope = animatedContentScope,
                                    resizeMode = SharedTransitionScope.ResizeMode.ScaleToBounds(contentScale = ContentScale.Crop),
                                    clipInOverlayDuringTransition =
                                    OverlayClip(
                                        RoundedCornerShape(
                                            roundedCornerAnimation
                                        )
                                    )
                                ),
                            containerColor = MaterialTheme.colorScheme.primary
                        ) {
                            Icon(MubbleDesignSystem.Icons.Add, contentDescription = null)
                        }
                    }
                },
                onNavigateToSettings = onNavigateToSettings
            )
        }
    }
}

@Composable
fun ProfilePostGrid(
    modifier: Modifier = Modifier,
    items: List<ImagePostFeedItem>,
    onPostClick: (postId: String, origin: PostOrigin) -> Unit
) {
    val bottomPadding = LocalBottomBarPadding.current
    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        modifier = modifier.fillMaxSize(),
        contentPadding =
        PaddingValues(
            top = 4.dp,
            start = 8.dp,
            end = 8.dp,
            bottom = bottomPadding + 8.dp
        ),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp, Alignment.Top)
    ) {
        postGrid(
            items = items,
            onPostClick = onPostClick
        )
    }
}

@Composable
fun ProfileBubbleGrid(
    modifier: Modifier = Modifier,
    items: List<BubbleFeedItem>,
    onPostClick: (postId: String, origin: PostOrigin) -> Unit
) {
    val bottomPadding = LocalBottomBarPadding.current
    LazyVerticalStaggeredGrid(
        columns = StaggeredGridCells.Fixed(2),
        contentPadding =
        PaddingValues(
            top = 4.dp,
            start = 8.dp,
            end = 8.dp,
            bottom = bottomPadding + 8.dp
        ),
        verticalItemSpacing = 8.dp,
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        modifier = modifier.fillMaxSize()
    ) {
        exploreBubblesFeed(
            items = items,
            onProfileClick = {},
            onPostClick = onPostClick
        )
    }
}
