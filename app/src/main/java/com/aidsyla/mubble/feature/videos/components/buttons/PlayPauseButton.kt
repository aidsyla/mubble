package com.aidsyla.mubble.feature.videos.components.buttons

import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.layout.size
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.media3.common.Player
import androidx.media3.common.util.UnstableApi
import androidx.media3.ui.compose.state.rememberPlayPauseButtonState
import com.aidsyla.mubble.ui.theme.MubbleTheme

@androidx.annotation.OptIn(UnstableApi::class)
@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
internal fun PlayPauseButton(modifier: Modifier = Modifier) {
    VideoControlIconToggleButton(
        modifier = modifier,
        checked = false,
        onCheckedChange = {},
    ) {
        Icon(
            modifier =
                Modifier.size(
                    IconButtonDefaults.extraSmallIconSize,
                ),
            painter = MubbleTheme.Icons.Pause,
            contentDescription = null,
            tint = Color.White,
        )
    }
}

@androidx.annotation.OptIn(UnstableApi::class)
@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
internal fun PlayPauseButton(
    modifier: Modifier = Modifier,
    player: Player,
) {
    val playPauseButtonState = rememberPlayPauseButtonState(player)
    VideoControlIconToggleButton(
        modifier = modifier,
        checked = playPauseButtonState.showPlay,
        onCheckedChange = { playPauseButtonState.onClick() },
    ) {
        AnimatedContent(
            targetState = playPauseButtonState.showPlay,
        ) {
            Icon(
                modifier =
                    Modifier.size(
                        IconButtonDefaults.extraSmallIconSize,
                    ),
                painter = if (it) MubbleTheme.Icons.Resume else MubbleTheme.Icons.Pause,
                contentDescription = null,
                tint = Color.White,
            )
        }
    }
}
