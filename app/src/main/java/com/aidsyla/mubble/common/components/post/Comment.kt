package com.aidsyla.mubble.common.components.post

import androidx.annotation.DrawableRes
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
import androidx.compose.foundation.layout.width
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.aidsyla.mubble.common.components.CircleImage
import com.aidsyla.mubble.data.Comment
import com.aidsyla.mubble.data.DummyCommentRepository
import com.aidsyla.mubble.ui.theme.MubbleDesignSystem
import com.aidsyla.mubble.ui.theme.MubbleTheme

@Composable
fun CommentItem(
    modifier: Modifier = Modifier,
    comment: Comment,
    startPadding: Dp = 12.dp,
    onViewRepliesClick: (String) -> Unit
) {
    CommentItem(
        modifier = modifier,
        userAvatarResId = comment.userAvatarResId,
        username = comment.username,
        text = comment.text,
        createdAt = comment.createdAt,
        likeCount = comment.likeCount,
        replyCount = comment.replyCount,
        startPadding = startPadding,
        onViewRepliesClick = { onViewRepliesClick(comment.userId) }
    )
}

@Composable
private fun CommentItem(
    modifier: Modifier = Modifier,
    @DrawableRes userAvatarResId: Int,
    username: String,
    text: String,
    createdAt: String,
    likeCount: Int,
    replyCount: Int = 0,
    startPadding: Dp = 12.dp,
    onViewRepliesClick: () -> Unit
) {
    Column {
        Row(
            modifier =
            modifier
                .padding(start = startPadding, end = 4.dp, top = 8.dp, bottom = 8.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.weight(1f)
            ) {
                CircleImage(
                    painter = painterResource(userAvatarResId),
                    size = 36.dp,
                    borderWidth = 0.2.dp
                )
                Column {
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Text(
                            text = username,
                            style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.Bold)
                        )
                        Text(
                            text = createdAt,
                            style = MaterialTheme.typography.bodySmall
                        )
                    }
                    Text(
                        text = text,
                        style = MaterialTheme.typography.bodyMedium
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(4.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.clickable { }
                    ) {
                        Icon(
                            painter = MubbleDesignSystem.Icons.Reply,
                            modifier = Modifier.size(16.dp),
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        Text(
                            text = "Reply",
                            style = MaterialTheme.typography.labelMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }
            Row(
                modifier =
                Modifier
                    .size(48.dp)
                    .padding()
                    .clickable { },
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(4.dp, Alignment.CenterHorizontally)
            ) {
                Icon(
                    painter = MubbleDesignSystem.Icons.Favorite,
                    modifier = Modifier.size(16.dp),
                    contentDescription = null
                )
                Text(
                    text = likeCount.toString(),
                    style = MaterialTheme.typography.labelMedium
                )
            }
        }
        if (replyCount > 0) {
            Box(
                modifier =
                Modifier
                    .fillMaxWidth()
                    .clickable { onViewRepliesClick() }
                    .padding(vertical = 4.dp),
                contentAlignment = Alignment.Center
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    HorizontalDivider(modifier = modifier.width(36.dp))
                    Text(
                        text = "$replyCount Replies",
                        style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.Bold)
                    )
                    HorizontalDivider(modifier = modifier.width(36.dp))
                }
            }
        }
    }
}

@Preview
@Composable
private fun CommentItemPreview() {
    MubbleTheme {
        Surface {
            CommentItem(
                comment = DummyCommentRepository.allComments.component1(),
                onViewRepliesClick = {}
            )
        }
    }
}
