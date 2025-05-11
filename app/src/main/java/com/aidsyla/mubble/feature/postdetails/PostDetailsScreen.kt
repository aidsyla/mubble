package com.aidsyla.mubble.feature.postdetails

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.CircularProgressIndicator
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.aidsyla.mubble.common.components.post.BubbleDetails
import com.aidsyla.mubble.common.components.post.PostDetails
import com.aidsyla.mubble.feature.explore.model.BubbleFeedItem
import com.aidsyla.mubble.feature.explore.model.ImagePostFeedItem

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PostDetailsScreen(
    onUserClick: (String) -> Unit,
    onBackClick: () -> Unit,
    viewModel: PostDetailsViewModel = hiltViewModel(),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    when (val state = uiState) {
                        PostDetailsUiState.Loading -> {

                        }

                        is PostDetailsUiState.Success -> {
                            Row(
                                modifier = Modifier
                                    .clip(shape = MaterialTheme.shapes.extraLarge)
                                    .clickable { onUserClick(state.postItem.username) }
                                    .padding(end = 8.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                Image(
                                    painter = painterResource(
                                        id = state.postItem.userAvatarResId
                                    ),
                                    contentDescription = null,
                                    modifier = Modifier
                                        .size(48.dp)
                                        .clip(CircleShape),
                                    contentScale = ContentScale.Crop,
                                )
                                Text(
                                    state.postItem.displayName,
                                    style = MaterialTheme.typography.titleMedium
                                )
                            }
                        }
                    }
                },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                },
            )
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            when (val state = uiState) {
                PostDetailsUiState.Loading -> {
                    CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
                }

                is PostDetailsUiState.Success -> {
                    when (val item = state.postItem) {
                        is ImagePostFeedItem -> PostDetails(
                            item = item,
                            onUserClick = {},
                            onMoreClick = viewModel::onMoreClick,
                            onPostClick = {}
                        )

                        is BubbleFeedItem -> BubbleDetails(
                            item = item,
                            onUserClick = {},
                            onMoreClick = viewModel::onMoreClick,
                            onPostClick = {}
                        )
                    }
                }
            }
        }
    }
}