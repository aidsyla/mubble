package com.aidsyla.mubble.feature.chats

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LocalRippleConfiguration
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
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
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.aidsyla.mubble.common.components.CircleImage
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
            CenterAlignedTopAppBar(
                title = {
                    AnimatedContent(
                        targetState = editUiState.isEditing
                    ) {
                        if (it) Text(text = "0 Selected") else Text(text = "Chats")
                    }
                }, navigationIcon = {
                    AnimatedContent(
                        targetState = editUiState.isEditing,
                    ) {
                        IconButton(onClick = { viewModel.editModeSwitch() }) {
                            Icon(
                                imageVector = if (it) Icons.Filled.ArrowBack else Icons.Filled.Edit,
                                contentDescription = null
                            )
                        }
                    }
                }, actions = {
                    AnimatedContent(targetState = editUiState.isEditing) {
                        if (it)
                            IconButton(onClick = { }) {
                                Icon(
                                    imageVector = Icons.Filled.Delete, contentDescription = null
                                )
                            }
                        else
                            IconButton(onClick = { }) {
                                Icon(
                                    imageVector = Icons.Filled.Search, contentDescription = null
                                )
                            }
                    }
                }, scrollBehavior = scrollState
            )
        },
    ) { innerPadding ->
        val bottomPadding = LocalBottomBarPadding.current
        LazyColumn(
            modifier = Modifier
                .padding(
                    top = innerPadding.calculateTopPadding(), bottom = bottomPadding
                )
                .nestedScroll(scrollState.nestedScrollConnection),
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            when (val state = uiState) {
                ChatListUiState.Loading -> {}
                is ChatListUiState.Success -> {
                    items(
                        items = state.chatPreviews, key = { it.chatId }) { chatPreview ->
                        ChatListItem(
                            isEditing = editUiState.isEditing,
                            chatPreview = chatPreview,
                            onChatClick = onChatClick
                        )
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun ChatListItem(
    isEditing: Boolean,
    chatPreview: ChatPreview,
    onChatClick: (chatId: String, otherUserId: String) -> Unit,
    modifier: Modifier = Modifier,
) {
    val dotColor = MaterialTheme.colorScheme.primary
    var isSelected by remember { mutableStateOf(false) }
    val targetColor =
        if (isSelected) MaterialTheme.colorScheme.surfaceContainerHigh
        else MaterialTheme.colorScheme.surface
    val backgroundColor by animateColorAsState(targetValue = targetColor)

    val chatModifier = if (!isEditing) modifier.clickable {
        onChatClick(
            chatPreview.chatId, chatPreview.otherUserId
        )
    }
    else modifier.clickable { isSelected = !isSelected }

    LaunchedEffect(isEditing) {
        if (!isEditing) {
            isSelected = false
        }
    }

    CompositionLocalProvider(
        value = LocalRippleConfiguration provides if (isEditing) null else LocalRippleConfiguration.current
    ) {
        Box(
            modifier = Modifier
                .padding(horizontal = 8.dp)
                .clip(MaterialTheme.shapes.largeIncreased)
                .background(backgroundColor)
        ) {
            Row(
                modifier = chatModifier
                    .fillMaxWidth()
                    .padding(vertical = 6.dp, horizontal = 8.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                verticalAlignment = Alignment.Top
            ) {
                if (chatPreview.isUnread) {
                    Canvas(
                        modifier = Modifier
                            .size(8.dp)
                            .align(Alignment.CenterVertically),
                        onDraw = { drawCircle(color = dotColor) })
                }
                CircleImage(
                    painter = painterResource(id = chatPreview.otherUserProfilePicResId),
                    contentDescription = "${chatPreview.otherUserName} profile picture",
                    borderWidth = 0.dp
                )
                Column(
                    modifier = Modifier.weight(1f)
                ) {
                    Text(
                        text = chatPreview.otherUserName,
                        style = MaterialTheme.typography.titleMedium
                    )
                    Text(
                        text = chatPreview.lastMessage,
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        overflow = TextOverflow.Ellipsis,
                        maxLines = 1
                    )
                }
                Column(
                    modifier = Modifier.align(Alignment.CenterVertically),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = chatPreview.timestamp,
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    if (chatPreview.notificationsOff) {
                        Icon(
                            painter = MubbleTheme.Icons.NotificationsOff,
                            contentDescription = "Notifications Off",
                            modifier = Modifier.size(16.dp),
                            tint = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }
        }
    }
}


@Preview
@Composable
private fun ChatPreview() {
    MubbleTheme {
        ChatListScreen(
            onChatClick = { _, _ -> })
    }
}