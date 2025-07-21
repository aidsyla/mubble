package com.aidsyla.mubble.feature.profile.follow

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.SearchBar
import androidx.compose.material3.SearchBarState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.aidsyla.mubble.data.User
import com.aidsyla.mubble.model.FollowType

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FollowList(
    modifier: Modifier = Modifier,
    isCurrentUser: Boolean,
    listType: FollowType,
    items: List<User>,
    searchBarState: SearchBarState,
    inputField: @Composable () -> Unit,
    onUserClick: (String) -> Unit,
    onFollowClick: (String) -> Unit,
    onMessageClick: (String) -> Unit,
) {
    LazyColumn(
        modifier = modifier.fillMaxSize(),
        contentPadding = PaddingValues(
            top = 4.dp,
            bottom = WindowInsets.navigationBars.asPaddingValues().calculateBottomPadding()
        ),
        verticalArrangement = Arrangement.spacedBy(4.dp),
    ) {
        item {
            SearchBar(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp, horizontal = 8.dp),
                state = searchBarState,
                inputField = inputField,
            )
        }
        followList(
            isCurrentUser = isCurrentUser,
            listType = listType,
            items = items,
            onUserClick = onUserClick,
            onFollowClick = onFollowClick,
            onMessageClick = onMessageClick
        )
    }
}

private fun LazyListScope.followList(
    isCurrentUser: Boolean,
    listType: FollowType,
    items: List<User>,
    onUserClick: (String) -> Unit,
    onFollowClick: (String) -> Unit,
    onMessageClick: (String) -> Unit,
) {
    items(
        items = items,
    ) {
        UserItem(
            isCurrentUser = isCurrentUser,
            listType = listType,
            userId = it.id,
            username = it.username,
            profilePictureResId = it.profilePictureResId,
            onUserClick = onUserClick,
            onFollowClick = onFollowClick,
            onMessageClick = onMessageClick,
        )
    }
}