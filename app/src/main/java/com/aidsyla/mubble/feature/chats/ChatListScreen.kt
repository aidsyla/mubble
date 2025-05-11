package com.aidsyla.mubble.feature.chats

import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.aidsyla.mubble.data.ChatPreview
import com.aidsyla.mubble.ui.LocalBottomBarPadding
import com.aidsyla.mubble.ui.theme.MubbleTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChatListScreen(
    modifier: Modifier = Modifier,
    onChatClick: (chatId: String, otherUserId: String) -> Unit,
    viewModel: ChatListViewModel = hiltViewModel(),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val editUiState by viewModel.editUiState.collectAsStateWithLifecycle()
    val scrollState = TopAppBarDefaults.enterAlwaysScrollBehavior()

    Scaffold(
        topBar = {
            when (val state = editUiState) {
                is ChatEditUiState.Edit -> {
                    CenterAlignedTopAppBar(
                        title = {
                            AnimatedContent(
                                targetState = !state.isEditing
                            ) {
                                when (it) {
                                    true -> Text(text = "Chats")
                                    false -> Text(text = "0 Selected")
                                }
                            }
                        },
                        navigationIcon = {
                            AnimatedContent(
                                targetState = !state.isEditing,
                            ) {
                                when (it) {
                                    true -> {
                                        IconButton(onClick = { viewModel.enterEditMode() }) {
                                            Icon(
                                                imageVector = Icons.Filled.Edit,
                                                contentDescription = null
                                            )
                                        }
                                    }

                                    false -> {
                                        IconButton(onClick = { viewModel.exitEditMode() }) {
                                            Icon(
                                                imageVector = Icons.Filled.ArrowBack,
                                                contentDescription = null
                                            )
                                        }
                                    }
                                }
                            }
                        },
                        actions = {
                            IconButton(onClick = {}) {
                                AnimatedContent(targetState = state.isEditing) {
                                    when (it) {
                                        true -> {
                                            Icon(
                                                imageVector = Icons.Filled.Delete,
                                                contentDescription = null
                                            )
                                        }

                                        false -> {
                                            Icon(
                                                imageVector = Icons.Filled.Search,
                                                contentDescription = null
                                            )
                                        }
                                    }
                                }
                            }
                        },
                        scrollBehavior = scrollState
                    )
                }
            }
        },
    ) { innerPadding ->
        val bottomPadding = LocalBottomBarPadding.current
        LazyColumn(
            modifier = Modifier
                .padding(top = innerPadding.calculateTopPadding(), bottom = bottomPadding)
                .nestedScroll(scrollState.nestedScrollConnection),
        ) {
            when (val state = uiState) {
                ChatListUiState.Loading -> {}
                is ChatListUiState.Success -> {
                    items(
                        items = state.chatPreviews,
                        key = { it.chatId }
                    ) { chatPreview ->
                        ChatListItem(
                            chatPreview = chatPreview,
                            onChatClick = onChatClick
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun ChatListItem(
    chatPreview: ChatPreview,
    onChatClick: (chatId: String, otherUserId: String) -> Unit,
    modifier: Modifier = Modifier,
) {
    val dotColor = MaterialTheme.colorScheme.primary
    Row(
        modifier = modifier
            .clickable { onChatClick(chatPreview.chatId, chatPreview.otherUserId) }
            .fillMaxWidth()
            .padding(8.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Image(
            painter = painterResource(id = chatPreview.otherUserProfilePicResId),
            contentDescription = "${chatPreview.otherUserName} profile picture",
            modifier = Modifier
                .size(48.dp)
                .clip(shape = CircleShape),
            contentScale = ContentScale.Crop
        )
        Column(
            modifier = Modifier.weight(1f)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Text(
                    text = chatPreview.otherUserName,
                    style = MaterialTheme.typography.titleMedium
                )
                if (chatPreview.isUnread) {
                    Canvas(
                        modifier = Modifier.size(8.dp),
                        onDraw = { drawCircle(color = dotColor) }
                    )
                }
            }
            Text(
                text = chatPreview.lastMessage,
                style = MaterialTheme.typography.bodySmall,
                maxLines = 2
            )
        }
        Column(
            horizontalAlignment = Alignment.End
        ) {
            Text(
                text = chatPreview.timestamp,
                style = MaterialTheme.typography.labelSmall
            )
            if (chatPreview.notificationsOff) {
                Icon(
                    imageVector = Icons.Default.Notifications,
                    contentDescription = "Notifications Off",
                    modifier = Modifier.size(16.dp),
                    tint = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}


@Preview
@Composable
private fun ChatPreview() {
    MubbleTheme {
        ChatListScreen(
            onChatClick = { _, _ -> }
        )
    }
}