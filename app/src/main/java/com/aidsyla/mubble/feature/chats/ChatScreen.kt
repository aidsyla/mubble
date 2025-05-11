package com.aidsyla.mubble.feature.chats

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.MailOutline
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.aidsyla.mubble.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChatScreen(
    modifier: Modifier = Modifier,
    onBackClick: () -> Unit,
    onProfileClick: (userId: String) -> Unit,
    onMoreClick: (userId: String) -> Unit,
    viewModel: ChatViewModel = hiltViewModel(),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior()
    val listState = rememberLazyListState()

    when (val state = uiState) {
        ChatScreenUiState.Loading -> {}
        is ChatScreenUiState.Success -> {
            Scaffold(topBar = {
                TopAppBar(
                    title = {
                        Row(
                            modifier = Modifier.clickable {
                                onProfileClick(state.otherUser.id)
                            },
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Image(
                                painter = painterResource(
                                    id = state.otherUser.profilePictureResId
                                ),
                                contentDescription = "${state.otherUser.displayName} profile picture",
                                modifier = Modifier
                                    .size(40.dp)
                                    .clip(CircleShape),
                                contentScale = ContentScale.Crop,
                            )
                            Text(
                                text = state.otherUser.displayName,
                                style = MaterialTheme.typography.titleMedium
                            )
                        }
                    }, navigationIcon = {
                        IconButton(onClick = onBackClick) {
                            Icon(Icons.AutoMirrored.Filled.ArrowBack, "Back")
                        }
                    }, actions = {
                        IconButton(onClick = { onMoreClick(state.otherUser.id) }) {
                            Icon(Icons.Filled.MoreVert, "More options")
                        }
                    }, scrollBehavior = scrollBehavior
                )
            }, bottomBar = {
                BottomAppBar(
                    modifier = Modifier.clip(
                        shape = RoundedCornerShape(
                            topStart = 12.dp, topEnd = 12.dp
                        )
                    ),
                    contentPadding = PaddingValues(12.dp),
                ) {
                    Box(
                        contentAlignment = Alignment.CenterStart,
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxHeight()
                            .clip(shape = RoundedCornerShape(50.dp))
                            .background(MaterialTheme.colorScheme.surfaceContainerHigh)
                            .padding(horizontal = 16.dp, vertical = 8.dp)
                    ) {
                        Text(
                            "Type your message...", style = MaterialTheme.typography.bodyMedium
                        )
                    }
                    IconButton(
                        onClick = {}) {
                        Icon(imageVector = Icons.Default.MailOutline, contentDescription = null)
                    }
                }
            }) {
                LazyColumn(
                    state = listState,
                    reverseLayout = true,
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(it)
                        .nestedScroll(scrollBehavior.nestedScrollConnection),
                    contentPadding = PaddingValues(8.dp),
                    verticalArrangement = Arrangement.spacedBy(4.dp, Alignment.Bottom)
                ) {
                    chat(
                        chatMessages = state.messages,
                        currentUserId = state.currentUserId
                    )
                }
            }
            LaunchedEffect(state.messages.size) {
                if (state.messages.isNotEmpty()) {
                    listState.scrollToItem(index = 0)
                }
            }
        }
    }
}

@Composable
fun ChatMessageBubble(
    text: String,
    timestamp: String,
    isOutgoing: Boolean,
    modifier: Modifier = Modifier,
) {
    var showDate by remember { mutableStateOf(false) }
    Box(
        modifier = modifier.fillMaxWidth(),
        contentAlignment = if (isOutgoing) Alignment.CenterEnd else Alignment.CenterStart
    ) {
        Column(
            horizontalAlignment = if (isOutgoing) Alignment.End else Alignment.Start,
            modifier = Modifier.fillMaxWidth(0.8f)
        ) {
            Box(
                modifier = Modifier
                    .clip(shape = RoundedCornerShape(20.dp))
                    .background(
                        if (isOutgoing) MaterialTheme.colorScheme.primaryContainer
                        else MaterialTheme.colorScheme.surfaceContainer
                    )
                    .clickable { showDate = !showDate },
            ) {
                Text(
                    text = text,
                    modifier = Modifier.padding(12.dp),
                    style = MaterialTheme.typography.bodyMedium
                )
            }
            AnimatedVisibility(
                visible = showDate,
            ) {
                Text(
                    text = timestamp, modifier = Modifier.padding(
                        end = if (isOutgoing) 12.dp else 0.dp,
                        start = if (isOutgoing) 0.dp else 12.dp
                    ), style = MaterialTheme.typography.labelSmall
                )
            }
        }
    }
}


@Composable
fun ChatImage(modifier: Modifier = Modifier) {
    Image(
        painter = painterResource(R.drawable.ic_launcher_background),
        contentDescription = null,
        modifier = Modifier
            .fillMaxWidth(0.5f)
            .clip(
                shape = RoundedCornerShape(20.dp)
            ),
        contentScale = ContentScale.FillWidth
    )
}
