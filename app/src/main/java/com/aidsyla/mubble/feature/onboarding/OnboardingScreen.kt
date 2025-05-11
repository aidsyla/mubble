package com.aidsyla.mubble.feature.onboarding

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.Crossfade
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PageSize
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.RoundRect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.times
import com.aidsyla.mubble.R
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun PagerIndicator(
    pagerState: PagerState,
    count: Int,
    modifier: Modifier = Modifier,
    activeLineWidth: Dp = 24.dp,
    dotWidth: Dp = 8.dp,
    dotHeight: Dp = 8.dp,
    circleSpacing: Dp = 4.dp,
    activeIndicatorColor: Color = MaterialTheme.colorScheme.primary,
    inactiveIndicatorColor: Color = MaterialTheme.colorScheme.surfaceVariant,
) {
    val totalWidth = remember(count, activeLineWidth, dotWidth, circleSpacing) {
        (count - 1) * (dotWidth + circleSpacing) + activeLineWidth
    }

//    val animatedColors = List(count) { i ->
//        animateColorAsState(
//            targetValue = if (i == pagerState.settledPage) activeIndicatorColor else inactiveIndicatorColor,
//        ).value
//    }

    Canvas(
        modifier
            .width(totalWidth)
            .wrapContentHeight()
    ) {
        val spacing = circleSpacing.toPx()
        val dotW = dotWidth.toPx()
        val dotH = dotHeight.toPx()
        val activeDotW = activeLineWidth.toPx()

        var x = 0f
        val y = center.y

        repeat(count) { i ->
            val posOffset = pagerState.pageOffset
            val dotOffset = posOffset % 1
            val current = posOffset.toInt()
//            val color = animatedColors[i]

            val color = if (i == pagerState.currentPage) activeIndicatorColor else inactiveIndicatorColor

            val factor =
                if (current == 2 || current == 3) (dotOffset * (activeDotW - dotW) * 1.4f) else (dotOffset * (activeDotW - dotW))

            val calculatedWidth = when {
                i == current -> activeDotW - factor
                i - 1 == current || (i == 0 && posOffset > count - 1) -> dotW + factor
                else -> dotW
            }

            drawIndicator(x, y, calculatedWidth, dotH, CornerRadius(12f, 12f), color)
            x += calculatedWidth + spacing
        }
    }
}

private fun DrawScope.drawIndicator(
    x: Float,
    y: Float,
    width: Float,
    height: Float,
    radius: CornerRadius,
    color: Color,
) {
    val rect = RoundRect(
        left = x,
        top = y - height / 2,
        right = x + width,
        bottom = y + height / 2,
        radiusX = radius.x,
        radiusY = radius.y
    )
    val path = Path().apply { addRoundRect(rect) }
    drawPath(path = path, color = color)
}

val PagerState.pageOffset: Float
    get() = currentPage + currentPageOffsetFraction

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WelcomeScreen(modifier: Modifier = Modifier) {
    val pagerState = rememberPagerState(pageCount = { 4 })
    val customPageSize = object : PageSize {
        override fun Density.calculateMainAxisPageSize(
            availableSpace: Int,
            pageSpacing: Int,
        ): Int {
            return ((availableSpace - 2 * pageSpacing) * 0.8).toInt()
        }
    }

    LaunchedEffect(pagerState) {
        delay(6000L)
        while (true) {
            if (pagerState.currentPage == pagerState.pageCount - 1) break
            if (pagerState.isScrollInProgress) {
                delay(6000L)
                continue
            }
            val nextPage =
                if (pagerState.currentPage == pagerState.pageCount - 1) 0 else pagerState.currentPage + 1

            if (pagerState.currentPage == 2)
                pagerState.animateScrollToPage(
                    nextPage,
                    animationSpec = spring(stiffness = Spring.StiffnessLow)
                )
            else
                pagerState.animateScrollToPage(nextPage)
            delay(6000L)
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {},
                actions = {
                    TextButton(
                        onClick = {}
                    ) {
                        Text("Skip")
                    }
                }
            )
        }
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.padding(it)
        ) {
            HorizontalPager(
                pageSize = customPageSize,
                contentPadding = PaddingValues(horizontal = 16.dp),
                pageSpacing = 8.dp,
                state = pagerState,
                verticalAlignment = Alignment.Top,
                modifier = Modifier
            ) { page ->
                when (page) {
                    0 -> Image(
                        painter = painterResource(R.drawable.ic_launcher_background),
                        contentDescription = null,
                        modifier = Modifier
                            .aspectRatio(9 / 16f)
                            .clip(shape = MaterialTheme.shapes.medium),
                        contentScale = ContentScale.FillHeight
                    )

                    1 -> Image(
                        painter = painterResource(R.drawable.ic_launcher_background),
                        contentDescription = null,
                        modifier = Modifier
                            .aspectRatio(9 / 16f)
                            .clip(shape = MaterialTheme.shapes.medium),
                        contentScale = ContentScale.FillHeight
                    )

                    2 -> Image(
                        painter = painterResource(R.drawable.ic_launcher_background),
                        contentDescription = null,
                        modifier = Modifier
                            .aspectRatio(9 / 16f)
                            .clip(shape = MaterialTheme.shapes.medium),
                        contentScale = ContentScale.FillHeight
                    )

                    3 -> Image(
                        painter = painterResource(R.drawable.ic_launcher_background),
                        contentDescription = null,
                        modifier = Modifier
                            .aspectRatio(9 / 16f)
                            .clip(shape = MaterialTheme.shapes.medium),
                        contentScale = ContentScale.FillHeight
                    )
                }
            }
            Spacer(modifier = Modifier.weight(1f))
            Crossfade(targetState = pagerState.currentPage) { page ->
                Box(modifier = Modifier.wrapContentSize()) {
                    when (page) {
                        0 -> ScreenDescription(
                            title = "Welcome to Mubble",
                            subtitle = "A place to connect, share, and stay in touch."
                        )

                        1 -> ScreenDescription(
                            title = "Share Your Moments",
                            subtitle = "Big moments or quick updates—post it your way."
                        )

                        2 -> ScreenDescription(
                            title = "Join the Conversation",
                            subtitle = "Circles connect you with communities that share your interests."
                        )

                        3 -> ScreenDescription(
                            title = "Stay Connected",
                            subtitle = "Chat, share images, and keep conversations going effortlessly."
                        )
                    }
                }
            }
            Spacer(modifier = Modifier.height(32.dp))
            PagerIndicator(
                pagerState = pagerState,
                count = 4,
            )
            Spacer(modifier = Modifier.weight(1f))
            val coroutineScope = rememberCoroutineScope()
            var buttonText by remember { mutableStateOf("Next") }

            LaunchedEffect(pagerState.currentPage) {
                buttonText = if (pagerState.currentPage == pagerState.pageCount - 1) "Get Started" else "Next"
            }
            AnimatedContent(targetState = buttonText, transitionSpec = {
                fadeIn(animationSpec = tween(300)) togetherWith fadeOut(animationSpec = tween(300))
            }) { text ->
                FilledTonalButton(
                    onClick = {
                        coroutineScope.launch {
                            pagerState.animateScrollToPage(pagerState.currentPage + 1)
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 36.dp)
                        .padding(bottom = 36.dp)
                        .height(56.dp),
                    shape = MaterialTheme.shapes.medium
                ) {
                    Text(text)
                }
            }
        }
    }
}

@Composable
fun ScreenDescription(
    modifier: Modifier = Modifier,
    title: String,
    subtitle: String,
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text(
            title,
            style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
        )
        Text(
            subtitle,
            style = MaterialTheme.typography.bodyLarge,
            textAlign = TextAlign.Center,
            minLines = 2
        )
    }
}