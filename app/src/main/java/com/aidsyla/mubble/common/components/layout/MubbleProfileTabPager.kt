package com.aidsyla.mubble.common.components.layout

import android.annotation.SuppressLint
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.DividerDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.PrimaryTabRow
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Tab
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.NestedScrollConnection
import androidx.compose.ui.input.nestedscroll.NestedScrollSource
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalWindowInfo
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import com.aidsyla.mubble.common.components.TabButtons
import com.aidsyla.mubble.common.components.for_reference.AnimatedIndicator
import com.aidsyla.mubble.common.components.for_reference.IndicatorVariant
import com.aidsyla.mubble.feature.profile.components.ProfileTopAppBar
import com.aidsyla.mubble.ui.theme.MubbleTheme
import kotlinx.coroutines.launch

const val STICKY_HEADER = "sticky_header"
const val STICKY_HEADER_INDEX = 1

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MubbleProfileTabPager(
    modifier: Modifier = Modifier,
    header: @Composable (() -> Unit)? = null,
    firstPage: @Composable () -> Unit,
    secondPage: @Composable () -> Unit,
    onNavigateToSettings: () -> Unit,
    onBackClick: () -> Unit,
) {
    val selectedIcons = MubbleTheme.ProfileTabs.iconsSelected
    val unselectedIcons = MubbleTheme.ProfileTabs.icons

    require(selectedIcons.size == 2 && unselectedIcons.size == 2) { "Icons count must match the number of pages." }

    val lazyListState = rememberLazyListState()

    val coroutineScope = rememberCoroutineScope()
    val pagerState = rememberPagerState(pageCount = { selectedIcons.size })
    val currentScreen = pagerState.currentPage
    val nestedScrollConnection = remember {
        object : NestedScrollConnection {
            override fun onPreScroll(
                available: Offset,
                source: NestedScrollSource,
            ): Offset {
                return if (available.y > 0) Offset.Zero else Offset(
                    x = 0f,
                    y = -lazyListState.dispatchRawDelta(-available.y)
                )
            }
        }
    }

    val statusBars = WindowInsets.statusBars.asPaddingValues().calculateTopPadding()
    val offsetAmount = TopAppBarDefaults.TopAppBarExpandedHeight + statusBars

    val tabRowHeight = 52.dp - 1.dp
    val pagerHeight = getScreenHeight() - offsetAmount - tabRowHeight

    val isHeaderDocked by rememberIsHeaderSticky(
        lazyListState = lazyListState,
        itemKey = STICKY_HEADER,
        offsetAmount = offsetAmount
    )
    val dividerAlpha by animateFloatAsState(
        targetValue = if (isHeaderDocked) 1f else 0f
    )

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
        LazyColumn(
            state = lazyListState,
            modifier = modifier.fillMaxSize(),
        ) {
            if (header != null)
                item {
                    header()
                }
            item(key = STICKY_HEADER) {
                Surface(
                    modifier = Modifier.zIndex(1f),
                    color = MaterialTheme.colorScheme.surface
                ) {
                    Column {
                        TabButtons(
                            modifier = Modifier
                                .padding(horizontal = 8.dp)
                                .padding(bottom = 3.dp),
                            spaceBetween = 8.dp,
                            selectedIcons = selectedIcons,
                            unselectedIcons = unselectedIcons,
                            selectedIndex = currentScreen,
                            onTabSelected = { index ->
                                coroutineScope.launch {
                                    pagerState.animateScrollToPage(index)
                                }
                            }
                        )
                        HorizontalDivider(
                            color = DividerDefaults.color.copy(alpha = dividerAlpha)
                        )
                    }
                }
            }
            item {
                HorizontalPager(
                    state = pagerState,
                    verticalAlignment = Alignment.Top,
                    modifier = Modifier
                        .nestedScroll(nestedScrollConnection)
                        .height(pagerHeight)
                ) { pageIndex ->
                    when (pageIndex) {
                        0 -> firstPage()
                        1 -> secondPage()
                    }
                }
            }
        }
    }
}

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MubbleProfileTabPager(
    modifier: Modifier = Modifier,
    titles: List<String>,
    header: @Composable (() -> Unit)? = null,
    firstPage: @Composable () -> Unit,
    secondPage: @Composable () -> Unit,
    onNavigateToSettings: () -> Unit,
    onBackClick: () -> Unit,
) {
    require(titles.size == 2) { "Titles and content must have the same size" }

    val lazyListState = rememberLazyListState()

    val coroutineScope = rememberCoroutineScope()
    val pagerState = rememberPagerState(pageCount = { titles.size })
    val currentScreen = pagerState.currentPage
    val nestedScrollConnection = remember {
        object : NestedScrollConnection {
            override fun onPreScroll(
                available: Offset,
                source: NestedScrollSource,
            ): Offset {
                return if (available.y > 0) Offset.Zero else Offset(
                    x = 0f,
                    y = -lazyListState.dispatchRawDelta(-available.y)
                )
            }
        }
    }

    val statusBars = WindowInsets.statusBars.asPaddingValues().calculateTopPadding()
    val offsetAmount = TopAppBarDefaults.TopAppBarExpandedHeight + statusBars

    val tabRowHeight = 52.dp - 1.dp
    val pagerHeight = getScreenHeight() - offsetAmount - tabRowHeight

    val isHeaderDocked by rememberIsHeaderSticky(
        lazyListState = lazyListState,
        itemKey = STICKY_HEADER,
        offsetAmount = offsetAmount
    )
    val dividerAlpha by animateFloatAsState(
        targetValue = if (isHeaderDocked) 1f else 0f
    )

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
        LazyColumn(
            state = lazyListState,
            modifier = modifier.fillMaxSize(),
        ) {
            if (header != null)
                item {
                    header()
                }
            item(key = STICKY_HEADER) {
                Surface(
                    modifier = Modifier.zIndex(1f),
                    color = MaterialTheme.colorScheme.surface
                ) {
                    PrimaryTabRow(
                        modifier = Modifier
                            .fillParentMaxWidth(),
                        selectedTabIndex = currentScreen,
                        indicator = {
                            AnimatedIndicator(
                                indicatorVariant = IndicatorVariant.PRIMARY,
                                pagerState = pagerState
                            )
                        },
                        divider = {
                            HorizontalDivider(
                                color = DividerDefaults.color.copy(alpha = dividerAlpha)
                            )
                        }
                    ) {
                        titles.forEachIndexed { index, title ->
                            Tab(
                                modifier = Modifier.zIndex(2f),
                                selected = currentScreen == index,
                                onClick = {
                                    coroutineScope.launch {
                                        pagerState.animateScrollToPage(index)
                                    }
                                },
                                text = { Text(title) },
                                selectedContentColor = MaterialTheme.colorScheme.primary,
                                unselectedContentColor = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                }
            }
            item {
                HorizontalPager(
                    state = pagerState,
                    verticalAlignment = Alignment.Top,
                    modifier = Modifier
                        .nestedScroll(nestedScrollConnection)
                        .height(pagerHeight)
                ) { pageIndex ->
                    when (pageIndex) {
                        0 -> firstPage()
                        1 -> secondPage()
                    }
                }
            }
        }
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

@Composable
fun getScreenHeight(): Dp {
    return with(LocalDensity.current) { LocalWindowInfo.current.containerSize.height.toDp() }
}

@Composable
fun getScreenWidth(): Dp {
    return with(LocalDensity.current) { LocalWindowInfo.current.containerSize.width.toDp() }
}


