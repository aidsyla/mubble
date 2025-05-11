package com.aidsyla.mubble.feature.chats

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aidsyla.mubble.data.ChatMessage
import com.aidsyla.mubble.data.ChatRepo
import com.aidsyla.mubble.data.User
import com.aidsyla.mubble.data.UserRepo
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ChatViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    private val userRepo: UserRepo,
    private val chatRepo: ChatRepo,
) : ViewModel() {

    private val chatId: String =
        savedStateHandle.get<String>("chatId") ?: ""
    private val otherUserId: String =
        savedStateHandle.get<String>("otherUserId") ?: ""

    private val _uiState = MutableStateFlow<ChatScreenUiState>(ChatScreenUiState.Loading)
    val uiState: StateFlow<ChatScreenUiState> = _uiState.asStateFlow()

    init {
        loadChat()
    }

    private fun loadChat() {
        viewModelScope.launch {
            val currentUserId = UserRepo.user1.id
            val otherUser = userRepo.getUser(otherUserId)
            val messages = chatRepo.getMessagesForChat(chatId)

            _uiState.value = ChatScreenUiState.Success(
                currentUserId = currentUserId,
                otherUser = otherUser!!,
                messages = messages
            )
        }
    }
}

sealed interface ChatScreenUiState {
    data object Loading : ChatScreenUiState

    data class Success(
        val currentUserId: String,
        val otherUser: User,
        val messages: List<ChatMessage>,
    ) : ChatScreenUiState
}
