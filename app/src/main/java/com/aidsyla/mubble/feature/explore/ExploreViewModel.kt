package com.aidsyla.mubble.feature.explore

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aidsyla.mubble.feature.explore.model.BubbleFeedItem
import com.aidsyla.mubble.feature.explore.model.FeedItem
import com.aidsyla.mubble.feature.explore.model.ImagePostFeedItem
import com.aidsyla.mubble.feature.home.data.DummyPostRepository
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
        ExploreUiState(items = sortExploreItemsForGrid_v6_optimized(DummyPostRepository.dummyFeedItems))
    )
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = ExploreUiState(isLoading = true)
        )

//    private fun sortExploreItemsForGrid(items: List<FeedItem>): List<FeedItem> {
//        val posts = items.filterIsInstance<ImagePostFeedItem>()
//        val bubbles = items.filterIsInstance<BubbleFeedItem>()
//
//        val sortedList = mutableListOf<FeedItem>()
//        val postIterator = posts.iterator()
//
//        while (postIterator.hasNext()) {
//            val post1 = postIterator.next()
//            sortedList.add(post1)
//
//            if (postIterator.hasNext()) {
//                val post2 = postIterator.next()
//                sortedList.add(post2)
//                if (bubbles.isNotEmpty()) {
//                    sortedList.add(bubbles.removeAt(0))
//                }
//            } else {
//                if (bubbles.isNotEmpty()) {
//                    sortedList.add(bubbles.removeAt(0))
//                }
//            }
//        }
//        sortedList.addAll(bubbles)
//        return sortedList
//    }

    private fun sortExploreItemsForGrid_v6_optimized(items: List<FeedItem>): List<FeedItem> {
        val posts = items.filterIsInstance<ImagePostFeedItem>()
        val bubbles = items.filterIsInstance<BubbleFeedItem>()

        val sortedList = mutableListOf<FeedItem>()
        val postIterator = posts.iterator()
        var bubbleIndex = 0

        while (postIterator.hasNext()) {
            val post1 = postIterator.next()
            sortedList.add(post1)

            if (postIterator.hasNext()) {
                val post2 = postIterator.next()
                sortedList.add(post2)
                if (bubbleIndex < bubbles.size) {
                    sortedList.add(bubbles[bubbleIndex])
                    bubbleIndex++
                }
            } else {
                if (bubbleIndex < bubbles.size) {
                    sortedList.add(bubbles[bubbleIndex])
                    bubbleIndex++
                }
            }
        }
        while (bubbleIndex < bubbles.size) {
            sortedList.add(bubbles[bubbleIndex])
            bubbleIndex++
        }
        return sortedList
    }
}

data class ExploreUiState(
    val items: List<FeedItem> = emptyList(),
    val isLoading: Boolean = false,
)
