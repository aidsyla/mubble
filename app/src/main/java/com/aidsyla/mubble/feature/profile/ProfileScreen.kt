package com.aidsyla.mubble.feature.profile

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.aidsyla.mubble.feature.explore.model.BubbleFeedItem
import com.aidsyla.mubble.feature.explore.model.ImagePostFeedItem
import com.aidsyla.mubble.feature.profile.components.bubbleList
import com.aidsyla.mubble.feature.profile.components.postGrid
import com.aidsyla.mubble.ui.LocalBottomBarPadding

@Composable
fun ProfileScreen(
    onNavigateToSettings: () -> Unit,
    onBackClick: () -> Unit,
    viewModel: ProfileViewModel = hiltViewModel(),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val bottomPadding = LocalBottomBarPadding.current
    when (val state = uiState) {
        ProfileScreenUiState.Loading -> {

        }

        is ProfileScreenUiState.CurrentUser -> {

        }

        is ProfileScreenUiState.OtherUser -> {
        }
    }
}

@Composable
fun ProfileBubbleList(
    modifier: Modifier = Modifier,
    items: List<BubbleFeedItem>,
) {
    val bottomPadding = LocalBottomBarPadding.current
    LazyColumn(
        modifier = modifier,
        contentPadding = PaddingValues(top = 240.dp, start = 8.dp, end = 8.dp, bottom = bottomPadding),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        bubbleList(items = items)
    }
}

@Composable
fun ProfilePostGrid(
    items: List<ImagePostFeedItem>,
    modifier: Modifier = Modifier,
) {
    LazyVerticalGrid(
        modifier = modifier,
        userScrollEnabled = true,
        columns = GridCells.Fixed(2),
        contentPadding = PaddingValues(8.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp, Alignment.Top)
    ) {
        postGrid(items = items)
        postGrid(items = items)
    }
}