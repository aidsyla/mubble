package com.aidsyla.mubble.common.components.layout

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.PrimaryTabRow
import androidx.compose.material3.Surface
import androidx.compose.material3.Tab
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
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
import kotlinx.coroutines.launch

const val STICKY_HEADER = "sticky_header_primary"
const val STICKY_HEADER_INDEX = 1

@Composable
fun TabbedPager(
    modifier: Modifier = Modifier,
    header: @Composable (() -> Unit)? = null,
    firstPage: @Composable () -> Unit,
    secondPage: @Composable () -> Unit,
    selectedIcons: List<Painter>,
    unselectedIcons: List<Painter>,
    lazyListState: LazyListState,
    scrolledColor: Color,
    shadowElevation: Dp,
    pagerHeight: Dp,
) {
    require(selectedIcons.size == 2) { "Selected icons count must match the number of pages." }
    require(unselectedIcons.size == 2) { "Unselected icons count must match the number of pages." }

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
                color = scrolledColor,
                shadowElevation = shadowElevation
            ) {
                TabButtons(
                    modifier = Modifier
                        .padding(horizontal = 8.dp)
                        .padding(bottom = 4.dp),
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

@Composable
fun TabbedPager(
    modifier: Modifier = Modifier,
    titles: List<String>,
    content: List<@Composable () -> Unit>,
    lazyListState: LazyListState,
    scrolledColor: Color,
    shadowElevation: Dp,
    pagerHeight: Dp,
    header: @Composable (() -> Unit)? = null,
) {
    require(titles.size == content.size) { "Titles and content must have the same size" }

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
                color = scrolledColor,
                shadowElevation = shadowElevation
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
                    divider = {}
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
                content[pageIndex].invoke()
            }
        }
    }
}

@Composable
fun getScreenHeight(): Dp {
    return with(LocalDensity.current) { LocalWindowInfo.current.containerSize.height.toDp() }
}

