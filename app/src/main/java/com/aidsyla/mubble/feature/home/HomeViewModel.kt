package com.aidsyla.mubble.feature.home

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aidsyla.mubble.feature.explore.model.FeedItem
import com.aidsyla.mubble.feature.home.data.DummyPostRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    ) : ViewModel() {

    val uiState: StateFlow<PostListUiState> = flowOf(
        PostListUiState(items = DummyPostRepository.dummyFeedItems)
    )
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = PostListUiState(isLoading = true)
        )

    fun onMoreClick(postId: String) {
        println("More clicked on post: $postId")
    }
}

data class PostListUiState(
    val items: List<FeedItem> = emptyList(),
    val isLoading: Boolean = false,
)