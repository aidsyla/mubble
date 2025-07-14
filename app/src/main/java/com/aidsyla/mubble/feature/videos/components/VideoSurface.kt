package com.aidsyla.mubble.feature.videos.components

import android.util.Log
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.VectorConverter
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.wrapContentSize
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
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.LayoutCoordinates
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.unit.IntSize
import androidx.media3.common.Player
import androidx.media3.common.util.UnstableApi
import androidx.media3.ui.compose.PlayerSurface
import androidx.media3.ui.compose.SURFACE_TYPE_SURFACE_VIEW
import androidx.media3.ui.compose.modifiers.resizeWithContentScale
import androidx.media3.ui.compose.state.rememberPresentationState
import com.aidsyla.mubble.common.components.post.detectTransformGesturesCustom
import com.aidsyla.mubble.ui.theme.surfaceDark
import kotlinx.coroutines.joinAll
import kotlinx.coroutines.launch
import kotlin.math.abs
import kotlin.math.max

@Composable
internal fun VideoSurface(
    modifier: Modifier = Modifier,
    player: Player?,
    isZooming: (Boolean) -> Unit,
) {
    val coroutineScope = rememberCoroutineScope()
    var isAnimatingBack by remember { mutableStateOf(false) }

    var layoutCoordinates by remember { mutableStateOf<LayoutCoordinates?>(null) }

    var initialCenter by remember { mutableStateOf(Offset.Zero) }
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
            isZooming(false)
        }
    }

    Box(
        modifier = Modifier
            .wrapContentSize(),
        contentAlignment = Alignment.Center
    ) {
        player?.let {
            MediaPlayerScreen(
                player = player,
                modifier
                    .fillMaxSize()
                    .onGloballyPositioned { coordinates ->
                        layoutCoordinates = coordinates
                        initialSize = coordinates.size
                        val position = coordinates.localToRoot(Offset.Zero)
                        initialCenter = Offset(
                            x = position.x + initialSize.width / 2f,
                            y = position.y + initialSize.height / 2f
                        )
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
                                    isZooming(true)
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
                    ),
            )
        }
    }
}

@androidx.annotation.OptIn(UnstableApi::class)
@Composable
private fun MediaPlayerScreen(player: Player, modifier: Modifier = Modifier) {
    var currentContentScale by remember { mutableStateOf(ContentScale.FillWidth) }

    Box(
        modifier = modifier
            .fillMaxSize()
    ) {
        val presentationState = rememberPresentationState(player)
        val videoSizeDp = presentationState.videoSizeDp

        LaunchedEffect(videoSizeDp) {
            if (videoSizeDp != null) {
                val videoWidth = videoSizeDp.width
                val videoHeight = videoSizeDp.height
                val videoAspectRatio = videoWidth / videoHeight

                currentContentScale = when {
                    videoAspectRatio <= 9f / 16f + 0.01f -> {
                        ContentScale.Crop
                    }

                    else -> {
                        ContentScale.Fit
                    }
                }

                val aspectRatio = when {
                    abs(videoAspectRatio - (16f / 9f)) < 0.01f -> "16:9 (Landscape)"
                    abs(videoAspectRatio - (4f / 3f)) < 0.01f -> "4:3 (Standard)"
                    abs(videoAspectRatio - (1f / 1f)) < 0.01f -> "1:1 (Square)"
                    abs(videoAspectRatio - (9f / 16f)) < 0.01f -> "9:16 (Portrait)"
                    videoAspectRatio < 9f / 16f -> "Taller than 9:16 (Portrait)"
                    videoAspectRatio > (16f / 9f) -> "Wider than 16:9 (Ultra-wide Landscape)"
                    videoAspectRatio > (4f / 3f) -> "Between 4:3 and 16:9 (Wide Landscape)"
                    videoAspectRatio > (1f / 1f) -> "Between 1:1 and 4:3 (Slightly Wide Landscape)"
                    else -> "Custom/Unknown $videoAspectRatio"
                }

                Log.d(
                    "VIDEO_SIZE",
                    "Aspect Ratio: $aspectRatio, Applied Scale: $currentContentScale"
                )
            } else {
                currentContentScale = ContentScale.FillWidth
            }
        }

        val scaledModifier =
            Modifier.resizeWithContentScale(currentContentScale, presentationState.videoSizeDp)

        PlayerSurface(
            player = player,
            surfaceType = SURFACE_TYPE_SURFACE_VIEW,
            modifier = scaledModifier
        )

        if (presentationState.coverSurface) {
            Box(Modifier.matchParentSize().background(color = surfaceDark))
        }
    }
}