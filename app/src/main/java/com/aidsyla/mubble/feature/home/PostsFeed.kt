package com.aidsyla.mubble.feature.home

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.items
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.aidsyla.mubble.common.components.post.BubbleCard
import com.aidsyla.mubble.common.components.post.PostCard
import com.aidsyla.mubble.feature.explore.model.BubbleFeedItem
import com.aidsyla.mubble.feature.explore.model.FeedItem
import com.aidsyla.mubble.feature.explore.model.ImagePostFeedItem

fun LazyListScope.postFeed(
    items: List<FeedItem>,
    onUserClick: (String) -> Unit,
    onMoreClick: (postId: String) -> Unit,
    onPostClick: (postId: String) -> Unit,
) {
    items(
        items = items,
        key = { it.id },
        contentType = { it.variant }
    ) { item ->
        when (item) {
            is ImagePostFeedItem -> PostCard(
                modifier = Modifier.padding(horizontal = 8.dp),
                item = item,
                onUserClick = onUserClick,
                onMoreClick = onMoreClick,
                onPostClick = onPostClick
            )
            is BubbleFeedItem -> BubbleCard(
                modifier = Modifier.padding(horizontal = 8.dp),
                item = item,
                onMoreClick = onMoreClick,
                onUserClick = onUserClick,
                onPostClick = onPostClick
            )
        }
    }
}