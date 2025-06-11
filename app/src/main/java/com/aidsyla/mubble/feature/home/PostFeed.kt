package com.aidsyla.mubble.feature.home

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.items
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.aidsyla.mubble.common.components.post.BasePostLayout
import com.aidsyla.mubble.feature.explore.model.BubbleFeedItem
import com.aidsyla.mubble.feature.explore.model.ImagePostFeedItem

fun LazyListScope.postFeed(
    uiState: PostListUiState,
    onUserClick: (String) -> Unit,
    onMoreClick: (postId: String) -> Unit,
    onPostClick: (postId: String) -> Unit,
    isCircleScreen: Boolean = false
) {
    items(
        items = uiState.items,
        key = { it.id },
        contentType = {
            when(it) {
                is ImagePostFeedItem -> "Image_Post"
                is BubbleFeedItem -> "Bubble_Post"
            }
        }
    ) { item ->
        val modifier = if (isCircleScreen) Modifier.padding(horizontal = 8.dp) else Modifier
        BasePostLayout(
            modifier = modifier,
            item = item,
            useCard = true,
            onUserClick = onUserClick,
            onMoreClick = onMoreClick,
            onPostClick = onPostClick
        )
    }
}