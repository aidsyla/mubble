package com.aidsyla.mubble.feature.profile.components

import androidx.annotation.DrawableRes
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.VectorConverter
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import com.aidsyla.mubble.app.LocalDarkTheme
import com.aidsyla.mubble.common.components.post.detectTransformGesturesCustom
import com.aidsyla.mubble.common.navigation.LocalNavAnimatedVisibilityScope
import com.aidsyla.mubble.common.navigation.LocalSharedTransitionScope
import com.aidsyla.mubble.ui.theme.MubbleTheme
import kotlinx.coroutines.joinAll
import kotlinx.coroutines.launch
import kotlin.math.max

data class FullScreenMediaSharedElementKey(
    val imgId: Int,
    val fullScreenMediaType: FullScreenMediaType,
)

enum class FullScreenMediaType {
    AVATAR,
    BANNER
}

@OptIn(ExperimentalSharedTransitionApi::class, ExperimentalMaterial3Api::class)
@Composable
fun FullScreenMediaViewer(
    modifier: Modifier = Modifier,
    type: FullScreenMediaType,
    @DrawableRes imageId: Int,
    onBackClick: () -> Unit,
) {
    val sharedTransitionScope = LocalSharedTransitionScope.current
        ?: throw IllegalStateException("No SharedElementScope found")
    val animatedContentScope = LocalNavAnimatedVisibilityScope.current
        ?: throw IllegalStateException("No AnimatedVisibility found")
    val isDarkMode = LocalDarkTheme.current

    val imageModifier = when (type) {
        FullScreenMediaType.AVATAR -> Modifier
            .fillMaxWidth()
            .aspectRatio(1f)
            .clickable {}

        FullScreenMediaType.BANNER -> Modifier
            .fillMaxWidth()
            .aspectRatio(2.25f)
            .clickable {}
    }

    val contentScale = when (type) {
        FullScreenMediaType.AVATAR -> ContentScale.FillWidth
        FullScreenMediaType.BANNER -> ContentScale.FillWidth
    }

    val backgroundColor = if (isDarkMode) Color.Black else Color.White

    with(sharedTransitionScope) {
        Box(
            modifier = modifier
                .clickable {
                    onBackClick()
                }
                .fillMaxSize()
                .background(color = backgroundColor),
            contentAlignment = Alignment.Center
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .statusBarsPadding()
                    .height(TopAppBarDefaults.TopAppBarExpandedHeight)
                    .padding(start = 4.dp)
                    .align(Alignment.TopStart),
                contentAlignment = Alignment.CenterStart
            ) {
                IconButton(
                    onClick = onBackClick
                ) {
                    Icon(
                        painter = MubbleTheme.Icons.Close,
                        contentDescription = "Close"
                    )
                }
            }
            ZoomableMedia(
                modifier = imageModifier
                    .sharedElement(
                        rememberSharedContentState(
                            key = FullScreenMediaSharedElementKey(
                                imgId = imageId,
                                fullScreenMediaType = type
                            )
                        ),
                        animatedVisibilityScope = animatedContentScope
                    ),
                imageId = imageId,
                contentScale = contentScale,
                type = type
            )
        }
    }
}

@Composable
fun ZoomableMedia(
    modifier: Modifier = Modifier,
    @DrawableRes imageId: Int,
    contentScale: ContentScale,
    type: FullScreenMediaType,
) {
    val coroutineScope = rememberCoroutineScope()
    var isAnimatingBack by remember { mutableStateOf(false) }

    var initialSize by remember { mutableStateOf(IntSize.Zero) }

    var zoom by remember { mutableFloatStateOf(1f) }
    var offset by remember { mutableStateOf(Offset.Zero) }
    var centerOffset by remember { mutableStateOf(Offset.Zero) }

    val zoomAnimatable = remember { Animatable(1f) }
    val offsetAnimatable = remember { Animatable(Offset.Zero, Offset.VectorConverter) }
    val centerOffsetAnimatable = remember { Animatable(Offset.Zero, Offset.VectorConverter) }

    centerOffset =
        if (initialSize == IntSize.Zero) Offset.Zero else {
            Offset(
                x = (initialSize.width / 2f) * (zoom - 1) - offset.x * zoom,
                y = (initialSize.height / 2f) * (zoom - 1) - offset.y * zoom
            )
        }

    LaunchedEffect(zoom) {
        if (!zoomAnimatable.isRunning) {
            zoomAnimatable.snapTo(zoom)
        }
    }
    LaunchedEffect(offset) {
        if (!offsetAnimatable.isRunning) {
            offsetAnimatable.snapTo(offset)
        }
    }
    LaunchedEffect(centerOffset) {
        if (!centerOffsetAnimatable.isRunning) {
            centerOffsetAnimatable.snapTo(centerOffset)
        }
    }

    fun resetToCenter() {
        coroutineScope.launch {
            isAnimatingBack = true
            val zoomJob = launch {
                zoomAnimatable.snapTo(zoom)
                zoomAnimatable.animateTo(
                    targetValue = 1f,
                    animationSpec = tween(
                        durationMillis = 300,
                        easing = FastOutSlowInEasing
                    )
                )
                zoom = 1f
            }

            val offsetJob = launch {
                centerOffsetAnimatable.snapTo(centerOffset)
                centerOffsetAnimatable.animateTo(
                    targetValue = Offset.Zero,
                    animationSpec = tween(
                        durationMillis = 300,
                        easing = FastOutSlowInEasing
                    )
                )
                centerOffset = Offset.Zero
            }
            joinAll(zoomJob, offsetJob)
            offset = Offset.Zero
            isAnimatingBack = false
        }
    }

    Image(
        painter = painterResource(imageId),
        contentDescription = null,
        modifier
            .fillMaxSize()
            .onGloballyPositioned { coordinates ->
                initialSize = coordinates.size
            }
            .then(
                if (isAnimatingBack)
                    Modifier else
                    Modifier.pointerInput(Unit) {
                        detectTransformGesturesCustom(
                            onGestureEnd = {
                                resetToCenter()
                            }
                        ) { centroid, pan, gestureZoom ->
                            val oldZoom = zoom
                            val newZoom = max(zoom * gestureZoom, 0.7f)

                            offset = (offset + centroid / oldZoom) -
                                    (centroid / newZoom + pan / oldZoom)
                            zoom = newZoom
                        }
                    }
            )
            .graphicsLayer(
                scaleX = zoomAnimatable.value,
                scaleY = zoomAnimatable.value,
                translationX = centerOffsetAnimatable.value.x,
                translationY = centerOffsetAnimatable.value.y,
            )
            .then(
                if (type == FullScreenMediaType.AVATAR) Modifier.clip(shape = CircleShape) else Modifier
            ),
        contentScale = contentScale
    )
}