package com.aidsyla.mubble.feature.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.aidsyla.mubble.feature.circle.AllButton
import com.aidsyla.mubble.feature.circle.CircleItem
import com.aidsyla.mubble.common.components.layout.MubbleListTabPager
import com.aidsyla.mubble.common.components.layout.MubbleTabRow
import com.aidsyla.mubble.common.navigation.shared_elements.PostOrigin
import com.aidsyla.mubble.feature.circle.model.CircleRepo
import com.aidsyla.mubble.ui.LocalBottomBarPadding
import com.aidsyla.mubble.ui.theme.MubbleTheme

val LocalPagerState = compositionLocalOf<PagerState?> { null }

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    modifier: Modifier = Modifier,
    viewModel: HomeViewModel = hiltViewModel(),
    onUserClick: (String) -> Unit,
    onPostClick: (postId: String, origin: PostOrigin) -> Unit,
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val tabTitles = remember { listOf("Following", "My Circles") }
    val pagerState = rememberPagerState { tabTitles.size }
    val bottomPadding = rememberStickyMaxPadding()

    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()
    val firstPageListState = rememberLazyListState()
    val secondPageListState = rememberLazyListState()

    LaunchedEffect(pagerState.currentPage) {
        val scrollOffset =
            if (scrollBehavior.state.collapsedFraction == 1f) -200
            else -16

        val targetIndex = if (pagerState.currentPage == 0) {
            viewModel.firstPageClickedIndex
        } else {
            viewModel.secondPageClickedIndex
        }

        if (targetIndex != -1) {
            val targetListState =
                if (pagerState.currentPage == 0) firstPageListState else secondPageListState

            targetListState.scrollToItem(targetIndex, scrollOffset)

            if (pagerState.currentPage == 0) {
                viewModel.firstPageClickedIndex = -1
            } else {
                viewModel.secondPageClickedIndex = -1
            }
        }
    }

    CompositionLocalProvider(
        LocalPagerState provides pagerState
    ) {
        MubbleListTabPager(
            modifier = modifier.padding(bottom = bottomPadding),
            scrollBehavior = scrollBehavior,
            pagerState = pagerState,
            firstPageListState = firstPageListState,
            secondPageListState = secondPageListState,
            firstPage = {
                HomeFollowingScreen(
                    uiState = uiState,
                    listState = it,
                    onUserClick = onUserClick,
                    onMoreClick = viewModel::onMoreClick,
                    onPostClick = { index, postId, origin ->
                        viewModel.firstPageClickedIndex = index
                        onPostClick(postId, origin)
                    }
                )
            },
            secondPage = {
                HomeMyCirclesScreen(
                    uiState = uiState,
                    listState = it,
                    onUserClick = onUserClick,
                    onMoreClick = viewModel::onMoreClick,
                    onPostClick = { index, postId, origin ->
                        viewModel.secondPageClickedIndex = index + 1
                        onPostClick(postId, origin)
                    }
                )
            },
            navigationIcon = {
                IconButton(onClick = {}, enabled = false) {
                    Icon(
                        painter = MubbleTheme.Icons.MubbleIcon, contentDescription = null,
                        tint = Color.Unspecified
                    )
                }
            },
            actions = {
                IconButton(onClick = {}) {
                    Icon(painter = MubbleTheme.Icons.Search, contentDescription = null)
                }
            }
        ) {
            MubbleTabRow(
                tabTitles = tabTitles,
                pagerState = pagerState
            )
        }
    }
}

@Composable
fun HomeFollowingScreen(
    modifier: Modifier = Modifier,
    uiState: PostListUiState,
    listState: LazyListState,
    onUserClick: (String) -> Unit,
    onMoreClick: (postId: String) -> Unit,
    onPostClick: (index: Int, postId: String, origin: PostOrigin) -> Unit,
) {
    LazyColumn(
        modifier = modifier.fillMaxSize(),
        state = listState,
        contentPadding = PaddingValues(start = 8.dp, end = 8.dp, top = 4.dp, bottom = 16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        postFeed(
            uiState = uiState,
            origin = PostOrigin.HomeFollowing,
            onUserClick = onUserClick,
            onMoreClick = onMoreClick,
            onPostClick = onPostClick
        )
    }
}

@Composable
fun HomeMyCirclesScreen(
    modifier: Modifier = Modifier,
    uiState: PostListUiState,
    listState: LazyListState,
    onUserClick: (String) -> Unit,
    onMoreClick: (postId: String) -> Unit,
    onPostClick: (index: Int, postId: String, origin: PostOrigin) -> Unit
) {
    LazyColumn(
        modifier = modifier.fillMaxSize(),
        state = listState,
        contentPadding = PaddingValues(top = 4.dp, bottom = 16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            LazyRow(
                contentPadding = PaddingValues(horizontal = 16.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(12.dp),
            ) {
                item {
                    AllButton()
                }
                items(CircleRepo.dummyCircles) {
                    CircleItem(
                        circle = it,
                        showIcon = false,
                        modifier = Modifier.fillParentMaxWidth(0.5f)
                    )
                }
            }
        }
        postFeed(
            uiState = uiState,
            origin = PostOrigin.HomeMyCircles,
            isCircleScreen = true,
            onUserClick = onUserClick,
            onMoreClick = onMoreClick,
            onPostClick = onPostClick
        )
    }
}

@Composable
fun rememberStickyMaxPadding(): Dp {
    val current = LocalBottomBarPadding.current
    val density = LocalDensity.current
    val currentPx = with(density) { current.roundToPx() }

    var maxPx by rememberSaveable { mutableIntStateOf(currentPx) }

    LaunchedEffect(currentPx) {
        if (currentPx > maxPx) {
            maxPx = currentPx
        }
    }

    return with(density) { maxPx.toDp() }
}