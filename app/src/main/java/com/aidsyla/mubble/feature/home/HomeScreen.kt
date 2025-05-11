package com.aidsyla.mubble.feature.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilledIconButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.aidsyla.mubble.common.components.circle.AllButton
import com.aidsyla.mubble.common.components.circle.CircleItem
import com.aidsyla.mubble.common.components.layout.TabbedPager
import com.aidsyla.mubble.ui.LocalBottomBarPadding

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    onUserClick: (String) -> Unit,
    onPostClick: (postId: String) -> Unit,
    viewModel: HomeViewModel = hiltViewModel(),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    val scrollState = TopAppBarDefaults.enterAlwaysScrollBehavior()
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text("mubble")
                },
                scrollBehavior = scrollState
            )
        }
    ) {
        val bottomPadding = LocalBottomBarPadding.current
        TabbedPagerHome(
            modifier = Modifier
                .padding(
                    top = it.calculateTopPadding(),
                    bottom = bottomPadding
                )
                .nestedScroll(scrollState.nestedScrollConnection),
            postListUiState = uiState,
            onUserClick = onUserClick,
            onMoreClick = viewModel::onMoreClick,
            onPostClick = onPostClick
        )
    }
}

@Composable
fun TabbedPagerHome(
    modifier: Modifier = Modifier,
    postListUiState: PostListUiState,
    onUserClick: (String) -> Unit,
    onMoreClick: (postId: String) -> Unit,
    onPostClick: (postId: String) -> Unit,
) {
    val titles = listOf("Following", "My Circles")
    val tabContent: List<@Composable () -> Unit> = listOf(
        {
            PostList(
                uiState = postListUiState,
                onUserClick = onUserClick,
                onMoreClick = onMoreClick,
                onPostClick = onPostClick
            )
        },
        {
            CircleScreen(
                uiState = postListUiState,
                onUserClick = onUserClick,
                onMoreClick = onMoreClick,
                onPostClick = onPostClick
            )
        },
    )
    TabbedPager(modifier = modifier, titles = titles, tabContent = tabContent)
}

@Composable
fun PostList(
    modifier: Modifier = Modifier,
    uiState: PostListUiState,
    onUserClick: (String) -> Unit,
    onMoreClick: (postId: String) -> Unit,
    onPostClick: (postId: String) -> Unit,
) {
    LazyColumn(
        modifier = modifier.fillMaxSize(),
        contentPadding = PaddingValues(vertical = 16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        postFeed(
            items = uiState.items,
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
    onUserClick: (String) -> Unit,
    onMoreClick: (postId: String) -> Unit,
    onPostClick: (postId: String) -> Unit,
) {
    LazyColumn(
        contentPadding = PaddingValues(vertical = 16.dp),
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
            items = uiState.items,
            onUserClick = onUserClick,
            onMoreClick = onMoreClick,
            onPostClick = onPostClick
        )
    }
}