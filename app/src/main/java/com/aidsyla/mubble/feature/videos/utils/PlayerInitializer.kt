package com.aidsyla.mubble.feature.videos.utils

import android.content.Context
import androidx.media3.common.C
import androidx.media3.common.MediaItem
import androidx.media3.common.Player
import androidx.media3.common.util.UnstableApi
import androidx.media3.exoplayer.DefaultLoadControl
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.exoplayer.upstream.DefaultAllocator

private const val MIN_BUFFER_MS = 15_000
private const val MAX_BUFFER_MS = 15_000
private const val BUFFER_FOR_PLAYBACK_MS = 1_000
private const val BUFFER_FOR_PLAYBACK_AFTER_REBUFFER_MS = 2_000

@androidx.annotation.OptIn(UnstableApi::class)
internal fun initializePlayerForVideo(
    context: Context,
    videoUrl: String
): Player {
    val loadControl =
        DefaultLoadControl
            .Builder()
            .setAllocator(DefaultAllocator(true, C.DEFAULT_BUFFER_SEGMENT_SIZE))
            .setBufferDurationsMs(
                MIN_BUFFER_MS,
                MAX_BUFFER_MS,
                BUFFER_FOR_PLAYBACK_MS,
                BUFFER_FOR_PLAYBACK_AFTER_REBUFFER_MS
            ).setTargetBufferBytes(C.LENGTH_UNSET)
            .setPrioritizeTimeOverSizeThresholds(true)
            .build()
    return ExoPlayer
        .Builder(context)
        .setLoadControl(loadControl)
        .build()
        .apply {
            setMediaItem(MediaItem.fromUri(videoUrl))
            prepare()
        }
}
