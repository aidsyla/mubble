package com.aidsyla.mubble.feature.circle

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.aidsyla.mubble.common.navigation.CircleRoute
import com.aidsyla.mubble.data.DummyPostRepository
import com.aidsyla.mubble.data.FeedItem
import com.aidsyla.mubble.feature.circle.model.Circle
import com.aidsyla.mubble.feature.circle.model.CircleRepo
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CircleViewModel
    @Inject
    constructor(
        savedStateHandle: SavedStateHandle,
    ) : ViewModel() {
        private val circleId: String = savedStateHandle.toRoute<CircleRoute>().circleId

        private val _uiState = MutableStateFlow<CircleUiState>(CircleUiState.Loading)
        val uiState: StateFlow<CircleUiState> = _uiState.asStateFlow()

        init {
            loadCircle()
        }

        private fun loadCircle() {
            viewModelScope.launch {
                val circle =
                    CircleRepo.dummyCircles.find {
                        it.id == circleId
                    }
                val posts = DummyPostRepository.dummyFeedItems
                if (circle != null) {
                    _uiState.value =
                        CircleUiState.Success(
                            circle = circle,
                            items = posts,
                        )
                }
            }
        }
    }

sealed interface CircleUiState {
    data object Loading : CircleUiState

    data class Success(
        val circle: Circle,
        val items: List<FeedItem>,
    ) : CircleUiState
}
