package com.aidsyla.mubble.feature.profile.otheruser

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.aidsyla.mubble.common.navigation.sharedelements.PostOrigin
import com.aidsyla.mubble.feature.profile.components.FullScreenMediaType
import com.aidsyla.mubble.feature.profile.components.MubbleProfileTabPager
import com.aidsyla.mubble.feature.profile.components.ProfileHeader
import com.aidsyla.mubble.feature.profile.currentuser.ProfileBubbleGrid
import com.aidsyla.mubble.feature.profile.currentuser.ProfilePostGrid

@Composable
fun OtherUserProfileScreen(
    viewModel: OtherUserProfileViewModel = hiltViewModel(),
    onBackClick: () -> Unit,
    onPostClick: (postId: String, origin: PostOrigin) -> Unit,
    onMediaClick: (Int, FullScreenMediaType) -> Unit,
    onFollowersClick: () -> Unit,
    onFollowingClick: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    var isClicked by rememberSaveable { mutableStateOf(false) }

    when (val state = uiState) {
        OtherUserProfileUiState.Loading -> {
        }

        is OtherUserProfileUiState.Success -> {
            MubbleProfileTabPager(
                modifier = Modifier,
                title = state.user.displayName,
                isCurrentUser = false,
                header = {
                    ProfileHeader(
                        user = state.user,
                        isCurrentUser = false,
                        hasAvatarOrBannerBeenClicked = isClicked,
                        onHasBeenClickedChange = { isClicked = it },
                        onMediaClick = onMediaClick,
                        onFollowersClick = onFollowersClick,
                        onFollowingClick = onFollowingClick
                    )
                },
                firstPage = {
                    ProfilePostGrid(
                        items = state.posts,
                        onPostClick = onPostClick
                    )
                },
                secondPage = {
                    ProfileBubbleGrid(
                        items = state.bubbles,
                        onPostClick = onPostClick
                    )
                },
                onBackClick = onBackClick
            )
        }
    }
}
