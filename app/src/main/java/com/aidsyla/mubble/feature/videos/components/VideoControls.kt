package com.aidsyla.mubble.feature.videos.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.media3.common.Player
import androidx.media3.common.util.UnstableApi
import com.aidsyla.mubble.feature.videos.components.buttons.PlayPauseButton
import com.aidsyla.mubble.feature.videos.components.buttons.PlaybackSpeedButton
import com.aidsyla.mubble.feature.videos.components.buttons.VideoControlIconButton
import com.aidsyla.mubble.feature.videos.components.buttons.VolumeButton
import com.aidsyla.mubble.ui.theme.MubbleDesignSystem

@androidx.annotation.OptIn(UnstableApi::class)
@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
internal fun VideoControls(
    modifier: Modifier = Modifier,
    player: Player?,
    onBackClick: () -> Unit
) {
    Row(
        modifier =
        modifier
            .fillMaxWidth()
            .padding(horizontal = 12.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        VideoControlIconButton(
            modifier = Modifier,
            onClick = onBackClick
        ) {
            Icon(
                modifier =
                Modifier.size(
                    IconButtonDefaults.extraSmallIconSize
                ),
                painter = MubbleDesignSystem.Icons.ArrowBack,
                contentDescription = null,
                tint = Color.White
            )
        }
        Row {
            if (player == null) {
                VolumeButton()
                PlayPauseButton()
                PlaybackSpeedButton()
            } else {
                VolumeButton(player = player)
                PlayPauseButton(player = player)
                PlaybackSpeedButton(player = player)
            }
        }
        Box(
            modifier =
            Modifier.size(
                IconButtonDefaults.extraSmallContainerSize(
                    IconButtonDefaults.IconButtonWidthOption.Uniform
                )
            )
        )
    }
}
