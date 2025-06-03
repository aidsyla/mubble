package com.aidsyla.mubble.feature.profile

import android.annotation.SuppressLint
import androidx.compose.animation.animateColor
import androidx.compose.animation.core.animateDp
import androidx.compose.animation.core.updateTransition
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.aidsyla.mubble.common.components.layout.STICKY_HEADER
import com.aidsyla.mubble.common.components.layout.MubbleProfileTabPager
import com.aidsyla.mubble.common.components.layout.getScreenHeight
import com.aidsyla.mubble.feature.explore.model.BubbleFeedItem
import com.aidsyla.mubble.feature.explore.model.ImagePostFeedItem
import com.aidsyla.mubble.feature.profile.components.ProfileHeader
import com.aidsyla.mubble.feature.profile.components.ProfileTopAppBar
import com.aidsyla.mubble.feature.profile.components.bubbleList
import com.aidsyla.mubble.feature.profile.components.postGrid
import com.aidsyla.mubble.ui.LocalBottomBarPadding
import com.aidsyla.mubble.ui.theme.MubbleTheme

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(
    viewModel: ProfileViewModel = hiltViewModel(),
    onNavigateToSettings: () -> Unit,
    onBackClick: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val lazyListState = rememberLazyListState()

    val statusBars = WindowInsets.statusBars.asPaddingValues().calculateTopPadding()
    val offsetAmount = TopAppBarDefaults.TopAppBarExpandedHeight + statusBars

    val tabRowHeight = 52.dp - 1.dp
    val pagerHeight = getScreenHeight() - offsetAmount - tabRowHeight

    val isHeaderDocked by rememberIsHeaderSticky(
        lazyListState = lazyListState,
        itemKey = STICKY_HEADER,
        offsetAmount = offsetAmount
    )

    val transition = updateTransition(targetState = isHeaderDocked, label = "headerTransition")
    val scrolledColor by transition.animateColor(label = "color") { docked ->
        if (docked) MaterialTheme.colorScheme.surfaceContainer else MaterialTheme.colorScheme.surface
    }
    val shadowElevation by transition.animateDp(label = "elevation") { docked ->
        if (docked) 3.dp else 0.dp
    }

    when (val state = uiState) {
        ProfileScreenUiState.Loading -> {

        }

        is ProfileScreenUiState.CurrentUser -> {
            Scaffold(
                modifier = Modifier,
                topBar = {
                    ProfileTopAppBar(
                        modifier = Modifier,
                        lazyListState = lazyListState,
                        offsetAmount = offsetAmount,
                        onNavigateToSettings = onNavigateToSettings,
                        onBackClick = onBackClick
                    )
                },
                contentWindowInsets = WindowInsets(0, 0, 0, 0)
            ) {
                ProfileScreenContent(
                    modifier = Modifier,
                    posts = state.posts,
                    bubbles = state.bubbles,
                    lazyListState = lazyListState,
                    shadowElevation = shadowElevation,
                    pagerHeight = pagerHeight,
                )
            }
        }

        is ProfileScreenUiState.OtherUser -> {
        }
    }
}

@Composable
fun ProfileScreenContent(
    modifier: Modifier = Modifier,
    posts: List<ImagePostFeedItem>,
    bubbles: List<BubbleFeedItem>,
    lazyListState: LazyListState,
    scrolledColor: Color? = null,
    shadowElevation: Dp,
    pagerHeight: Dp,
) {
    MubbleProfileTabPager(
        modifier = modifier,
        header = {
            ProfileHeader()
        },
        firstPage = {
            ProfilePostGrid(items = posts)
        },
        secondPage = {
            ProfileBubbleList(items = bubbles)
        },
        selectedIcons = MubbleTheme.ProfileTabs.iconsSelected,
        unselectedIcons = MubbleTheme.ProfileTabs.icons,
        lazyListState = lazyListState,
        scrolledColor = scrolledColor,
        shadowElevation = shadowElevation,
        pagerHeight = pagerHeight,
    )
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

@Composable
fun rememberIsHeaderSticky(
    lazyListState: LazyListState,
    itemKey: Any,
    offsetAmount: Dp,
): State<Boolean> {
    val currentDensity = LocalDensity.current
    return remember(offsetAmount) {
        derivedStateOf {
            val layoutInfo = lazyListState.layoutInfo
            val visibleItems = layoutInfo.visibleItemsInfo
            val headerItem = visibleItems.find { it.key == itemKey }

            if (headerItem != null) {
                val headerOffsetDp = with(currentDensity) { headerItem.offset.toDp() }
                headerOffsetDp < offsetAmount
            } else {
                false
//                0.dp < offsetAmount
            }
        }
    }
}
