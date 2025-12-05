package com.aidsyla.mubble.feature.home

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.aidsyla.mubble.common.components.post.BasePostLayout
import com.aidsyla.mubble.common.navigation.shared_elements.PostOrigin
import com.aidsyla.mubble.data.BubbleFeedItem
import com.aidsyla.mubble.data.ImagePostFeedItem

fun LazyListScope.postFeed(
    uiState: PostListUiState,
    origin: PostOrigin,
    isCircleScreen: Boolean = false,
    onUserClick: (String) -> Unit,
    onMoreClick: (postId: String) -> Unit,
    onPostClick: (index: Int, postId: String, origin: PostOrigin) -> Unit,
) {
    itemsIndexed(
        items = uiState.items,
        key = { _, item -> item.id },
        contentType = { _, item ->
            when (item) {
                is ImagePostFeedItem -> "Image_Post"
                is BubbleFeedItem -> "Bubble_Post"
            }
        },
    ) { index, item ->
        val modifier = if (isCircleScreen) Modifier.padding(horizontal = 8.dp) else Modifier
        BasePostLayout(
            modifier = modifier,
            origin = origin,
            item = item,
            useCard = true,
            onUserClick = onUserClick,
            onMoreClick = onMoreClick,
            onPostClick = { onPostClick(index, it, origin) },
        )
    }
}
