package com.aidsyla.mubble.feature.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.FilledIconButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.aidsyla.mubble.common.components.circle.AllButton
import com.aidsyla.mubble.common.components.circle.CircleItem
import com.aidsyla.mubble.common.components.layout.MubbleListTabPager
import com.aidsyla.mubble.common.components.layout.MubbleTabRow
import com.aidsyla.mubble.ui.LocalBottomBarPadding
import com.aidsyla.mubble.ui.theme.MubbleTheme

@Composable
fun HomeScreen(
    modifier: Modifier = Modifier,
    viewModel: HomeViewModel = hiltViewModel(),
    onUserClick: (String) -> Unit,
    onPostClick: (postId: String) -> Unit,
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val tabTitles = remember { listOf("Following", "My Circles") }
    val pagerState = rememberPagerState { tabTitles.size }
    val bottomPadding = LocalBottomBarPadding.current

    MubbleListTabPager(
        modifier = Modifier.padding(bottom = bottomPadding),
        pagerState = pagerState,
        firstPage = {
            PostList(
                uiState = uiState,
                listState = it,
                onUserClick = onUserClick,
                onMoreClick = viewModel::onMoreClick,
                onPostClick = onPostClick
            )
        },
        secondPage = {
            CircleScreen(
                uiState = uiState,
                listState = it,
                onUserClick = onUserClick,
                onMoreClick = viewModel::onMoreClick,
                onPostClick = onPostClick
            )
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

@Composable
fun PostList(
    modifier: Modifier = Modifier,
    uiState: PostListUiState,
    listState: LazyListState,
    onUserClick: (String) -> Unit,
    onMoreClick: (postId: String) -> Unit,
    onPostClick: (postId: String) -> Unit,
) {
    LazyColumn(
        modifier = modifier.fillMaxSize(),
        state = listState,
        contentPadding = PaddingValues(start = 8.dp, end = 8.dp, top = 4.dp, bottom = 16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        postFeed(
            uiState = uiState,
            onUserClick = onUserClick,
            onMoreClick = onMoreClick,
            onPostClick = onPostClick
        )
    }
}

@Composable
fun CircleScreen(
    modifier: Modifier = Modifier,
    uiState: PostListUiState,
    listState: LazyListState,
    onUserClick: (String) -> Unit,
    onMoreClick: (postId: String) -> Unit,
    onPostClick: (postId: String) -> Unit,
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
                items(7) {
                    CircleItem(isFullWidth = false, modifier = Modifier.fillParentMaxWidth(0.5f)) {
                        FilledIconButton(onClick = {}) {
                            Icon(imageVector = Icons.Default.Add, contentDescription = null)
                        }
                    }
                }
            }
        }
        postFeed(
            uiState = uiState,
            onUserClick = onUserClick,
            onMoreClick = onMoreClick,
            onPostClick = onPostClick,
            isCircleScreen = true
        )
    }
}