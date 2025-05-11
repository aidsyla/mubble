package com.aidsyla.mubble.feature.explore

import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyGridScope
import androidx.compose.foundation.lazy.grid.items
import com.aidsyla.mubble.feature.explore.model.BubbleFeedItem
import com.aidsyla.mubble.feature.explore.model.FeedItem
import com.aidsyla.mubble.feature.explore.model.ImagePostFeedItem

fun LazyGridScope.explorePostsFeed(
    items: List<FeedItem>,
    onProfileClick: (userId: String) -> Unit,
    onPostClick: (postId: String) -> Unit,
) {
    items(
        items = items,
        key = { it.id },
        span = { item ->
            when (item) {
                is ImagePostFeedItem -> GridItemSpan(1)
                is BubbleFeedItem -> GridItemSpan(2)
            }
        }
    ) { item ->
        when (item) {
            is ImagePostFeedItem -> ExplorePost(
                item = item,
                onPostClick = onPostClick
            )

            is BubbleFeedItem -> ExploreBubble(
                item = item,
            )
        }
    }
}
