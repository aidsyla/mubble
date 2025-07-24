package com.aidsyla.mubble.feature.profile.current_user

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aidsyla.mubble.data.User
import com.aidsyla.mubble.data.UserRepo
import com.aidsyla.mubble.data.BubbleFeedItem
import com.aidsyla.mubble.data.FeedItem
import com.aidsyla.mubble.data.ImagePostFeedItem
import com.aidsyla.mubble.data.DummyPostRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
) : ViewModel() {

    private val _uiState = MutableStateFlow<ProfileScreenUiState>(ProfileScreenUiState.Loading)
    val uiState: StateFlow<ProfileScreenUiState> = _uiState.asStateFlow()

    init {
        loadProfile()
    }

    private fun loadProfile() {
        viewModelScope.launch {
            val currentUserId = getCurrentUser().id
            val user = UserRepo.getUser(currentUserId)
            if (user != null)
                _uiState.value = ProfileScreenUiState.Success(
                    user = user,
                    posts = DummyPostRepository.dummyFeedItems.filterIsInstance<ImagePostFeedItem>(),
                    bubbles = DummyPostRepository.dummyFeedItems.filterIsInstance<BubbleFeedItem>(),
                    savedPosts = DummyPostRepository.dummyFeedItems
                )
        }
    }

    private fun getCurrentUser(): User = UserRepo.dummyUsers.component1()
}

sealed interface ProfileScreenUiState {

    data object Loading : ProfileScreenUiState

    data class Success(
        val user: User,
        val posts: List<ImagePostFeedItem>,
        val bubbles: List<BubbleFeedItem>,
        val savedPosts: List<FeedItem>
    ) : ProfileScreenUiState
}