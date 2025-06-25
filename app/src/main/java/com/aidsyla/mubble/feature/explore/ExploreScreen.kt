package com.aidsyla.mubble.feature.explore

import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.staggeredgrid.LazyStaggeredGridState
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.aidsyla.mubble.common.components.CircleImage
import com.aidsyla.mubble.common.components.circle.CircleItem
import com.aidsyla.mubble.common.components.layout.MubbleGridTabPager
import com.aidsyla.mubble.common.components.layout.MubbleTabRow
import com.aidsyla.mubble.feature.circle.model.Circle
import com.aidsyla.mubble.feature.circle.model.CircleRepo
import com.aidsyla.mubble.feature.explore.model.BubbleFeedItem
import com.aidsyla.mubble.feature.explore.model.ImagePostFeedItem
import com.aidsyla.mubble.feature.home.data.DummyPostRepository
import com.aidsyla.mubble.ui.LocalBottomBarPadding
import com.aidsyla.mubble.ui.theme.MubbleTheme

@Composable
fun ExploreScreen(
    viewModel: ExploreViewModel = hiltViewModel(),
    onPostClick: (postId: String) -> Unit,
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val tabTitles = remember { listOf("Circles", "Media", "Bubbles") }
    val pagerState = rememberPagerState(initialPage = 1) { tabTitles.size }
    val bottomPadding = LocalBottomBarPadding.current

    MubbleGridTabPager(
        modifier = Modifier.padding(bottom = bottomPadding),
        pagerState = pagerState,
        firstPage = {
            ExploreCircleGrid(
                items = CircleRepo.dummyCircles
            )
        },
        secondPage = {
            ExplorePostGrid(
                items = uiState.media,
                state = it,
                onPostClick = onPostClick
            )
        },
        thirdPage = {
            ExploreBubbleGrid(
                items = uiState.bubbles,
                state = it,
                onPostClick = onPostClick
            )
        },
        navigationIcon = {
            IconButton(onClick = {}, enabled = false) {
                Icon(
                    painter = MubbleTheme.Icons.MubbleIcon, contentDescription = null,
                    tint = Color.Unspecified
                )
            }
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
fun ExploreCircleGrid(
    modifier: Modifier = Modifier,
    items: List<Circle>,
) {
    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        contentPadding = PaddingValues(top = 4.dp, start = 8.dp, end = 8.dp, bottom = 8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        items(items) {
            CircleItem(circle = it, showIcon = true, modifier = Modifier.fillMaxWidth())
        }
    }
}

@Composable
fun ExploreBubbleGrid(
    modifier: Modifier = Modifier,
    items: List<BubbleFeedItem>,
    state: LazyStaggeredGridState,
    onPostClick: (postId: String) -> Unit,
) {
    LazyVerticalStaggeredGrid(
        state = state,
        columns = StaggeredGridCells.Fixed(2),
        contentPadding = PaddingValues(top = 4.dp, start = 8.dp, end = 8.dp, bottom = 8.dp),
        verticalItemSpacing = 8.dp,
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        modifier = Modifier.fillMaxSize()
    ) {
        exploreBubblesFeed(
            items = items,
            onProfileClick = {},
            onPostClick = onPostClick
        )
    }
}

@Composable
fun ExplorePostGrid(
    modifier: Modifier = Modifier,
    items: List<ImagePostFeedItem>,
    state: LazyStaggeredGridState,
    onPostClick: (postId: String) -> Unit,
) {
    LazyVerticalStaggeredGrid(
        state = state,
        columns = StaggeredGridCells.Fixed(2),
        contentPadding = PaddingValues(top = 4.dp, start = 8.dp, end = 8.dp, bottom = 8.dp),
        verticalItemSpacing = 8.dp,
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        modifier = Modifier.fillMaxSize()
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
    Box(
        modifier = modifier
            .clip(MaterialTheme.shapes.medium)
            .clickable { onPostClick(item.id) }
    ) {
        Image(
            painter = painterResource(item.postImageResId),
            contentDescription = "Post media",
            modifier = Modifier
                .fillMaxWidth(),
        )
        Box(
            modifier = Modifier
                .matchParentSize()
                .background(MubbleTheme.Gradients.fadingBlackGradient)
        )
        ExploreHeader(
            modifier = Modifier
                .align(Alignment.TopStart),
            avatarResId = item.userAvatarResId,
            username = item.username
        )
    }
}

@Composable
fun ExploreHeader(
    modifier: Modifier = Modifier,
    username: String,
    textColor: Color = Color.White,
    @DrawableRes avatarResId: Int,
) {
    Row(
        modifier = modifier.padding(8.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        CircleImage(
            painter = painterResource(avatarResId),
            size = 24.dp
        )
        Text(
            text = username, style = MaterialTheme.typography.labelMedium.copy(
                fontWeight = FontWeight.Bold
            ),
            color = textColor
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
        username = item.username,
        description = item.postDescription,
        avatarResId = item.userAvatarResId
    )
}

@Composable
fun ExploreBubble(
    modifier: Modifier = Modifier,
    username: String,
    description: String,
    @DrawableRes avatarResId: Int,
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        shape = MaterialTheme.shapes.medium
    ) {
        ExploreHeader(
            avatarResId = avatarResId,
            username = username,
            textColor = MaterialTheme.colorScheme.onSurface
        )
        Text(
            text = description,
            style = MaterialTheme.typography.bodySmall,
            modifier = Modifier
                .padding(horizontal = 8.dp)
                .padding(bottom = 8.dp)
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun ExplorePostPreview() {
    val item = DummyPostRepository.dummyFeedItems
        .filterIsInstance<ImagePostFeedItem>()
        .first()
    MubbleTheme {
        ExplorePost(
            item = item,
            onPostClick = {}
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun ExploreBubblePreview() {
    val item = DummyPostRepository.dummyFeedItems
        .filterIsInstance<BubbleFeedItem>()
        .first()
    MubbleTheme {
        Surface {
            ExploreBubble(
                item = item
            )
        }
    }
}