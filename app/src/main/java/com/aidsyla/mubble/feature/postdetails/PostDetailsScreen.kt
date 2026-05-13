package com.aidsyla.mubble.feature.postdetails

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.EnterExitState
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.animation.core.animateDp
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandIn
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkOut
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.aidsyla.mubble.R
import com.aidsyla.mubble.common.components.CircleImage
import com.aidsyla.mubble.common.components.SubtleHorizontalDivider
import com.aidsyla.mubble.common.components.layout.rememberIsAtTop
import com.aidsyla.mubble.common.components.post.BasePostLayout
import com.aidsyla.mubble.common.components.post.CommentItem
import com.aidsyla.mubble.common.navigation.LocalNavAnimatedVisibilityScope
import com.aidsyla.mubble.common.navigation.LocalSharedTransitionScope
import com.aidsyla.mubble.common.navigation.sharedelements.PostOrigin
import com.aidsyla.mubble.common.navigation.sharedelements.PostSharedElementKey
import com.aidsyla.mubble.common.navigation.sharedelements.PostSharedElementType
import com.aidsyla.mubble.data.DummyCommentRepository
import com.aidsyla.mubble.ui.theme.MubbleDesignSystem
import com.aidsyla.mubble.ui.theme.MubbleTheme

@OptIn(ExperimentalMaterial3Api::class, ExperimentalSharedTransitionApi::class)
@Composable
fun PostDetailsScreen(
    origin: PostOrigin,
    onUserClick: (String) -> Unit,
    onBackClick: () -> Unit,
    viewModel: PostDetailsViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    val sharedTransitionScope =
        LocalSharedTransitionScope.current
            ?: throw IllegalStateException("No SharedElementScope found")
    val animatedContentScope =
        LocalNavAnimatedVisibilityScope.current
            ?: throw IllegalStateException("No AnimatedVisibility found")

    val roundedCornerAnimation by animatedContentScope.transition.animateDp {
        when (it) {
            EnterExitState.PreEnter -> 0.dp
            EnterExitState.Visible -> 12.dp
            EnterExitState.PostExit -> 12.dp
        }
    }

    val lazyListState = rememberLazyListState()
    val isAtTop = lazyListState.rememberIsAtTop()

    with(sharedTransitionScope) {
        when (val state = uiState) {
            PostDetailsUiState.Loading -> {
            }

            is PostDetailsUiState.Success -> {
                Scaffold(
                    modifier =
                    Modifier
                        .sharedBounds(
                            rememberSharedContentState(
                                key =
                                PostSharedElementKey(
                                    postId = state.postItem.id,
                                    origin = origin,
                                    type = PostSharedElementType.Bounds
                                )
                            ),
                            resizeMode = SharedTransitionScope.ResizeMode.scaleToBounds(contentScale = ContentScale.Crop),
                            clipInOverlayDuringTransition =
                            OverlayClip(
                                RoundedCornerShape(
                                    roundedCornerAnimation
                                )
                            ),
                            enter = EnterTransition.None,
                            exit = ExitTransition.None,
                            animatedVisibilityScope = animatedContentScope
                        ),
                    topBar = {
                        Box {
                            TopAppBar(
                                title = {
                                    Row(
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                                        modifier =
                                        Modifier
                                            .clip(shape = MaterialTheme.shapes.extraLarge)
                                            .clickable {
                                                onUserClick(state.postItem.id)
                                            }.padding(end = 8.dp)
                                    ) {
                                        with(sharedTransitionScope) {
                                            CircleImage(
                                                modifier =
                                                Modifier.sharedElement(
                                                    rememberSharedContentState(
                                                        key =
                                                        PostSharedElementKey(
                                                            postId = state.postItem.id,
                                                            origin = origin,
                                                            type = PostSharedElementType.ProfileAvatar
                                                        )
                                                    ),
                                                    animatedVisibilityScope = animatedContentScope
                                                ),
                                                painter = painterResource(state.postItem.userAvatarResId),
                                                borderWidth = 0.1.dp,
                                                contentDescription = "avatar"
                                            )
                                        }
                                        Column {
                                            Row(
                                                horizontalArrangement = Arrangement.spacedBy(6.dp)
                                            ) {
                                                Text(
                                                    modifier =
                                                    Modifier
                                                        .alignByBaseline()
                                                        .sharedBounds(
                                                            rememberSharedContentState(
                                                                PostSharedElementKey(
                                                                    postId = state.postItem.id,
                                                                    origin = origin,
                                                                    type = PostSharedElementType.DisplayName
                                                                )
                                                            ),
                                                            animatedVisibilityScope = animatedContentScope
                                                        ),
                                                    text = state.postItem.displayName,
                                                    style =
                                                    MaterialTheme.typography.bodyMedium.copy(
                                                        fontWeight = FontWeight.SemiBold
                                                    ),
                                                    color = MaterialTheme.colorScheme.onSurface
                                                )
                                                Text(
                                                    modifier = Modifier.alignByBaseline(),
                                                    text = state.postItem.datePosted,
                                                    style = MaterialTheme.typography.bodyMedium,
                                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                                )
                                            }
                                            state.postItem.circleName?.let {
                                                Row(
                                                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                                                    verticalAlignment = Alignment.CenterVertically
                                                ) {
                                                    Icon(
                                                        painter = MubbleDesignSystem.Icons.InCircle,
                                                        contentDescription = null,
                                                        tint = MaterialTheme.colorScheme.onSurface
                                                    )
                                                    CircleImage(
                                                        painter = painterResource(R.drawable.post_3),
                                                        size = 24.dp,
                                                        borderWidth = 0.5.dp
                                                    )
                                                    Text(
                                                        text = it,
                                                        style = MaterialTheme.typography.bodyMedium,
                                                        color = MaterialTheme.colorScheme.onSurface
                                                    )
                                                }
                                            }
                                        }
                                    }
                                },
                                navigationIcon = {
                                    IconButton(onClick = onBackClick) {
                                        Icon(
                                            painter = MubbleDesignSystem.Icons.ArrowBack,
                                            contentDescription = "Back"
                                        )
                                    }
                                },
                                actions = {
                                    IconButton(onClick = { }) {
                                        Icon(
                                            painter = MubbleDesignSystem.Icons.MoreHorizontal,
                                            contentDescription = "More"
                                        )
                                    }
                                }
                            )
                            if (!isAtTop) {
                                SubtleHorizontalDivider(modifier = Modifier.align(Alignment.BottomCenter))
                            }
                        }
                    },
                    bottomBar = {
                        CommentBottomBar()
                    }
                ) {
                    val postId = state.postItem.id
                    val commentsForPost = DummyCommentRepository.getCommentsForPost(postId)

                    var commentIdToFetchReplies: String by remember { mutableStateOf("") }
                    val replies =
                        DummyCommentRepository.getRepliesForComment(postId, commentIdToFetchReplies)

                    var areRepliesOpen by remember { mutableStateOf(false) }

                    LazyColumn(
                        modifier =
                        Modifier
                            .fillMaxSize()
                            .padding(it),
                        state = lazyListState
                    ) {
                        item {
                            BasePostLayout(
                                modifier = Modifier,
                                item = state.postItem,
                                origin = origin,
                                useCard = false,
                                isInPostDetails = true,
                                onUserClick = { },
                                onMoreClick = viewModel::onMoreClick,
                                onPostClick = { }
                            )
                        }
                        item {
                            Text(
                                modifier = Modifier.padding(horizontal = 16.dp),
                                text = "Comments",
                                style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.SemiBold)
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                        }
                        items(commentsForPost) { comment ->
                            CommentItem(
                                comment = comment,
                                onViewRepliesClick = { commentId ->
                                    areRepliesOpen = !areRepliesOpen
                                    commentIdToFetchReplies = commentId
                                }
                            )
                            AnimatedVisibility(
                                visible = comment.userId == commentIdToFetchReplies && areRepliesOpen,
                                enter =
                                fadeIn(animationSpec = tween(durationMillis = 300)) +
                                    expandIn(
                                        expandFrom = Alignment.TopStart,
                                        clip = false
                                    ),
                                exit =
                                shrinkOut(
                                    animationSpec = tween(durationMillis = 300),
                                    shrinkTowards = Alignment.TopStart,
                                    clip = false
                                ) + fadeOut()
                            ) {
                                Column {
                                    replies.forEach { replies ->
                                        CommentItem(
                                            comment = replies,
                                            startPadding = 44.dp,
                                            onViewRepliesClick = {}
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun CommentBottomBar(
    modifier: Modifier = Modifier,
    color: Color = MaterialTheme.colorScheme.surfaceContainerLow
) {
    Box(modifier = modifier) {
        Surface(
            color = color
        ) {
            Row(
                modifier =
                Modifier
                    .padding(horizontal = 16.dp, vertical = 12.dp)
                    .navigationBarsPadding(),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                CircleImage(
                    painter = painterResource(R.drawable.profile_14),
                    size = 40.dp,
                    borderWidth = 0.3.dp
                )
                Box(
                    modifier =
                    Modifier
                        .weight(1f)
                        .height(40.dp)
                        .clip(shape = RoundedCornerShape(100))
                        .background(color = MaterialTheme.colorScheme.surfaceContainerHighest),
                    contentAlignment = Alignment.CenterStart
                ) {
                    Text(
                        modifier = Modifier.padding(start = 12.dp, end = 4.dp),
                        text = "Add a comment..",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        }
        SubtleHorizontalDivider(
            modifier = Modifier.align(Alignment.TopCenter)
        )
    }
}

@Preview
@Composable
private fun CommentBottomBarPreview() {
    MubbleTheme {
        CommentBottomBar()
    }
}
