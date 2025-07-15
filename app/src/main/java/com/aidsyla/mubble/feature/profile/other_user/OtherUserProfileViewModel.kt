package com.aidsyla.mubble.feature.profile.other_user

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aidsyla.mubble.data.User
import com.aidsyla.mubble.data.UserRepo
import com.aidsyla.mubble.feature.explore.model.BubbleFeedItem
import com.aidsyla.mubble.feature.explore.model.ImagePostFeedItem
import com.aidsyla.mubble.feature.home.data.DummyPostRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class OtherUserProfileViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
) : ViewModel() {

    private val viewedUserId: String? = savedStateHandle.get<String>("userId")

    private val _uiState =
        MutableStateFlow<OtherUserProfileUiState>(OtherUserProfileUiState.Loading)
    val uiState: StateFlow<OtherUserProfileUiState> = _uiState.asStateFlow()

    init {
        loadProfile()
    }

    private fun loadProfile() {
        viewModelScope.launch {
            val userId = viewedUserId ?: ""
            val user = UserRepo.getUser(userId)
            if (user != null) {
                val allPosts =
                    DummyPostRepository.dummyFeedItems.filterIsInstance<ImagePostFeedItem>()
                val filteredPosts = allPosts.filter {
                    it.id == userId
                }
                val allBubbles =
                    DummyPostRepository.dummyFeedItems.filterIsInstance<BubbleFeedItem>()
                val filteredBubbles = allBubbles.filter {
                    it.id == userId
                }
                _uiState.value = OtherUserProfileUiState.Success(
                    user = user,
                    posts = filteredPosts,
                    bubbles = filteredBubbles,
                )
            }
        }
    }
}

sealed interface OtherUserProfileUiState {
    data object Loading : OtherUserProfileUiState

    data class Success(
        val user: User,
        val posts: List<ImagePostFeedItem>,
        val bubbles: List<BubbleFeedItem>,
    ) : OtherUserProfileUiState
}