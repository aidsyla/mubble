package com.aidsyla.mubble.common.components

import androidx.compose.animation.core.tween
import androidx.compose.foundation.pager.PagerState
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.PrimaryTabRow
import androidx.compose.material3.Tab
import androidx.compose.material3.TabIndicatorScope
import androidx.compose.material3.TabPosition
import androidx.compose.material3.TabRowDefaults.PrimaryIndicator
import androidx.compose.material3.TabRowDefaults.SecondaryIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.Measurable
import androidx.compose.ui.unit.Constraints
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.lerp
import com.aidsyla.mubble.ui.theme.MubbleTheme
import kotlinx.coroutines.launch

enum class IndicatorVariant {
    PRIMARY,
    SECONDARY,
}

@Composable
fun Tab(
    modifier: Modifier = Modifier,
    indicatorVariant: IndicatorVariant = IndicatorVariant.SECONDARY,
    pagerState: PagerState,
    tabs: List<String>,
) {
    val coroutineScope = rememberCoroutineScope()
    val currentScreen = pagerState.currentPage

    PrimaryTabRow(
        modifier = modifier,
        selectedTabIndex = currentScreen,
        indicator = {
            AnimatedIndicator(
                indicatorVariant = indicatorVariant,
                pagerState = pagerState,
            )
        },
    ) {
        tabs.forEachIndexed { index, title ->
            Tab(
                selected = currentScreen == index,
                onClick = {
                    coroutineScope.launch {
                        pagerState.animateScrollToPage(
                            page = index,
                            animationSpec = tween(durationMillis = 300),
                        )
                    }
                },
                text = { Text(title) },
                selectedContentColor = MaterialTheme.colorScheme.primary,
                unselectedContentColor = MaterialTheme.colorScheme.onSurfaceVariant,
            )
        }
    }
}

@Composable
fun Tab(
    modifier: Modifier = Modifier,
    indicatorVariant: IndicatorVariant = IndicatorVariant.SECONDARY,
    pagerState: PagerState,
) {
    val coroutineScope = rememberCoroutineScope()
    val currentScreen = pagerState.currentPage

    val selectedIcons = MubbleTheme.ProfileTabs.iconsSelected
    val unselectedIcons = MubbleTheme.ProfileTabs.icons

    PrimaryTabRow(
        modifier = modifier,
        selectedTabIndex = currentScreen,
        indicator = {
            AnimatedIndicator(
                indicatorVariant = indicatorVariant,
                pagerState = pagerState,
            )
        },
        divider = { HorizontalDivider(thickness = 0.5.dp) },
    ) {
        selectedIcons.forEachIndexed { index, icon ->
            Tab(
                selected = currentScreen == index,
                onClick = {
                    coroutineScope.launch {
                        pagerState.animateScrollToPage(
                            page = index,
                            animationSpec = tween(durationMillis = 300),
                        )
                    }
                },
                icon = {
                    Icon(
                        painter =
                            if (currentScreen ==
                                index
                            ) {
                                selectedIcons[index]
                            } else {
                                unselectedIcons[index]
                            },
                        contentDescription = null,
                    )
                },
                selectedContentColor = MaterialTheme.colorScheme.secondary,
                unselectedContentColor = MaterialTheme.colorScheme.onSurfaceVariant,
            )
        }
    }
}

@Composable
fun TabIndicatorScope.AnimatedIndicator(
    indicatorVariant: IndicatorVariant = IndicatorVariant.PRIMARY,
    pagerState: PagerState,
) {
    val currentPage = pagerState.currentPage
    val pageOffset = pagerState.currentPageOffsetFraction
    val indicatorModifier =
        Modifier
            .tabIndicatorLayout { measurable: Measurable, constraints: Constraints, tabPositions: List<TabPosition> ->
                val currentTab = tabPositions.getOrNull(currentPage) ?: tabPositions.first()
                val nextTab = tabPositions.getOrNull(currentPage + 1)

                val indicatorStart: Dp =
                    when {
                        nextTab != null ->
                            lerp(
                                start = currentTab.left,
                                stop = nextTab.left,
                                fraction = pageOffset,
                            )

                        pageOffset < 0f && currentPage > 0 -> {
                            val previousTab = tabPositions[currentPage - 1]
                            lerp(
                                start = previousTab.left,
                                stop = currentTab.left,
                                fraction = pageOffset + 1f,
                            )
                        }

                        else -> currentTab.left
                    }

                val indicatorEnd: Dp =
                    when {
                        nextTab != null ->
                            lerp(
                                start = currentTab.right,
                                stop = nextTab.right,
                                fraction = pageOffset,
                            )

                        pageOffset < 0f && currentPage > 0 -> {
                            val previousTab = tabPositions[currentPage - 1]
                            lerp(
                                start = previousTab.right,
                                stop = currentTab.right,
                                fraction = pageOffset + 1f,
                            )
                        }

                        else -> currentTab.right
                    }

                val indicatorStartPx = indicatorStart.roundToPx()
                val indicatorEndPx = indicatorEnd.roundToPx()
                val indicatorWidth = indicatorEndPx - indicatorStartPx

                val placeable =
                    measurable.measure(
                        constraints.copy(
                            minWidth = indicatorWidth,
                            maxWidth = indicatorWidth,
                        ),
                    )
                layout(constraints.maxWidth, constraints.maxHeight) {
                    placeable.placeRelative(
                        x = indicatorStartPx,
                        y = constraints.maxHeight - placeable.height,
                    )
                }
            }

    when (indicatorVariant) {
        IndicatorVariant.PRIMARY -> {
            val baseWidth = 56.dp // 36.dp
            val maxExtraWidth = 64.dp // 56.dp

            val factor =
                if (pageOffset >= 0f) {
                    (pageOffset / 0.5f).coerceAtMost(1f)
                } else {
                    ((-pageOffset) / 0.5f).coerceAtMost(1f)
                }

            val dynamicWidth = baseWidth + (maxExtraWidth * factor)
            PrimaryIndicator(
//                shape = RoundedCornerShape(topStart = 3.dp, topEnd = 3.dp),
                width = dynamicWidth,
                modifier = indicatorModifier,
                height = 2.dp,
                color = MaterialTheme.colorScheme.secondary,
            )
        }

        IndicatorVariant.SECONDARY -> {
            SecondaryIndicator(
                modifier = indicatorModifier,
                height = 2.dp,
                color = MaterialTheme.colorScheme.secondary,
            )
        }
    }
}
