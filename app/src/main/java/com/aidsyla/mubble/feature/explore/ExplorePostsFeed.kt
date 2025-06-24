package com.aidsyla.mubble.feature.explore

import androidx.compose.foundation.lazy.staggeredgrid.LazyStaggeredGridScope
import androidx.compose.foundation.lazy.staggeredgrid.items
import com.aidsyla.mubble.feature.explore.model.BubbleFeedItem
import com.aidsyla.mubble.feature.explore.model.ImagePostFeedItem

fun LazyStaggeredGridScope.explorePostsFeed(
    items: List<ImagePostFeedItem>,
    onProfileClick: (userId: String) -> Unit,
    onPostClick: (postId: String) -> Unit,
) {
    items(
        items = items,
    ) { item ->
        ExplorePost(
            item = item,
            onPostClick = onPostClick
        )
    }
}

fun LazyStaggeredGridScope.exploreBubblesFeed(
    items: List<BubbleFeedItem>,
    onProfileClick: (userId: String) -> Unit,
    onPostClick: (postId: String) -> Unit,
) {
    items(
        items = items,
    ) { item ->
        ExploreBubble(
            item = item
        )
    }
}
