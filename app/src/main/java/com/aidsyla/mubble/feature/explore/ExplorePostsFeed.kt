package com.aidsyla.mubble.feature.explore

import androidx.compose.foundation.lazy.staggeredgrid.LazyStaggeredGridScope
import androidx.compose.foundation.lazy.staggeredgrid.items
import com.aidsyla.mubble.common.navigation.shared_elements.PostOrigin
import com.aidsyla.mubble.data.BubbleFeedItem
import com.aidsyla.mubble.data.ImagePostFeedItem

fun LazyStaggeredGridScope.explorePostsFeed(
    items: List<ImagePostFeedItem>,
    onProfileClick: (userId: String) -> Unit,
    onPostClick: (postId: String, origin: PostOrigin) -> Unit,
) {
    items(
        items = items,
    ) { item ->
        ExplorePost(
            item = item,
            onPostClick = onPostClick,
        )
    }
}

fun LazyStaggeredGridScope.exploreBubblesFeed(
    items: List<BubbleFeedItem>,
    onProfileClick: (userId: String) -> Unit,
    onPostClick: (postId: String, origin: PostOrigin) -> Unit,
) {
    items(
        items = items,
    ) { item ->
        ExploreBubble(
            item = item,
            onPostClick = onPostClick,
        )
    }
}
