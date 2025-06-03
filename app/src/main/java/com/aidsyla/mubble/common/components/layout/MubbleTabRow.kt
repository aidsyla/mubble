package com.aidsyla.mubble.common.components.layout

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateIntAsState
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.PagerState
import androidx.compose.material3.LocalRippleConfiguration
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.TabRowDefaults.PrimaryIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.Stable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.layout.positionInParent
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.lerp
import kotlinx.coroutines.launch

/**
 * Custom tab row with indicator that moves based on [PagerState.currentPageOffsetFraction] and
 * animates its width dynamically.
 */
@Composable
fun MubbleTabRow(
    tabTitles: List<String>,
    pagerState: PagerState,
    modifier: Modifier = Modifier,
) {
    val state = rememberAnimatedIndicatorTabState(pagerState = pagerState, totalTabs = tabTitles.size)
    val coroutineScope = rememberCoroutineScope()
    val density = LocalDensity.current
    Column(
        modifier = modifier
            .heightIn(min = 48.dp)
            .padding(horizontal = 8.dp),
        horizontalAlignment = Alignment.Start,
        verticalArrangement = Arrangement.Center
    ) {
        Row(
            modifier = Modifier,
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            tabTitles.forEachIndexed { index, title ->
                CompositionLocalProvider(value = LocalRippleConfiguration.provides(value = null)) {
                    Box(
                        modifier = Modifier
                            .padding(horizontal = 0.dp, vertical = 12.dp)
                            .onGloballyPositioned {
                                val leftDp = with(density) { it.positionInParent().x.toDp() }
                                val widthDp = with(density) { it.size.width.toDp() }
                                state.onTabMeasured(index, leftDp, widthDp)
                            }
                            .clickable {
                                coroutineScope.launch {
                                    pagerState.animateScrollToPage(index)
                                }
                            }
                    ) {
                        val isCurrentPage = pagerState.currentPage == index
                        val color by animateColorAsState(
                            targetValue = if (isCurrentPage) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        val weight by animateIntAsState(
                            targetValue = if (isCurrentPage) FontWeight.SemiBold.weight else FontWeight.Medium.weight
                        )
                        Text(
                            text = title,
                            color = color,
                            fontWeight = FontWeight(weight),
                            style = MaterialTheme.typography.titleSmall,
                            modifier = Modifier.align(Alignment.Center)
                        )
                    }
                }
            }
        }
        PrimaryIndicator(
            modifier = Modifier
                .offset { IntOffset(x = state.indicatorStartPx, y = 0) },
            width = with(density) { state.indicatorWidthPx.toDp() }
        )
    }
}

@Stable
private class MubbleTabRowState(
    private val pagerState: PagerState,
    private val density: Density,
    private val totalTabs: Int,
) {
    private val tabLefts = mutableStateListOf<Dp>()
    private val tabWidths = mutableStateListOf<Dp>()

    fun onTabMeasured(index: Int, left: Dp, width: Dp) {
        while (tabLefts.size <= index) tabLefts.add(0.dp)
        while (tabWidths.size <= index) tabWidths.add(0.dp)
        tabLefts[index] = left
        tabWidths[index] = width
    }

    private val currentIndex by derivedStateOf {
        if (totalTabs > 0) {
            pagerState.currentPage.coerceIn(0 until totalTabs)
        } else {
            0
        }
    }

    private val pageOffset by derivedStateOf { pagerState.currentPageOffsetFraction }

    private val currentLeftDp by derivedStateOf { tabLefts.getOrElse(currentIndex) { 0.dp } }
    private val currentWidthDp by derivedStateOf { tabWidths.getOrElse(currentIndex) { 0.dp } }
    private val currentCenter by derivedStateOf { currentLeftDp + (currentWidthDp / 2f) }

    private val nextLeftDp by derivedStateOf { tabLefts.getOrElse(currentIndex + 1) { currentLeftDp } }
    private val nextWidthDp by derivedStateOf { tabWidths.getOrElse(currentIndex + 1) { currentWidthDp } }
    private val nextCenterDp by derivedStateOf { nextLeftDp + (nextWidthDp / 2f) }

    private val prevLeftDp by derivedStateOf { tabLefts.getOrElse(currentIndex - 1) { currentLeftDp } }
    private val prevWidthDp by derivedStateOf { tabWidths.getOrElse(currentIndex - 1) { currentWidthDp } }
    private val prevCenterDp by derivedStateOf { prevLeftDp + (prevWidthDp / 2f) }

    private val fWidth by derivedStateOf {
        if (pageOffset >= 0f) (pageOffset / 0.5f).coerceIn(0f, 1f)
        else ((-pageOffset) / 0.5f).coerceIn(0f, 1f)
    }

    private val dynamicWidthDp: Dp by derivedStateOf {
//        val baseWidth = 36.dp
//        val maxExtraWidth = 46.dp
//        baseWidth + (maxExtraWidth * fWidth)
        currentWidthDp + (36.dp * fWidth)
    }

    private val indicatorCenterDp: Dp by derivedStateOf {
        when {
            nextCenterDp != currentCenter && pageOffset >= 0f -> {
                lerp(
                    start = currentCenter,
                    stop = nextCenterDp,
                    fraction = pageOffset.coerceIn(0f, 1f)
                )
            }

            prevCenterDp != currentCenter && pageOffset < 0f -> {
                val backwardFraction = (pageOffset + 1f).coerceIn(0f, 1f)
                lerp(
                    start = prevCenterDp,
                    stop = currentCenter,
                    fraction = backwardFraction
                )
            }

            else -> currentCenter
        }
    }

    private val indicatorStartDp by derivedStateOf { indicatorCenterDp - (dynamicWidthDp / 2f) }

    val indicatorStartPx: Int by derivedStateOf {
        with(density) { indicatorStartDp.roundToPx() }
    }

    val indicatorWidthPx: Int by derivedStateOf {
        with(density) { dynamicWidthDp.roundToPx() }
    }
}

@Composable
private fun rememberAnimatedIndicatorTabState(
    pagerState: PagerState,
    totalTabs: Int,
): MubbleTabRowState {
    val density = LocalDensity.current
    return remember(pagerState, totalTabs, density) {
        MubbleTabRowState(pagerState, density, totalTabs)
    }
}
