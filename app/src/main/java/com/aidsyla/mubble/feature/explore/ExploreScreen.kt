package com.aidsyla.mubble.feature.explore

import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyGridState
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.aidsyla.mubble.common.components.layout.MubbleGridTabPager
import com.aidsyla.mubble.common.components.layout.MubbleListTabPager
import com.aidsyla.mubble.common.components.layout.MubbleTabRow
import com.aidsyla.mubble.feature.explore.model.BubbleFeedItem
import com.aidsyla.mubble.feature.explore.model.FeedItem
import com.aidsyla.mubble.feature.explore.model.ImagePostFeedItem
import com.aidsyla.mubble.ui.LocalBottomBarPadding
import com.aidsyla.mubble.ui.theme.MubbleTheme

@Composable
fun ExploreScreen(
    viewModel: ExploreViewModel = hiltViewModel(),
    onPostClick: (postId: String) -> Unit,
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val tabTitles = remember { listOf("Posts", "Circles") }
    val pagerState = rememberPagerState { tabTitles.size }
    val bottomPadding = LocalBottomBarPadding.current

    MubbleGridTabPager(
        modifier = Modifier.padding(bottom = bottomPadding),
        pagerState = pagerState,
        firstPage = {
            ExplorePostGrid(
                items = uiState.items,
                listState = it,
                onPostClick = onPostClick
            )
        },
        secondPage = {
            ExplorePostGrid(
                items = uiState.items,
                listState = it,
                onPostClick = onPostClick
            )
        },
        actions = {
            IconButton(onClick = {}) {
                Icon(painter = MubbleTheme.Icons.Search, contentDescription = null)
            }
        }
    ) {
        MubbleTabRow(
            tabTitles = tabTitles,
            pagerState = pagerState
        )
    }
}

@Composable
fun ExplorePostGrid(
    modifier: Modifier = Modifier,
    items: List<FeedItem>,
    listState: LazyGridState,
    onPostClick: (postId: String) -> Unit,
) {
    LazyVerticalGrid(
        modifier = modifier,
        state = listState,
        columns = GridCells.Fixed(2),
        contentPadding = PaddingValues(start = 8.dp, end = 8.dp, top = 4.dp, bottom = 16.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        explorePostsFeed(
            items = items,
            onProfileClick = {},
            onPostClick = onPostClick
        )
    }
}

@Composable
fun ExplorePost(
    modifier: Modifier = Modifier,
    item: ImagePostFeedItem,
    onPostClick: (postId: String) -> Unit,
) {
    Card(
        modifier = modifier.aspectRatio(1f),
        onClick = { onPostClick(item.id) }
    ) {
        ExploreHeader(
            avatarResId = item.userAvatarResId,
            username = item.username
        )
        Image(
            painter = painterResource(item.postImageResId),
            contentDescription = "Post media",
            modifier = modifier.fillMaxWidth(),
            contentScale = ContentScale.Crop
        )
    }
}

@Composable
fun ExploreHeader(
    modifier: Modifier = Modifier,
    username: String,
    @DrawableRes avatarResId: Int,
) {
    Row(
        modifier = modifier.padding(8.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Image(
            painter = painterResource(avatarResId),
            contentDescription = null,
            modifier = Modifier
                .size(24.dp)
                .clip(shape = CircleShape),
            contentScale = ContentScale.Crop
        )
        Text(
            text = username, style = MaterialTheme.typography.labelMedium.copy(
                fontWeight = FontWeight.W900
            )
        )
    }
}

@Composable
fun ExploreBubble(
    modifier: Modifier = Modifier,
    item: BubbleFeedItem,
) {
    ExploreBubble(
        modifier = modifier,
        displayName = item.displayName,
        description = item.postDescription,
        likeCount = item.likeCount,
        avatarResId = item.userAvatarResId
    )
}

@Composable
fun ExploreBubble(
    modifier: Modifier = Modifier,
    displayName: String,
    description: String,
    likeCount: Int,
    @DrawableRes avatarResId: Int,
) {
    Card(
        modifier = modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier.padding(start = 12.dp, end = 8.dp, top = 16.dp, bottom = 16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Image(
                painter = painterResource(avatarResId),
                contentDescription = null,
                modifier = Modifier
                    .size(40.dp)
                    .clip(shape = CircleShape),
                contentScale = ContentScale.Crop
            )
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = displayName, style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.SemiBold
                    )
                )
                Text(
                    text = description,
                    style = MaterialTheme.typography.bodyMedium
                )
            }
            Column(
                modifier = Modifier
                    .width(48.dp)
                    .clickable { },
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.FavoriteBorder,
                    contentDescription = null,
                )
                Text(text = likeCount.toString(), style = MaterialTheme.typography.labelSmall)
            }
        }
    }
}

@Preview
@Composable
private fun ExplorePostPreview() {
//    ExplorePost()
}

@Preview
@Composable
private fun ExploreBubblePreview() {
//    ExploreBubble()
}

