package com.aidsyla.mubble.feature.postdetails

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aidsyla.mubble.feature.explore.model.FeedItem
import com.aidsyla.mubble.feature.home.data.DummyPostRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PostDetailsViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
) : ViewModel() {
    private val postId: String = savedStateHandle.get<String>("postId") ?: ""

    private val _uiState = MutableStateFlow<PostDetailsUiState>(PostDetailsUiState.Loading)
    val uiState: StateFlow<PostDetailsUiState> = _uiState.asStateFlow()

    init {
        loadPostDetails()
    }

    private fun loadPostDetails() {
        viewModelScope.launch {
            val post = DummyPostRepository.dummyFeedItems.find {
                it.id == postId
            }
            if (post != null) _uiState.value = PostDetailsUiState.Success(post)
        }
    }

    fun onMoreClick(postId: String) {
        println("More clicked on post details: $postId")
    }
}

sealed interface PostDetailsUiState {
    data object Loading : PostDetailsUiState

    data class Success(
        val postItem: FeedItem,
    ) : PostDetailsUiState
}
