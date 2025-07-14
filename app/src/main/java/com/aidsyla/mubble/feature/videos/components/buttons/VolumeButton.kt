package com.aidsyla.mubble.feature.videos.components.buttons

import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.layout.size
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.media3.common.Player
import androidx.media3.common.listen
import com.aidsyla.mubble.ui.theme.MubbleTheme

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
internal fun VolumeButton(modifier: Modifier = Modifier, player: Player) {
    val volumeButtonState = rememberVolumeButtonState(player)
    VideoControlIconToggleButton(
        modifier = modifier,
        checked = volumeButtonState.isMuted,
        onCheckedChange = { volumeButtonState.onClick() },
    ) {
        AnimatedContent(
            targetState = volumeButtonState.isMuted
        ) {
            Icon(
                modifier = Modifier.size(
                    IconButtonDefaults.extraSmallIconSize
                ),
                painter = if (it) MubbleTheme.Icons.VolumeOff else MubbleTheme.Icons.VolumeUp,
                contentDescription = null,
                tint = Color.White
            )
        }
    }
}

@Composable
fun rememberVolumeButtonState(player: Player): VolumeButtonState {
    val volumeButtonState = remember(player) { VolumeButtonState(player) }
    LaunchedEffect(player) {
        volumeButtonState.observe()
    }
    return volumeButtonState
}

class VolumeButtonState(private val player: Player) {
    private var volumeBeforeMute by mutableFloatStateOf(player.volume)

    private var isEnabled by mutableStateOf(player.isCommandAvailable(Player.COMMAND_SET_VOLUME))

    var isMuted by mutableStateOf(player.volume == 0f)
        private set

    fun onClick() {
        if (!isEnabled) return

        if (isMuted) {
            player.volume = if (volumeBeforeMute > 0) volumeBeforeMute else 1f
        } else {
            volumeBeforeMute = player.volume
            player.volume = 0f
        }
    }

    suspend fun observe(): Nothing =
        player.listen { events ->
            if (events.contains(Player.EVENT_AVAILABLE_COMMANDS_CHANGED)) {
                isEnabled = player.isCommandAvailable(Player.COMMAND_SET_VOLUME)
            }

            if (events.contains(Player.EVENT_VOLUME_CHANGED)) {
                val newVolume = player.volume
                isMuted = newVolume == 0f
                if (!isMuted) {
                    volumeBeforeMute = newVolume
                }
            }
        }
}