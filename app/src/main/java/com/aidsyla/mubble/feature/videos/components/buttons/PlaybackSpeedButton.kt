package com.aidsyla.mubble.feature.videos.components.buttons

import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.layout.size
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.media3.common.Player
import androidx.media3.common.util.UnstableApi
import androidx.media3.ui.compose.state.rememberPlaybackSpeedState
import com.aidsyla.mubble.ui.theme.MubbleTheme

private enum class PlaybackSpeed(
    val multiplier: Float,
) {
    HALF(0.5f),
    NORMAL(1.0f),
    DOUBLE(2.0f),
    ;

    fun next(): PlaybackSpeed =
        when (this) {
            HALF -> NORMAL
            NORMAL -> DOUBLE
            DOUBLE -> HALF
        }
}

@androidx.annotation.OptIn(UnstableApi::class)
@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
internal fun PlaybackSpeedButton(
    modifier: Modifier = Modifier,
    player: Player,
) {
    var speed by remember { mutableStateOf(PlaybackSpeed.NORMAL) }
    val playbackSpeedState = rememberPlaybackSpeedState(player)

    LaunchedEffect(speed) {
        playbackSpeedState.updatePlaybackSpeed(speed.multiplier)
    }

    VideoControlIconButton(
        modifier = modifier,
        onClick = { speed = speed.next() },
    ) {
        AnimatedContent(
            targetState = speed,
        ) {
            Icon(
                modifier =
                    Modifier.size(
                        IconButtonDefaults.extraSmallIconSize,
                    ),
                painter =
                    when (it) {
                        PlaybackSpeed.HALF -> MubbleTheme.Icons.Speed0_5x
                        PlaybackSpeed.NORMAL -> MubbleTheme.Icons.Speed1x
                        PlaybackSpeed.DOUBLE -> MubbleTheme.Icons.Speed2x
                    },
                contentDescription = null,
                tint = Color.White,
            )
        }
    }
}

@androidx.annotation.OptIn(UnstableApi::class)
@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
internal fun PlaybackSpeedButton(modifier: Modifier = Modifier) {
    VideoControlIconButton(
        modifier = modifier,
        onClick = {},
    ) {
        Icon(
            modifier =
                Modifier.size(
                    IconButtonDefaults.extraSmallIconSize,
                ),
            painter = MubbleTheme.Icons.Speed1x,
            contentDescription = null,
            tint = Color.White,
        )
    }
}
