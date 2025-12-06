package com.aidsyla.mubble.feature.videos.components

import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.unit.dp
import androidx.media3.common.Player
import com.aidsyla.mubble.ui.theme.primaryDark
import kotlinx.coroutines.delay
import kotlin.time.Duration.Companion.seconds

@Composable
internal fun VideoProgressBar(
    player: Player,
    modifier: Modifier = Modifier,
    onScrubbingInfoChange: (isScrubbing: Boolean, currentScrubPositionMs: Long) -> Unit
) {
    var sliderPosition by remember { mutableFloatStateOf(0f) }
    var isPlaying by remember { mutableStateOf(false) }
    var isScrubbing by remember { mutableStateOf(false) }
    var progressBarWidthPx by remember { mutableFloatStateOf(0f) }

    DisposableEffect(player) {
        val listener =
            object : Player.Listener {
                override fun onIsPlayingChanged(isPlayingNew: Boolean) {
                    isPlaying = isPlayingNew
                }

                override fun onPositionDiscontinuity(
                    oldPosition: Player.PositionInfo,
                    newPosition: Player.PositionInfo,
                    reason: Int
                ) {
                    if (!isScrubbing && player.duration > 0) {
                        sliderPosition = player.currentPosition / player.duration.toFloat()
                    }
                }
            }
        player.addListener(listener)

        onDispose {
            player.removeListener(listener)
        }
    }

    if (isPlaying && !isScrubbing) {
        LaunchedEffect(Unit) {
            while (true) {
                val duration = player.duration
                sliderPosition =
                    if (duration > 0) {
                        player.currentPosition / duration.toFloat()
                    } else {
                        0f
                    }
                delay(1.seconds / 30)
            }
        }
    }

    Box(
        modifier =
        modifier
            .fillMaxWidth()
            .height(48.dp)
            .onSizeChanged { size ->
                progressBarWidthPx = size.width.toFloat()
            }.pointerInput(player, progressBarWidthPx) {
                detectDragGestures(
                    onDragStart = { offset ->
                        isScrubbing = true
                        val newSliderPos = (offset.x / progressBarWidthPx).coerceIn(0f, 1f)
                        sliderPosition = newSliderPos
                        val dragPositionMs = (newSliderPos * player.duration).toLong()
                        onScrubbingInfoChange(true, dragPositionMs)
                    },
                    onDragEnd = {
                        val targetPositionMs = (sliderPosition * player.duration).toLong()
                        player.seekTo(targetPositionMs)
                        isScrubbing = false
                        onScrubbingInfoChange(
                            false,
                            targetPositionMs
                        )
                    },
                    onDragCancel = {
                        val targetPositionMs = (sliderPosition * player.duration).toLong()
                        player.seekTo(targetPositionMs)
                        isScrubbing = false
                        onScrubbingInfoChange(
                            false,
                            targetPositionMs
                        )
                    },
                    onDrag = { change, _ ->
                        val newSliderPos = (change.position.x / progressBarWidthPx).coerceIn(0f, 1f)
                        sliderPosition = newSliderPos
                        val dragPositionMs = (newSliderPos * player.duration).toLong()
                        onScrubbingInfoChange(true, dragPositionMs)
                        change.consume()
                    }
                )
            },
        contentAlignment = Alignment.BottomCenter
    ) {
        LinearProgressIndicator(
            progress = { sliderPosition },
            modifier =
            Modifier
                .fillMaxWidth()
                .height(3.dp)
                .padding(horizontal = 12.dp),
            color = primaryDark,
            trackColor = Color.White
        )
    }
}
