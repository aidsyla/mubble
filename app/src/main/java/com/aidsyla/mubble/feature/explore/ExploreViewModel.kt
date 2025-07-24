package com.aidsyla.mubble.feature.explore

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aidsyla.mubble.data.BubbleFeedItem
import com.aidsyla.mubble.data.ImagePostFeedItem
import com.aidsyla.mubble.data.DummyPostRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class ExploreViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
) : ViewModel() {
    private val postId: String = savedStateHandle.get<String>("postId") ?: ""

    val uiState: StateFlow<ExploreUiState> = flowOf(
        ExploreUiState(
            media = DummyPostRepository.dummyFeedItems.filterIsInstance<ImagePostFeedItem>(),
            bubbles = DummyPostRepository.dummyFeedItems.filterIsInstance<BubbleFeedItem>(),
        )
    )
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = ExploreUiState(isLoading = true)
        )
}

data class ExploreUiState(
    val media: List<ImagePostFeedItem> = emptyList(),
    val bubbles: List<BubbleFeedItem> = emptyList(),
    val isLoading: Boolean = false,
)
