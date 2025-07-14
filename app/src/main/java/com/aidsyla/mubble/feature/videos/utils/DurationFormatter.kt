package com.aidsyla.mubble.feature.videos.utils

import java.util.Locale

internal fun formatMsToMinutesSeconds(ms: Long): String {
    val totalSeconds = ms / 1000
    val minutes = totalSeconds / 60
    val seconds = totalSeconds % 60
    return String.format(Locale.ROOT, "%02d:%02d", minutes, seconds)
}