package com.aidsyla.mubble.common.components.layout

import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.PrimaryTabRow
import androidx.compose.material3.Tab
import androidx.compose.material3.TabIndicatorScope
import androidx.compose.material3.TabPosition
import androidx.compose.material3.TabRowDefaults.PrimaryIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.input.nestedscroll.NestedScrollConnection
import androidx.compose.ui.input.nestedscroll.NestedScrollSource
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.layout.Measurable
import androidx.compose.ui.unit.Constraints
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.lerp
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TabbedPager(
    titles: List<String>,
    modifier: Modifier = Modifier,
    tabContent: List<@Composable () -> Unit>,
    header: @Composable () -> Unit = {},
) {
    val coroutineScope = rememberCoroutineScope()
    val pagerState = rememberPagerState(pageCount = { titles.size })
    val currentScreen = pagerState.currentPage
    val scrollState = rememberScrollState()

    BoxWithConstraints(
        modifier = modifier
    ) {
        val screenHeight = maxHeight
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(scrollState)
        ) {
            header()
            Column(
                modifier = Modifier.height(screenHeight)
            ) {
                PrimaryTabRow(
                    selectedTabIndex = currentScreen,
                    indicator = {
                        AnimatedIndicatorWithPageOffset(
                            currentPage = pagerState.currentPage,
                            pageOffset = pagerState.currentPageOffsetFraction
                        )
                    }
                ) {
                    titles.forEachIndexed { index, title ->
                        Tab(
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
                val nestedScrollConnection = remember {
                    object : NestedScrollConnection {
                        override fun onPreScroll(
                            available: Offset,
                            source: NestedScrollSource,
                        ): Offset {
                            return if (available.y > 0) Offset.Zero else Offset(
                                x = 0f,
                                y = -scrollState.dispatchRawDelta(-available.y)
                            )
                        }
                    }
                }
                HorizontalPager(
                    state = pagerState,
                    verticalAlignment = Alignment.Top,
                    modifier = Modifier
                        .wrapContentHeight()
                        .nestedScroll(nestedScrollConnection)
                ) { pageIndex ->
                    tabContent[pageIndex].invoke()
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TabIndicatorScope.AnimatedIndicatorWithPageOffset(
    currentPage: Int,
    pageOffset: Float,
) {
    val baseWidth = 36.dp
    val maxExtraWidth = 56.dp

    val factor = if (pageOffset >= 0f) {
        (pageOffset / 0.5f).coerceAtMost(1f)
    } else {
        ((-pageOffset) / 0.5f).coerceAtMost(1f)
    }

    val dynamicWidth = baseWidth + (maxExtraWidth * factor)
    PrimaryIndicator(
        width = dynamicWidth,
        modifier = Modifier
            .tabIndicatorLayout { measurable: Measurable, constraints: Constraints, tabPositions: List<TabPosition> ->
                val currentTab = tabPositions.getOrNull(currentPage) ?: tabPositions.first()
                val nextTab = tabPositions.getOrNull(currentPage + 1)

                val indicatorStart: Dp = when {
                    nextTab != null -> lerp(
                        start = currentTab.left,
                        stop = nextTab.left,
                        fraction = pageOffset
                    )

                    pageOffset < 0f && currentPage > 0 -> {
                        val previousTab = tabPositions[currentPage - 1]
                        lerp(
                            start = previousTab.left,
                            stop = currentTab.left,
                            fraction = pageOffset + 1f
                        )
                    }

                    else -> currentTab.left
                }

                val indicatorEnd: Dp = when {
                    nextTab != null -> lerp(
                        start = currentTab.right,
                        stop = nextTab.right,
                        fraction = pageOffset
                    )

                    pageOffset < 0f && currentPage > 0 -> {
                        val previousTab = tabPositions[currentPage - 1]
                        lerp(
                            start = previousTab.right,
                            stop = currentTab.right,
                            fraction = pageOffset + 1f
                        )
                    }

                    else -> currentTab.right
                }

                val indicatorStartPx = indicatorStart.roundToPx()
                val indicatorEndPx = indicatorEnd.roundToPx()
                val indicatorWidth = indicatorEndPx - indicatorStartPx

                val placeable = measurable.measure(
                    constraints.copy(
                        minWidth = indicatorWidth,
                        maxWidth = indicatorWidth
                    )
                )
                layout(constraints.maxWidth, constraints.maxHeight) {
                    placeable.placeRelative(
                        x = indicatorStartPx,
                        y = constraints.maxHeight - placeable.height
                    )
                }
            }
    )
}