package com.aidsyla.mubble.feature.profile

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.aidsyla.mubble.common.components.layout.MubbleProfileTabPager
import com.aidsyla.mubble.feature.explore.model.BubbleFeedItem
import com.aidsyla.mubble.feature.explore.model.ImagePostFeedItem
import com.aidsyla.mubble.feature.profile.components.ProfileHeader
import com.aidsyla.mubble.feature.profile.components.bubbleList
import com.aidsyla.mubble.feature.profile.components.postGrid
import com.aidsyla.mubble.ui.LocalBottomBarPadding
import com.aidsyla.mubble.ui.theme.MubbleTheme

@Composable
fun ProfileScreen(
    viewModel: ProfileViewModel = hiltViewModel(),
    onNavigateToSettings: () -> Unit,
    onBackClick: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    when (val state = uiState) {
        ProfileScreenUiState.Loading -> {

        }

        is ProfileScreenUiState.CurrentUser -> {
            MubbleProfileTabPager(
                modifier = Modifier,
                header = {
                    ProfileHeader()
                },
                firstPage = {
                    ProfilePostGrid(items = state.posts)
                },
                secondPage = {
                    ProfileBubbleList(items = state.bubbles)
                },
                onNavigateToSettings = onNavigateToSettings,
                onBackClick = onBackClick
            )
        }

        is ProfileScreenUiState.OtherUser -> {
        }
    }
}

@Composable
fun ProfilePostGrid(
    modifier: Modifier = Modifier,
    items: List<ImagePostFeedItem>,
) {
    val bottomPadding = LocalBottomBarPadding.current
    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        modifier = modifier.fillMaxSize(),
        contentPadding = PaddingValues(
            top = 4.dp,
            start = 8.dp,
            end = 8.dp,
            bottom = bottomPadding + 8.dp
        ),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp, Alignment.Top)
    ) {
        postGrid(items = items)
    }
}

@Composable
fun ProfileBubbleList(
    modifier: Modifier = Modifier,
    items: List<BubbleFeedItem>,
) {
    val bottomPadding = LocalBottomBarPadding.current
    LazyColumn(
        modifier = modifier.fillMaxSize(),
        contentPadding = PaddingValues(
            top = 4.dp,
            start = 8.dp,
            end = 8.dp,
            bottom = bottomPadding + 8.dp
        ),
        verticalArrangement = Arrangement.spacedBy(8.dp, Alignment.Top)
    ) {
        bubbleList(items = items)
    }
}
