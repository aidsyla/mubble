package com.aidsyla.mubble.common.components.post

import androidx.annotation.DrawableRes
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.EaseInOutCubic
import androidx.compose.animation.core.EaseOutQuad
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LocalRippleConfiguration
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.aidsyla.mubble.R
import com.aidsyla.mubble.feature.explore.model.BubbleFeedItem
import com.aidsyla.mubble.feature.explore.model.FeedItem
import com.aidsyla.mubble.feature.explore.model.ImagePostFeedItem
import com.aidsyla.mubble.feature.home.data.DummyPostRepository
import com.aidsyla.mubble.ui.theme.MubbleTheme

@Composable
fun BasePostLayout(
    modifier: Modifier = Modifier,
    item: FeedItem,
    useCard: Boolean = true,
    onUserClick: (String) -> Unit,
    onMoreClick: (postId: String) -> Unit,
    onPostClick: (postId: String) -> Unit,
) {
    val detailsPadding = if (!useCard) Modifier.padding(top = 4.dp) else Modifier
    var isLiked by remember { mutableStateOf(false) }
    val content: @Composable () -> Unit = {

        if (useCard) {
            PostHeader(
                name = item.displayName,
                avatarResId = item.userAvatarResId,
                circleName = item.circleName,
                datePosted = item.datePosted,
                onUserClick = { onUserClick(item.id) },
                onMoreClick = { onMoreClick(item.id) }
            )
        }

        when (item) {
            is ImagePostFeedItem -> {
                item.postDescription?.let {
                    PostDescription(
                        modifier = detailsPadding
                            .padding(bottom = 8.dp),
                        description = it,
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
                PostMedia(
                    imageResId = item.postImageResId,
                )
                PostActions(
                    likeCount = item.likeCount,
                    commentCount = item.commentCount,
                    shareCount = item.shareCount,
                    isLiked = isLiked,
                    onFilledChange = { isLiked = it }
                )
            }

            is BubbleFeedItem -> {
                PostDescription(
                    modifier = detailsPadding,
                    description = item.postDescription,
                    style = MaterialTheme.typography.bodyLarge
                )
                PostActions(
                    likeCount = item.likeCount,
                    commentCount = item.commentCount,
                    shareCount = item.shareCount,
                    isLiked = isLiked,
                    onFilledChange = { isLiked = it }
                )
            }
        }
    }

    if (useCard)
        Card(
            modifier = modifier
                .clip(shape = MaterialTheme.shapes.large)
                .combinedClickable(
                onClick = { onPostClick(item.id) },
                onDoubleClick = { isLiked = !isLiked }
            ),
            shape = MaterialTheme.shapes.large,
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surfaceContainer
            )
        ) {
            content()
        }
    else
        Column(modifier = modifier) {
            content()
        }
}

@Composable
fun PostHeader(
    modifier: Modifier = Modifier,
    name: String,
    @DrawableRes avatarResId: Int,
    datePosted: String,
    circleName: String?,
    onUserClick: () -> Unit,
    onMoreClick: () -> Unit,
) {
    Row(
        modifier = modifier
            .padding(start = 16.dp, end = 4.dp)
            .padding(vertical = 8.dp)
            .fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier
                .clip(shape = MaterialTheme.shapes.extraLarge)
                .clickable { onUserClick() }
                .padding(end = 8.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .border(1.dp, MaterialTheme.colorScheme.outlineVariant, CircleShape)
                    .clip(CircleShape)
            ) {
                Image(
                    painter = painterResource(avatarResId),
                    contentDescription = "$name's avatar",
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop
                )
            }
            Column {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(6.dp),
                ) {
                    Text(
                        modifier = Modifier.alignByBaseline(),
                        text = name,
                        style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.SemiBold),
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Text(
                        modifier = Modifier.alignByBaseline(),
                        text = datePosted,
                        style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Light),
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
                circleName?.let {
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            painter = MubbleTheme.Icons.InCircle,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.onSurface
                        )
                        Box(
                            modifier = Modifier
                                .size(24.dp)
                                .border(
                                    0.5.dp,
                                    MaterialTheme.colorScheme.outlineVariant,
                                    CircleShape
                                )
                                .clip(CircleShape)
                        ) {
                            Image(
                                modifier = Modifier.fillMaxSize(),
                                contentScale = ContentScale.Crop,
                                painter = painterResource(R.drawable.post_3),
                                contentDescription = null
                            )
                        }
                        Text(
                            text = it,
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    }
                }
            }
        }
        Spacer(Modifier.weight(1f))
        IconButton(onClick = onMoreClick) {
            Icon(imageVector = Icons.Default.MoreVert, contentDescription = "More options")
        }
    }
}

@Composable
fun PostDescription(
    modifier: Modifier = Modifier,
    style: TextStyle,
    description: String,
) {
    Text(
        modifier = modifier
            .padding(horizontal = 16.dp),
        text = description,
        style = style,
        color = MaterialTheme.colorScheme.onSurface
    )
}

@Composable
fun PostMedia(
    modifier: Modifier = Modifier,
    @DrawableRes imageResId: Int,
) {
    Image(
        painter = painterResource(imageResId),
        contentDescription = "Post media",
        modifier = modifier
            .fillMaxWidth(),
        contentScale = ContentScale.FillWidth
    )
}

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun BouncingHeartIcon(
    isFilled: Boolean,
    onFilledChange: (Boolean) -> Unit,
    count: Int,
) {
    val sizeAnim = remember { Animatable(0f) }

    LaunchedEffect(isFilled) {
        if (isFilled) {
            sizeAnim.snapTo(0f)
            sizeAnim.animateTo(
                targetValue = 40f,
                animationSpec = tween(durationMillis = 100, easing = EaseOutQuad)
            )
            sizeAnim.animateTo(
                targetValue = 26f,
                animationSpec = tween(durationMillis = 150, easing = EaseInOutCubic)
            )
        } else {
            sizeAnim.animateTo(0f, animationSpec = tween(200))
        }
    }
    CompositionLocalProvider(value = LocalRippleConfiguration provides null) {
        Row(
            modifier = Modifier
                .height(48.dp)
                .clickable { onFilledChange(!isFilled) },
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Box(
                modifier = Modifier.size(40.dp),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    modifier = Modifier.size(26.dp),
                    painter = MubbleTheme.Icons.Favorite,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onSurface
                )
                if (sizeAnim.value > 0f) {
                    Icon(
                        modifier = Modifier.size(sizeAnim.value.dp),
                        painter = MubbleTheme.Icons.FavoriteFilled,
                        contentDescription = null,
                        tint = Color.Red
                    )
                }
            }
            if (count > 0) {
                Text(count.toString(), style = MaterialTheme.typography.labelMediumEmphasized)
            }
        }
    }
}

@Composable
fun PostActions(
    modifier: Modifier = Modifier,
    isLiked: Boolean,
    onFilledChange: (Boolean) -> Unit,
    likeCount: Int,
    commentCount: Int,
    shareCount: Int,
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(start = 8.dp, end = 16.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        BouncingHeartIcon(
            count = likeCount,
            isFilled = isLiked,
            onFilledChange = onFilledChange
        )
        ActionItem(
            painter = MubbleTheme.Icons.Comment,
            count = commentCount
        )
        Spacer(modifier = Modifier.weight(1f))
        ActionItem(painter = MubbleTheme.Icons.Send, count = shareCount)
    }
}

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun ActionItem(
    modifier: Modifier = Modifier,
    painter: Painter,
    count: Int,
) {
    Row(
        modifier = modifier
            .height(48.dp)
            .wrapContentWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(7.dp)
    ) {
        Icon(modifier = Modifier.size(26.dp), painter = painter, contentDescription = null)
        if (count > 0) {
            Text(count.toString(), style = MaterialTheme.typography.labelMediumEmphasized)
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
        BasePostLayout(
            modifier = Modifier.padding(8.dp),
            item = item,
            useCard = true,
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
            BasePostLayout(
                item = item,
                useCard = false,
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
        BasePostLayout(
            modifier = Modifier.padding(8.dp),
            item = item,
            useCard = true,
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
            BasePostLayout(
                item = item,
                useCard = false,
                onUserClick = {},
                onMoreClick = {},
                onPostClick = {}
            )
        }
    }
}