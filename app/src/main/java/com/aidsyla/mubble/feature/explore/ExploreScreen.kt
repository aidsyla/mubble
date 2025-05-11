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
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.aidsyla.mubble.common.components.layout.TabbedPager
import com.aidsyla.mubble.feature.explore.model.BubbleFeedItem
import com.aidsyla.mubble.feature.explore.model.FeedItem
import com.aidsyla.mubble.feature.explore.model.ImagePostFeedItem
import com.aidsyla.mubble.feature.profile.ProfileBubbleList
import com.aidsyla.mubble.ui.LocalBottomBarPadding

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ExploreScreen(
    viewModel: ExploreViewModel = hiltViewModel(),
    onPostClick: (postId: String) -> Unit,
) {
    val scrollState = TopAppBarDefaults.exitUntilCollapsedScrollBehavior()
    val uiState = viewModel.uiState.collectAsStateWithLifecycle()
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Explore") },
                actions = {
                    IconButton(onClick = {}) {
                        Icon(
                            imageVector = Icons.Filled.Search,
                            contentDescription = "Localized description"
                        )
                    }
                },
                scrollBehavior = scrollState
            )
        }
    ) {
        val bottomPadding = LocalBottomBarPadding.current
        TabbedPagerExplore(
            modifier = Modifier
                .padding(top = it.calculateTopPadding(), bottom = bottomPadding)
                .nestedScroll(scrollState.nestedScrollConnection),
            items = uiState.value.items,
            onPostClick = onPostClick,
        )
    }
}

@Composable
fun TabbedPagerExplore(
    modifier: Modifier = Modifier,
    items: List<FeedItem>,
    onPostClick: (postId: String) -> Unit,
) {
    val titles = listOf("Posts", "Circles")
    val tabContent: List<@Composable () -> Unit> = listOf(
        {
            ExplorePostGrid(
                items = items,
                onPostClick = onPostClick
            )
        },
        { ProfileBubbleList() },
    )
    TabbedPager(modifier = modifier, titles = titles, tabContent = tabContent)
}

@Composable
fun ExplorePostGrid(
    modifier: Modifier = Modifier,
    items: List<FeedItem>,
    onPostClick: (postId: String) -> Unit,
) {
    LazyVerticalGrid(
        modifier = modifier,
        columns = GridCells.Fixed(2),
        contentPadding = PaddingValues(8.dp),
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

