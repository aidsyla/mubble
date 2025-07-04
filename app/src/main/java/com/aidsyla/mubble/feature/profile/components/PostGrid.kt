package com.aidsyla.mubble.feature.profile.components

import androidx.compose.foundation.lazy.grid.LazyGridScope
import androidx.compose.foundation.lazy.grid.items
import com.aidsyla.mubble.common.navigation.shared_elements.PostOrigin
import com.aidsyla.mubble.feature.explore.model.ImagePostFeedItem

fun LazyGridScope.postGrid(
    items: List<ImagePostFeedItem>,
    onPostClick: (postId: String, origin: PostOrigin) -> Unit,
) {
    items(items = items, key = { it.id }) {
        ProfilePost(
            item = it,
            onPostClick = { postId -> onPostClick(postId, PostOrigin.ProfileMedia) }
        )
    }
}