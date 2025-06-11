package com.aidsyla.mubble.common.components.layout

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.grid.LazyGridState
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.DividerDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.lerp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MubbleListTabPager(
    modifier: Modifier = Modifier,
    pagerState: PagerState,
    firstPage: @Composable (LazyListState) -> Unit,
    secondPage: @Composable (LazyListState) -> Unit,
    navigationIcon: @Composable () -> Unit = {},
    actions: @Composable RowScope.() -> Unit = {},
    appBarTitle: @Composable () -> Unit,
) {
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()

    val firstPageListState = rememberLazyListState()
    val secondPageListState = rememberLazyListState()

    val isAtTopFirst = firstPageListState.rememberIsAtTop()
    val isAtTopSecond = secondPageListState.rememberIsAtTop()

    val state =
        rememberCollapsingTopBarScreenState(
            scrollBehavior,
            pagerState,
            isAtTopFirst,
            isAtTopSecond
        )

    val dividerAlpha by animateFloatAsState(
        targetValue = state.targetDividerAlpha, label = "dividerAlpha"
    )
    val appBarAlpha by animateFloatAsState(
        targetValue = state.targetAppBarAlpha, label = "appBarAlpha"
    )

    val animatedAppBarColor = MaterialTheme.colorScheme.surface.copy(alpha = appBarAlpha)

    Scaffold(
        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            Surface(
                color = animatedAppBarColor
            ) {
                Column {
                    CenterAlignedTopAppBar(
                        colors = TopAppBarDefaults.topAppBarColors(
                            containerColor = Color.Transparent,
                            scrolledContainerColor = Color.Transparent,
                            navigationIconContentColor = MaterialTheme.colorScheme.onSurface,
                            titleContentColor = MaterialTheme.colorScheme.onSurface,
                            actionIconContentColor = MaterialTheme.colorScheme.onSurfaceVariant
                        ),
                        navigationIcon = navigationIcon,
                        title = appBarTitle,
                        actions = actions,
                        scrollBehavior = scrollBehavior
                    )
                    HorizontalDivider(
                        color = DividerDefaults.color.copy(alpha = dividerAlpha)
                    )
                }
            }
        }, contentWindowInsets = WindowInsets(0.dp)
    ) {
        val topInset = it.calculateTopPadding()
        val pagerTopPadding: Dp = if (state.collapsedFraction != 1f) {
            lerp(start = topInset, stop = 0.dp, fraction = state.collapsedFraction)
        } else {
            0.dp
        }

        HorizontalPager(
            state = pagerState,
            verticalAlignment = Alignment.Top,
            modifier = modifier
                .fillMaxSize()
                .padding(top = pagerTopPadding)
        ) { pageIndex ->
            when (pageIndex) {
                0 -> firstPage(firstPageListState)
                1 -> secondPage(secondPageListState)
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MubbleGridTabPager(
    modifier: Modifier = Modifier,
    pagerState: PagerState,
    firstPage: @Composable (LazyGridState) -> Unit,
    secondPage: @Composable (LazyGridState) -> Unit,
    navigationIcon: @Composable () -> Unit = {},
    actions: @Composable RowScope.() -> Unit = {},
    appBarTitle: @Composable () -> Unit,
) {
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()

    val firstPageListState = rememberLazyGridState()
    val secondPageListState = rememberLazyGridState()

    val isAtTopFirst = firstPageListState.rememberIsAtTop()
    val isAtTopSecond = secondPageListState.rememberIsAtTop()

    val state =
        rememberCollapsingTopBarScreenState(
            scrollBehavior,
            pagerState,
            isAtTopFirst,
            isAtTopSecond
        )

    val dividerAlpha by animateFloatAsState(
        targetValue = state.targetDividerAlpha, label = "dividerAlpha"
    )
    val appBarAlpha by animateFloatAsState(
        targetValue = state.targetAppBarAlpha, label = "appBarAlpha"
    )

    val animatedAppBarColor = MaterialTheme.colorScheme.surface.copy(alpha = appBarAlpha)

    Scaffold(
        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection), topBar = {
            Surface(
                color = animatedAppBarColor
            ) {
                Column {
                    CenterAlignedTopAppBar(
                        colors = TopAppBarDefaults.topAppBarColors(
                            containerColor = Color.Transparent,
                            scrolledContainerColor = Color.Transparent,
                            navigationIconContentColor = MaterialTheme.colorScheme.onSurface,
                            titleContentColor = MaterialTheme.colorScheme.onSurface,
                            actionIconContentColor = MaterialTheme.colorScheme.onSurfaceVariant
                        ),
                        navigationIcon = navigationIcon,
                        title = appBarTitle,
                        actions = actions,
                        scrollBehavior = scrollBehavior
                    )
                    HorizontalDivider(
                        color = DividerDefaults.color.copy(alpha = dividerAlpha)
                    )
                }
            }
        }, contentWindowInsets = WindowInsets(0.dp)
    ) {
        val topInset = it.calculateTopPadding()
        val pagerTopPadding: Dp = if (state.collapsedFraction != 1f) {
            lerp(start = topInset, stop = 0.dp, fraction = state.collapsedFraction)
        } else {
            0.dp
        }

        HorizontalPager(
            state = pagerState,
            verticalAlignment = Alignment.Top,
            modifier = modifier
                .fillMaxSize()
                .padding(top = pagerTopPadding)
        ) { pageIndex ->
            when (pageIndex) {
                0 -> firstPage(firstPageListState)
                1 -> secondPage(secondPageListState)
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
class CollapsingTopBarScreenState(
    private val scrollBehavior: TopAppBarScrollBehavior,
    private val pagerState: PagerState,
    private val isAtTopFirst: Boolean,
    private val isAtTopSecond: Boolean,
) {
    val collapsedFraction: Float
        get() = scrollBehavior.state.collapsedFraction

    val contentOffset: Float
        get() = scrollBehavior.state.contentOffset

    val targetAppBarAlpha: Float
        get() = if ((1f - collapsedFraction) < 0.01f) 0f else 1f

    val targetDividerAlpha: Float
        get() {
            val notAtTop =
                if (pagerState.currentPage == 0) !isAtTopFirst else !isAtTopSecond

            return if (targetAppBarAlpha == 1f && collapsedFraction == 0f && notAtTop) {
                1f
            } else {
                0f
            }
        }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun rememberCollapsingTopBarScreenState(
    scrollBehavior: TopAppBarScrollBehavior,
    pagerState: PagerState,
    isAtTopFirst: Boolean,
    isAtTopSecond: Boolean,
): CollapsingTopBarScreenState {
    return remember(scrollBehavior, pagerState, isAtTopFirst, isAtTopSecond) {
        CollapsingTopBarScreenState(
            scrollBehavior, pagerState, isAtTopFirst, isAtTopSecond
        )
    }
}

@Composable
fun LazyGridState.rememberIsAtTop(): Boolean {
    return remember {
        derivedStateOf {
            firstVisibleItemIndex == 0 && firstVisibleItemScrollOffset == 0
        }
    }.value
}

@Composable
fun LazyListState.rememberIsAtTop(): Boolean {
    return remember {
        derivedStateOf {
            firstVisibleItemIndex == 0 && firstVisibleItemScrollOffset == 0
        }
    }.value
}
