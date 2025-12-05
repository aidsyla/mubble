package com.aidsyla.mubble.feature.profile.follow

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.expandIn
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.text.input.rememberTextFieldState
import androidx.compose.foundation.text.input.setTextAndPlaceCursorAtEnd
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExpandedFullScreenSearchBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SearchBarDefaults
import androidx.compose.material3.SearchBarValue
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.rememberSearchBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.aidsyla.mubble.common.components.Tab
import com.aidsyla.mubble.data.UserRepo
import com.aidsyla.mubble.model.FollowType
import com.aidsyla.mubble.ui.theme.MubbleTheme
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FollowScreen(
    viewModel: FollowViewModel = hiltViewModel(),
    isCurrentUser: Boolean,
    type: FollowType,
    onUserClick: (String) -> Unit,
    onMessageClick: (String) -> Unit,
    onBackClick: () -> Unit,
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    val textFieldState = rememberTextFieldState()
    val searchBarState = rememberSearchBarState()
    val coroutineScope = rememberCoroutineScope()

    val tabs = remember { listOf("312k Followers", "1482 Following") }
    val initialPage =
        when (type) {
            FollowType.FOLLOWERS -> 0
            FollowType.FOLLOWING -> 1
        }
    val pagerState = rememberPagerState(initialPage = initialPage) { tabs.size }

    val inputField = @Composable {
        SearchBarDefaults.InputField(
            modifier = Modifier,
            searchBarState = searchBarState,
            textFieldState = textFieldState,
            onSearch = { coroutineScope.launch { searchBarState.animateToCollapsed() } },
            placeholder = { Text("Search") },
            leadingIcon = {
                AnimatedContent(
                    targetState = searchBarState.currentValue,
                    contentAlignment = Alignment.Center,
                    transitionSpec = {
                        fadeIn() +
                            expandIn(
                                expandFrom = Alignment.Center,
                                clip = false,
                            ) togetherWith fadeOut() +
                            shrinkOut(
                                shrinkTowards = Alignment.Center,
                                clip = false,
                            )
                    },
                ) { targetSearchBarValue ->
                    when (targetSearchBarValue) {
                        SearchBarValue.Expanded -> {
                            IconButton(
                                onClick = { coroutineScope.launch { searchBarState.animateToCollapsed() } },
                            ) {
                                Icon(
                                    painter = MubbleTheme.Icons.ArrowBack,
                                    contentDescription = "Back",
                                )
                            }
                        }

                        SearchBarValue.Collapsed -> {
                            Icon(
                                painter = MubbleTheme.Icons.Search,
                                contentDescription = "Search",
                            )
                        }
                    }
                }
            },
        )
    }

    Scaffold(
        topBar = {
            Column {
                CenterAlignedTopAppBar(title = { Text("alex_smith") }, navigationIcon = {
                    IconButton(
                        onClick = onBackClick,
                    ) {
                        Icon(
                            painter = MubbleTheme.Icons.ArrowBack,
                            contentDescription = null,
                        )
                    }
                })
                Tab(
                    pagerState = pagerState,
                    tabs = tabs,
                )
                ExpandedFullScreenSearchBar(
                    state = searchBarState,
                    inputField = inputField,
                    windowInsets = { WindowInsets.statusBars },
                ) {
                    SearchResults(
                        onResultClick = { result ->
                            textFieldState.setTextAndPlaceCursorAtEnd(result)
                            coroutineScope.launch { searchBarState.animateToCollapsed() }
                        },
                    )
                }
            }
        },
    ) { padding ->
        when (val state = uiState) {
            FollowScreenUiState.Loading -> {}
            is FollowScreenUiState.Success -> {
                HorizontalPager(
                    state = pagerState,
                    verticalAlignment = Alignment.Top,
                    modifier =
                        Modifier
                            .fillMaxSize()
                            .padding(top = padding.calculateTopPadding()),
                ) { pageIndex ->
                    when (pageIndex) {
                        0 ->
                            FollowList(
                                isCurrentUser = isCurrentUser,
                                listType = FollowType.FOLLOWERS,
                                items = state.followers,
                                searchBarState = searchBarState,
                                inputField = inputField,
                                onUserClick = onUserClick,
                                onFollowClick = {},
                                onMessageClick = onMessageClick,
                            )

                        1 ->
                            FollowList(
                                isCurrentUser = isCurrentUser,
                                listType = FollowType.FOLLOWING,
                                items = state.following,
                                searchBarState = searchBarState,
                                inputField = inputField,
                                onUserClick = onUserClick,
                                onFollowClick = {},
                                onMessageClick = onMessageClick,
                            )
                    }
                }
            }
        }
    }
}

@Preview
@Composable
private fun FollowUserItemPreview() {
    val user = UserRepo.dummyUsers[0]
    MubbleTheme {
        Surface {
            UserItem(
                isCurrentUser = true,
                listType = FollowType.FOLLOWING,
                userId = "",
                username = user.username,
                profilePictureResId = user.profilePictureResId,
                onUserClick = {},
                onFollowClick = {},
                onMessageClick = {},
            )
        }
    }
}
