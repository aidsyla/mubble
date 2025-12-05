package com.aidsyla.mubble.feature.profile.follow

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aidsyla.mubble.data.User
import com.aidsyla.mubble.data.UserRepo
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FollowViewModel
    @Inject
    constructor() : ViewModel() {
        private val _uiState = MutableStateFlow<FollowScreenUiState>(FollowScreenUiState.Loading)
        val uiState: StateFlow<FollowScreenUiState> = _uiState.asStateFlow()

        init {
            load()
        }

        private fun load() {
            viewModelScope.launch {
                _uiState.value =
                    FollowScreenUiState.Success(
                        followers = UserRepo.dummyUsers,
                        following = UserRepo.dummyUsers,
                    )
            }
        }
    }

sealed interface FollowScreenUiState {
    data object Loading : FollowScreenUiState

    data class Success(
        val followers: List<User>,
        val following: List<User>,
    ) : FollowScreenUiState
}
