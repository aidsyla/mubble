package com.aidsyla.mubble.feature.profile.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.aidsyla.mubble.data.User
import com.aidsyla.mubble.feature.profile.ProfileScreenUiState

@Composable
fun ProfileHeader(
    modifier: Modifier = Modifier,
    user: User,
    uiState: ProfileScreenUiState,
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
                    .aspectRatio(2f),
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
                description = user.description,
                uiState = uiState
            )
        }
        Spacer(modifier = Modifier.height(4.dp))
    }
}