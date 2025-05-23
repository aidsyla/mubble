package com.aidsyla.mubble.feature.chats

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.aidsyla.mubble.R
import com.aidsyla.mubble.data.UserRepo

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChatDetailsScreen(
    modifier: Modifier = Modifier,
    otherUserId: String,
    onBackClick: () -> Unit,
    onProfileClick: (userId: String) -> Unit,
) {
    val otherUser by remember(otherUserId) {
        mutableStateOf(UserRepo.getUser(otherUserId))
    }
    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior()
    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Column(
                        modifier = Modifier
                            .clickable { onProfileClick(otherUserId) }
                            .fillMaxHeight(),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Image(
                            painter = painterResource(
                                id = otherUser?.profilePictureResId ?: R.drawable.profile_1
                            ),
                            contentDescription = "${otherUser?.displayName ?: "User"} profile picture",
                            modifier = Modifier
                                .clip(CircleShape)
                                .aspectRatio(1f)
                                .weight(1f),
                            contentScale = ContentScale.Crop,
                        )
                        Text(
                            otherUser?.displayName ?: "Chat",
                            style = MaterialTheme.typography.titleMedium,
                        )
                    }
                },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, "Back")
                    }
                },
                expandedHeight = TopAppBarDefaults.MediumAppBarExpandedHeight,
                scrollBehavior = scrollBehavior
            )
        },
    ) {
        ChatDetailsPager(
            modifier = Modifier.padding(it)
        )
    }
}

@Composable
fun ChatDetailsPager(modifier: Modifier = Modifier) {
    val titles = listOf("Media", "Posts", "Bubbles")
    val tabContent: List<@Composable () -> Unit> = listOf(
//        { ProfilePostGrid() },
//        { ProfilePostGrid() },
//        { ProfileBubbleList() },
    )
//    TabbedPager(modifier = modifier, titles = titles, content = tabContent)
}