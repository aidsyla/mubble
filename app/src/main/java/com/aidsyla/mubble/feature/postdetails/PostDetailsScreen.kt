package com.aidsyla.mubble.feature.postdetails

import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.aidsyla.mubble.R
import com.aidsyla.mubble.common.components.CircleImage
import com.aidsyla.mubble.common.components.post.BasePostLayout
import com.aidsyla.mubble.common.navigation.LocalNavAnimatedVisibilityScope
import com.aidsyla.mubble.common.navigation.LocalSharedTransitionScope
import com.aidsyla.mubble.common.navigation.shared_elements.PostOrigin
import com.aidsyla.mubble.common.navigation.shared_elements.PostSharedElementKey
import com.aidsyla.mubble.common.navigation.shared_elements.PostSharedElementType
import com.aidsyla.mubble.ui.theme.MubbleTheme

@OptIn(ExperimentalMaterial3Api::class, ExperimentalSharedTransitionApi::class)
@Composable
fun PostDetailsScreen(
    origin: PostOrigin,
    onUserClick: (String) -> Unit,
    onBackClick: () -> Unit,
    viewModel: PostDetailsViewModel = hiltViewModel(),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    val sharedTransitionScope = LocalSharedTransitionScope.current
        ?: throw IllegalStateException("No SharedElementScope found")
    val animatedContentScope = LocalNavAnimatedVisibilityScope.current
        ?: throw IllegalStateException("No AnimatedVisibility found")

    with(sharedTransitionScope) {
        when (val state = uiState) {
            PostDetailsUiState.Loading -> {

            }

            is PostDetailsUiState.Success -> {
                Scaffold(
                    modifier = Modifier
                        .sharedBounds(
                            rememberSharedContentState(
                                key = PostSharedElementKey(
                                    postId = state.postItem.id,
                                    origin = origin,
                                    type = PostSharedElementType.Bounds
                                )
                            ),
                            animatedVisibilityScope = animatedContentScope
                        ),
                    topBar = {
                        TopAppBar(
                            title = {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                                    modifier = Modifier
                                        .clip(shape = MaterialTheme.shapes.extraLarge)
                                        .padding(end = 8.dp)
                                ) {
                                    with(sharedTransitionScope) {
                                        CircleImage(
                                            modifier = Modifier.sharedElement(
                                                rememberSharedContentState(
                                                    key = PostSharedElementKey(
                                                        postId = state.postItem.id,
                                                        origin = origin,
                                                        type = PostSharedElementType.ProfileAvatar
                                                    )
                                                ),
                                                animatedVisibilityScope = animatedContentScope,
                                            ),
                                            painter = painterResource(state.postItem.userAvatarResId),
                                            contentDescription = "avatar"
                                        )
                                    }
                                    Column {
                                        Row(
                                            horizontalArrangement = Arrangement.spacedBy(6.dp),
                                        ) {
                                            Text(
                                                modifier = Modifier
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
                                                style = MaterialTheme.typography.bodyMedium.copy(
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
                                                    painter = MubbleTheme.Icons.InCircle,
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
                                    Icon(MubbleTheme.Icons.ArrowBack, contentDescription = "Back")
                                }
                            },
                            actions = {
                                IconButton(onClick = { }) {
                                    Icon(
                                        MubbleTheme.Icons.MoreHorizontal,
                                        contentDescription = "More"
                                    )
                                }
                            }
                        )
                    }
                ) {
                    Box(modifier = Modifier.fillMaxSize().padding(it)) {
                        BasePostLayout(
                            modifier = Modifier,
                            item = state.postItem,
                            origin = origin,
                            useCard = false,
                            isInPostDetails = true,
                            onUserClick = onUserClick,
                            onMoreClick = viewModel::onMoreClick,
                            onPostClick = { }
                        )
                    }
                }
            }
        }
    }
}