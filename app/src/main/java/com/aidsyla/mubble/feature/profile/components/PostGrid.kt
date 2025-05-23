package com.aidsyla.mubble.feature.profile.components

import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyGridScope
import androidx.compose.foundation.lazy.grid.items
import com.aidsyla.mubble.feature.explore.model.ImagePostFeedItem

fun LazyGridScope.postGrid(
    items: List<ImagePostFeedItem>,
) {
    items(
        items = items,
        key = { it.id },
        span = {
            GridItemSpan(1)
        }
    ) { item ->
        ProfilePost(imageResId = item.postImageResId)
    }
}