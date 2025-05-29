package com.aidsyla.mubble.common.components.post

import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Send
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.aidsyla.mubble.R
import com.aidsyla.mubble.feature.explore.model.BubbleFeedItem
import com.aidsyla.mubble.feature.explore.model.FeedItem
import com.aidsyla.mubble.feature.explore.model.ImagePostFeedItem
import com.aidsyla.mubble.feature.home.data.DummyPostRepository
import com.aidsyla.mubble.ui.theme.MubbleTheme

@Composable
private fun BasePostLayout(
    modifier: Modifier = Modifier,
    item: FeedItem,
    useCard: Boolean,
    onUserClick: (String) -> Unit,
    onMoreClick: (postId: String) -> Unit,
    onPostClick: (postId: String) -> Unit,
) {
    val content: @Composable () -> Unit = {
        item.circleName?.let { CircleHeader(it) }

        if (useCard) {
            PostHeader(
                name = item.displayName,
                avatarResId = item.userAvatarResId,
                onUserClick = { onUserClick(item.id) },
                onMoreClick = { onMoreClick(item.id) }
            )
        }

        when (item) {
            is ImagePostFeedItem -> {
                Box(contentAlignment = Alignment.BottomCenter) {
                    PostMedia(imageResId = item.postImageResId)
                    PostActions(
                        shape = RoundedCornerShape(topStart = 12.dp, topEnd = 12.dp),
                        likeCount = item.likeCount,
                        commentCount = item.commentCount,
                        shareCount = item.shareCount
                    )
                }
                PostDetails(
                    topPadding = 12.dp,
                    textStyle = MaterialTheme.typography.bodySmall,
                    description = item.postDescription,
                    username = item.username,
                    datePosted = item.datePosted
                )
            }

            is BubbleFeedItem -> {
                PostDetails(
                    topPadding = 0.dp,
                    textStyle = MaterialTheme.typography.bodyLarge,
                    description = item.postDescription,
                    username = item.username,
                    datePosted = item.datePosted
                )
                PostActions(
                    shape = if (useCard) RectangleShape else RoundedCornerShape(
                        bottomStart = 12.dp,
                        bottomEnd = 12.dp
                    ),
                    likeCount = item.likeCount,
                    commentCount = item.commentCount,
                    shareCount = item.shareCount
                )
            }
        }
    }

    if (useCard)
        OutlinedCard(
            onClick = { onPostClick(item.id) },
            modifier = modifier,
        ) {
            content()
        }
    else
        Column(modifier = modifier) {
            content()
        }
}

@Composable
fun PostCard(
    modifier: Modifier = Modifier,
    item: ImagePostFeedItem,
    onUserClick: (String) -> Unit,
    onMoreClick: (postId: String) -> Unit,
    onPostClick: (postId: String) -> Unit,
) {
    BasePostLayout(
        modifier = modifier,
        item = item,
        useCard = true,
        onUserClick = onUserClick,
        onMoreClick = onMoreClick,
        onPostClick = onPostClick
    )
}

@Composable
fun PostDetails(
    modifier: Modifier = Modifier,
    item: ImagePostFeedItem,
    onUserClick: (String) -> Unit,
    onMoreClick: (postId: String) -> Unit,
    onPostClick: (postId: String) -> Unit,
) {
    BasePostLayout(
        modifier = modifier,
        item = item,
        useCard = false,
        onUserClick = onUserClick,
        onMoreClick = onMoreClick,
        onPostClick = onPostClick
    )
}

@Composable
fun BubbleCard(
    modifier: Modifier = Modifier,
    item: BubbleFeedItem,
    onUserClick: (String) -> Unit,
    onMoreClick: (postId: String) -> Unit,
    onPostClick: (postId: String) -> Unit,
) {
    BasePostLayout(
        modifier = modifier,
        item = item,
        useCard = true,
        onUserClick = onUserClick,
        onMoreClick = onMoreClick,
        onPostClick = onPostClick
    )
}

@Composable
fun BubbleDetails(
    modifier: Modifier = Modifier,
    item: BubbleFeedItem,
    onUserClick: (String) -> Unit,
    onMoreClick: (postId: String) -> Unit,
    onPostClick: (postId: String) -> Unit,
) {
    BasePostLayout(
        modifier = modifier,
        item = item,
        useCard = false,
        onUserClick = onUserClick,
        onMoreClick = onMoreClick,
        onPostClick = onPostClick
    )
}

@Composable
fun CircleHeader(
    circleName: String,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier
            .background(color = MaterialTheme.colorScheme.surfaceContainer)
            .fillMaxWidth()
            .clickable { }
            .padding(vertical = 8.dp),
        horizontalArrangement = Arrangement.spacedBy(4.dp, Alignment.CenterHorizontally),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Image(
            modifier = Modifier
                .size(24.dp)
                .clip(shape = CircleShape),
            painter = painterResource(R.drawable.ic_launcher_background),
            contentDescription = null
        )
        Text(text = circleName)
    }
}

@Composable
fun PostHeader(
    modifier: Modifier = Modifier,
    name: String,
    @DrawableRes avatarResId: Int,
    onUserClick: () -> Unit,
    onMoreClick: () -> Unit,
) {
    Row(
        modifier = modifier
            .padding(
                start = 16.dp, top = 12.dp, bottom = 12.dp, end = 4.dp
            )
            .fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier
                .clip(shape = MaterialTheme.shapes.extraLarge)
                .clickable { onUserClick() }
                .padding(end = 8.dp)
        ) {
            Image(
                painter = painterResource(avatarResId),
                contentDescription = "$name's avatar",
                modifier = Modifier
                    .size(48.dp)
                    .clip(shape = CircleShape),
                contentScale = ContentScale.Crop
            )
            Text(
                text = name,
                style = MaterialTheme.typography.titleMedium.copy(
                    fontWeight = FontWeight.Medium
                )
            )
        }
        Spacer(Modifier.weight(1f))
        IconButton(onClick = onMoreClick) {
            Icon(imageVector = Icons.Default.MoreVert, contentDescription = "More options")
        }
    }
}

@Composable
fun PostMedia(
    modifier: Modifier = Modifier,
    @DrawableRes imageResId: Int,
) {
    Image(
        painter = painterResource(imageResId),
        contentDescription = "Post media",
        modifier = modifier.fillMaxWidth(),
        contentScale = ContentScale.FillWidth
    )
}

@Composable
fun PostActions(
    modifier: Modifier = Modifier,
    shape: Shape,
    likeCount: Int,
    commentCount: Int,
    shareCount: Int,
) {
    Card(
        modifier = modifier,
        shape = shape, colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.secondaryContainer
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(36.dp)
                .padding(start = 12.dp, end = 12.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            ActionItem(imageVector = Icons.Default.FavoriteBorder, count = likeCount)
            ActionItem(
                imageVector = Icons.Default.Add,
                count = commentCount
            )
            Spacer(modifier = Modifier.weight(1f))
            ActionItem(imageVector = Icons.Default.Send, count = shareCount)
        }
    }
}

@Composable
fun ActionItem(
    modifier: Modifier = Modifier,
    imageVector: ImageVector,
    count: Int,
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        Icon(imageVector = imageVector, contentDescription = null)
        if (count > 0) {
            Text(count.toString(), style = MaterialTheme.typography.labelSmall)
        }
    }
}

@Composable
fun PostDetails(
    modifier: Modifier = Modifier,
    topPadding: Dp,
    textStyle: TextStyle,
    description: String,
    username: String,
    datePosted: String,
) {
    Column(
        modifier = modifier.padding(start = 16.dp, end = 16.dp, top = topPadding, bottom = 12.dp),
        verticalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        Text(
            text = description,
            style = textStyle
        )
        Row(modifier = Modifier.fillMaxWidth()) {
            Text(
                text = username,
                style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Medium),
                maxLines = 1
            )
            Spacer(modifier = Modifier.weight(1f))
            Text(
                text = datePosted,
                style = MaterialTheme.typography.labelSmall
            )
        }
    }
}

@Preview(showBackground = true, name = "Post Card Preview")
@Composable
private fun PostCardPreview() {
    val item = DummyPostRepository.dummyFeedItems
        .filterIsInstance<ImagePostFeedItem>()
        .first()
    MubbleTheme {
        PostCard(
            modifier = Modifier.padding(8.dp),
            item = item,
            onUserClick = {},
            onMoreClick = {},
            onPostClick = {}
        )
    }
}

@Preview(showBackground = true, name = "Post Details Preview")
@Composable
private fun PostDetailsPreview() {
    val item = DummyPostRepository.dummyFeedItems
        .filterIsInstance<ImagePostFeedItem>()
        .first()
    MubbleTheme {
        Surface {
            PostDetails(
                item = item,
                onUserClick = {},
                onMoreClick = {},
                onPostClick = {}
            )
        }
    }
}

@Preview(showBackground = true, name = "Bubble Card Preview")
@Composable
private fun BubbleCardPreview() {
    val item = DummyPostRepository.dummyFeedItems
        .filterIsInstance<BubbleFeedItem>()
        .first()

    MubbleTheme {
        BubbleCard(
            modifier = Modifier.padding(8.dp),
            item = item,
            onUserClick = {},
            onMoreClick = {},
            onPostClick = {}
        )
    }
}

@Preview(showBackground = true, name = "Bubble Details Preview")
@Composable
private fun BubbleDetailsPreview() {
    val item = DummyPostRepository.dummyFeedItems
        .filterIsInstance<BubbleFeedItem>()
        .first()

    MubbleTheme {
        Surface {
            BubbleDetails(
                item = item,
                onUserClick = {},
                onMoreClick = {},
                onPostClick = {}
            )
        }
    }
}