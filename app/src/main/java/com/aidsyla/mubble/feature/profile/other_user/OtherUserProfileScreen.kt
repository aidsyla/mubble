package com.aidsyla.mubble.feature.profile.other_user

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.aidsyla.mubble.common.navigation.shared_elements.PostOrigin
import com.aidsyla.mubble.feature.profile.components.MubbleProfileTabPager
import com.aidsyla.mubble.feature.profile.components.ProfileHeader
import com.aidsyla.mubble.feature.profile.current_user.ProfileBubbleList
import com.aidsyla.mubble.feature.profile.current_user.ProfilePostGrid

@Composable
fun OtherUserProfileScreen(
    viewModel: OtherUserProfileViewModel = hiltViewModel(),
    onMoreClick: () -> Unit,
    onEditClick: () -> Unit,
    onBackClick: () -> Unit,
    onPostClick: (postId: String, origin: PostOrigin) -> Unit,
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    when (val state = uiState) {
        OtherUserProfileUiState.Loading -> {

        }

        is OtherUserProfileUiState.Success -> {
            MubbleProfileTabPager(
                modifier = Modifier,
                isCurrentUser = false,
                header = {
                    ProfileHeader(
                        user = state.user
                    )
                },
                firstPage = {
                    ProfilePostGrid(
                        items = state.posts,
                        onPostClick = onPostClick
                    )
                },
                secondPage = {
                    ProfileBubbleList(
                        items = state.bubbles,
                        onPostClick = onPostClick
                    )
                },
                onMoreClick = onMoreClick,
                onEditClick = onEditClick,
                onBackClick = onBackClick
            )
        }
    }
}