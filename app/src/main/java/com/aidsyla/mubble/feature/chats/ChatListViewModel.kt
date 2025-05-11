package com.aidsyla.mubble.feature.chats

import androidx.lifecycle.ViewModel
import com.aidsyla.mubble.data.ChatPreview
import com.aidsyla.mubble.data.ChatRepo.allSampleChats
import com.aidsyla.mubble.data.UserRepo
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject
import kotlin.random.Random

@HiltViewModel
class ChatListViewModel @Inject constructor(
) : ViewModel() {

    private val _uiState = MutableStateFlow<ChatListUiState>(ChatListUiState.Loading)
    val uiState: StateFlow<ChatListUiState> = _uiState.asStateFlow()

    private val _editUiState = MutableStateFlow<ChatEditUiState>(ChatEditUiState.Edit(isEditing = false))
    val editUiState: StateFlow<ChatEditUiState> = _editUiState.asStateFlow()

    init {
        val chatPreviews = getChatPreviewsForUser(UserRepo.user1.id)
        _uiState.value = ChatListUiState.Success(chatPreviews = chatPreviews)
    }

    private fun getChatPreviewsForUser(currentUserId: String): List<ChatPreview> {
        val userChats = allSampleChats.filter { it.participantIds.contains(currentUserId) }

        return userChats.mapNotNull { chat ->
            val otherUserId = chat.participantIds.firstOrNull { it != currentUserId } ?: return@mapNotNull null
            val otherUser = UserRepo.getUser(otherUserId) ?: return@mapNotNull null
            val lastMessage = chat.messages.lastOrNull()
            ChatPreview(
                chatId = chat.chatId,
                otherUserId = otherUserId,
                otherUserName = otherUser.displayName,
                otherUserProfilePicResId = otherUser.profilePictureResId,
                lastMessage = lastMessage?.message ?: "No messages yet",
                timestamp = lastMessage?.timestamp ?: "",
                isUnread = Random.nextBoolean(),
                notificationsOff = Random.nextBoolean() && Random.nextBoolean()
            )
        }
    }

    fun enterEditMode() {
        _editUiState.value = ChatEditUiState.Edit(isEditing = true)
    }

    fun exitEditMode() {
        _editUiState.value = ChatEditUiState.Edit(isEditing = false)
    }
}

sealed interface ChatListUiState {
    data object Loading : ChatListUiState

    data class Success(
        val chatPreviews: List<ChatPreview>
    ) : ChatListUiState
}

sealed interface ChatEditUiState {
    data class Edit(
        val isEditing: Boolean
    ) : ChatEditUiState
}
