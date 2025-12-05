package com.aidsyla.mubble.feature.videos.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandIn
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkOut
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.aidsyla.mubble.common.components.SubtleHorizontalDivider
import com.aidsyla.mubble.common.components.layout.rememberIsAtTop
import com.aidsyla.mubble.common.components.post.CommentItem
import com.aidsyla.mubble.data.DummyCommentRepository
import com.aidsyla.mubble.feature.postdetails.CommentBottomBar
import com.aidsyla.mubble.ui.theme.MubbleTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CommentsBottomSheet(
    openBottomSheet: Boolean,
    onOpenChange: (Boolean) -> Unit,
) {
    val bottomSheetState =
        rememberModalBottomSheetState(skipPartiallyExpanded = true)

    val commentsForPost = DummyCommentRepository.getCommentsForPost("user_1")

    var commentIdToFetchReplies: String by remember { mutableStateOf("") }
    val replies =
        DummyCommentRepository.getRepliesForComment("user_1", commentIdToFetchReplies)

    var areRepliesOpen by remember { mutableStateOf(false) }

    val listState = rememberLazyListState()
    val isAtTop = listState.rememberIsAtTop()

    if (openBottomSheet) {
        ModalBottomSheet(
            dragHandle = {
                Box(
                    modifier =
                        Modifier
                            .fillMaxWidth(),
                ) {
                    Column(
                        modifier =
                            Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 16.dp, vertical = 8.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement =
                            Arrangement.spacedBy(
                                4.dp,
                                Alignment.CenterVertically,
                            ),
                    ) {
                        Surface(
                            modifier =
                                Modifier.padding(vertical = 8.dp),
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            shape = MaterialTheme.shapes.extraLarge,
                        ) {
                            Box(Modifier.size(width = 32.dp, height = 4.dp))
                        }
                        Box(
                            contentAlignment = Alignment.Center,
                        ) {
                            Text(
                                text = "13 Comments",
                                style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.SemiBold),
                            )
                        }
                    }
                    Icon(
                        modifier =
                            Modifier
                                .padding(end = 16.dp)
                                .size(20.dp)
                                .align(Alignment.CenterEnd),
                        painter = MubbleTheme.Icons.Close,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                    AnimatedVisibility(
                        modifier = Modifier.align(Alignment.BottomCenter),
                        enter = fadeIn(animationSpec = tween(durationMillis = 300)),
                        exit = fadeOut(animationSpec = tween(durationMillis = 300)),
                        visible = !isAtTop,
                    ) {
                        SubtleHorizontalDivider()
                    }
                }
            },
            sheetGesturesEnabled = false,
            onDismissRequest = { onOpenChange(false) },
            sheetState = bottomSheetState,
        ) {
            Box(modifier = Modifier.wrapContentSize()) {
                LazyColumn(
                    modifier =
                        Modifier
                            .fillMaxHeight(0.75f)
                            .padding(bottom = 64.dp),
                    state = listState,
                ) {
                    items(commentsForPost) { comment ->
                        CommentItem(
                            comment = comment,
                            onViewRepliesClick = { commentId ->
                                areRepliesOpen = !areRepliesOpen
                                commentIdToFetchReplies = commentId
                            },
                        )
                        with(this@ModalBottomSheet) {
                            AnimatedVisibility(
                                visible = comment.userId == commentIdToFetchReplies && areRepliesOpen,
                                enter =
                                    fadeIn(animationSpec = tween(durationMillis = 300)) +
                                        expandIn(
                                            expandFrom = Alignment.TopStart,
                                            clip = false,
                                        ),
                                exit =
                                    shrinkOut(
                                        animationSpec = tween(durationMillis = 300),
                                        shrinkTowards = Alignment.TopStart,
                                        clip = false,
                                    ) + fadeOut(),
                            ) {
                                Column {
                                    replies.forEach { replies ->
                                        CommentItem(
                                            comment = replies,
                                            startPadding = 44.dp,
                                            onViewRepliesClick = {},
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
                CommentBottomBar(modifier = Modifier.align(Alignment.BottomCenter))
            }
        }
    }
}
