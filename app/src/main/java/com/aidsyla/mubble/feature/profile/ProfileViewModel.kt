package com.aidsyla.mubble.feature.profile

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aidsyla.mubble.data.User
import com.aidsyla.mubble.data.UserRepo
import com.aidsyla.mubble.feature.explore.model.BubbleFeedItem
import com.aidsyla.mubble.feature.explore.model.FeedItem
import com.aidsyla.mubble.feature.explore.model.ImagePostFeedItem
import com.aidsyla.mubble.feature.home.data.DummyPostRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

class ProfileViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
) : ViewModel() {

    private val viewedUserId: String? = savedStateHandle.get<String>("userId")

    private val _uiState = MutableStateFlow<ProfileScreenUiState>(ProfileScreenUiState.Loading)
    val uiState: StateFlow<ProfileScreenUiState> = _uiState.asStateFlow()

    init {
        loadProfile()
    }

    private fun loadProfile() {
        viewModelScope.launch {
            val currentUserId = getCurrentUser().id

            if (viewedUserId == null || viewedUserId == currentUserId) {
                val user = UserRepo.getUser(currentUserId)
                if (user != null)
                    _uiState.value = ProfileScreenUiState.CurrentUser(
                        user = user,
                        posts = DummyPostRepository.dummyFeedItems.filterIsInstance<ImagePostFeedItem>(),
                        bubbles = DummyPostRepository.dummyFeedItems.filterIsInstance<BubbleFeedItem>(),
                        savedPosts = DummyPostRepository.dummyFeedItems
                    )
            } else {
                val user = UserRepo.getUser(viewedUserId)
                if (user != null)
                    _uiState.value = ProfileScreenUiState.OtherUser(user)
            }
        }
    }

    private fun getCurrentUser(): User = UserRepo.user1
}

sealed interface ProfileScreenUiState {

    data object Loading : ProfileScreenUiState

    data class OtherUser(
        val user: User,
    ) : ProfileScreenUiState

    data class CurrentUser(
        val user: User,
        val posts: List<ImagePostFeedItem>,
        val bubbles: List<BubbleFeedItem>,
        val savedPosts: List<FeedItem>
    ) : ProfileScreenUiState
}