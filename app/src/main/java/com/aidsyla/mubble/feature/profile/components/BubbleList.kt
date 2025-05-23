package com.aidsyla.mubble.feature.profile.components

import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.items
import com.aidsyla.mubble.feature.explore.ExploreBubble
import com.aidsyla.mubble.feature.explore.model.BubbleFeedItem

fun LazyListScope.bubbleList(
    items: List<BubbleFeedItem>,
) {
    items(
        items = items,
        key = { it.id },
    ) { item ->
        ExploreBubble(item = item)
    }
}