package com.aidsyla.mubble.feature.profile

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.aidsyla.mubble.R
import com.aidsyla.mubble.common.components.layout.TabbedPager
import com.aidsyla.mubble.data.User
import com.aidsyla.mubble.data.UserRepo
import com.aidsyla.mubble.ui.theme.MubbleTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(
    onNavigateToSettings: () -> Unit,
) {
    val scrollBehavior = TopAppBarDefaults.exitUntilCollapsedScrollBehavior()
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Profile") },
//                    navigationIcon = {
//                        IconButton(onClick = { }) {
//                            Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
//                        }
//                    },
                actions = {
                    IconButton(onClick = { /* TODO: Navigate Search */ }) {
                        Icon(
                            Icons.Default.Search,
                            null
                        )
                    }
                    IconButton(onClick = { /* TODO: Navigate Edit Profile */ }) {
                        Icon(
                            Icons.Default.Edit,
                            null
                        )
                    }
                    IconButton(onClick = onNavigateToSettings) {
                        Icon(
                            Icons.Default.Settings,
                            null
                        )
                    }
                },
                scrollBehavior = scrollBehavior,
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.Transparent
                )
            )
        }
    ) {
        TabbedPagerProfile(
            modifier = Modifier
                .nestedScroll(scrollBehavior.nestedScrollConnection)
                .padding(top = it.calculateTopPadding())
        )
    }
}

@Composable
fun TabbedPagerProfile(modifier: Modifier = Modifier) {
    val titles = listOf("Posts", "Bubbles", "Saved")
    val tabContent: List<@Composable () -> Unit> = listOf(
        { ProfilePostGrid() },
        { ProfileBubbleList() },
        { SaveScreen() }
    )
    TabbedPager(modifier = modifier, titles = titles, tabContent = tabContent) {
        UserDetails(user = UserRepo.user1)
    }
}

@Composable
fun SaveScreen() {
    val titles = listOf("Posts", "Bubbles")
    val tabContent: List<@Composable () -> Unit> = listOf(
        { ProfilePostGrid() },
        { ProfileBubbleList() },
    )
    TabbedPager(titles = titles, tabContent = tabContent)
}

@Composable
fun UserDetails(
    modifier: Modifier = Modifier,
    user: User,
) {
    Column(
        modifier = modifier
    ) {
        Box(
            contentAlignment = Alignment.BottomEnd
        ) {
            Image(
                painter = painterResource(user.bannerResId),
                contentDescription = null,
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(3f),
                contentScale = ContentScale.FillWidth
            )
            Image(
                painter = painterResource(user.profilePictureResId),
                contentDescription = null,
                modifier = Modifier
                    .padding(end = 16.dp)
                    .offset(y = (50).dp)
                    .size(100.dp)
                    .clip(CircleShape),
                contentScale = ContentScale.Crop
            )
        }
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
                .padding(horizontal = 16.dp)
                .padding(top = 12.dp)
        ) {
            ProfileDetails(
                modifier = Modifier.weight(1f),
                displayName = user.displayName,
                followerCount = user.followerCount,
                followingCount = user.followingCount,
                description = user.description
            )
        }
        Spacer(modifier = Modifier.height(4.dp))
    }
}

@Composable
fun ProfileBubbleList(modifier: Modifier = Modifier) {
    LazyColumn(
        contentPadding = PaddingValues(8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(48) {
//            ExploreBubble()
        }
    }
}


@Composable
fun ProfilePostGrid(modifier: Modifier = Modifier) {
    LazyVerticalGrid(
        modifier = modifier,
        userScrollEnabled = true,
        columns = GridCells.Fixed(2),
        contentPadding = PaddingValues(8.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(534) {
            ProfilePost()
        }
    }
}

@Composable
fun ProfilePost(modifier: Modifier = Modifier) {
    Image(
        painter = painterResource(R.drawable.ic_launcher_background), contentDescription = null,
        modifier = modifier
            .fillMaxWidth()
            .aspectRatio(1f)
            .clip(shape = MaterialTheme.shapes.medium),
        contentScale = ContentScale.FillWidth
    )
}

@Composable
fun ProfileDetails(
    modifier: Modifier = Modifier,
    displayName: String,
    followerCount: Int,
    followingCount: Int,
    description: String?,
) {
    Column(
        modifier = modifier
    ) {
        Text(
            text = displayName,
            style = MaterialTheme.typography.titleMedium
        )
        Row(
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Text(
                text = "$followerCount Followers",
                style = MaterialTheme.typography.labelMedium
            )
            Text(
                text = "$followingCount Following",
                style = MaterialTheme.typography.labelMedium
            )
        }
        Spacer(modifier = Modifier.height(4.dp))
        if (description != null)
            Text(
                text = description,
                style = MaterialTheme.typography.bodySmall
            )
        Spacer(modifier = Modifier.height(8.dp))
        Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Button(
                onClick = {},
                contentPadding = PaddingValues(horizontal = 20.dp, vertical = 8.dp),
                modifier = Modifier.height(32.dp)
            ) {
                Text(text = "Follow", style = MaterialTheme.typography.labelMedium)
            }
            Button(
                onClick = {},
                contentPadding = PaddingValues(horizontal = 20.dp, vertical = 8.dp),
                modifier = Modifier.height(32.dp)
            ) {
                Text(text = "Message", style = MaterialTheme.typography.labelMedium)
            }
        }
    }
}

@Preview
@Composable
private fun ProfilePreview() {
    MubbleTheme {
    }
}