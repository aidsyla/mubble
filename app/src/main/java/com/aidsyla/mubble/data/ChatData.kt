package com.aidsyla.mubble.data

import androidx.annotation.DrawableRes
import java.util.UUID

data class ChatPreview(
    val chatId: String = UUID.randomUUID().toString(),
    val otherUserId: String,
    val otherUserName: String,
    @DrawableRes val otherUserProfilePicResId: Int,
    val lastMessage: String,
    val timestamp: String,
    val isUnread: Boolean,
    val notificationsOff: Boolean
)

data class ChatMessage(
    val id: String = UUID.randomUUID().toString(),
    val message: String,
    val timestamp: String,
    val senderId: String,
)

data class Chat(
    val chatId: String,
    val participantIds: List<String>,
    val messages: List<ChatMessage>
)

object ChatRepo {

    private val chat12Messages = listOf(
        ChatMessage(senderId = UserRepo.user2.id, message = "Maybe later, thanks!", timestamp = "10:03 AM"),
        ChatMessage(senderId = UserRepo.user1.id, message = "Great! Need any help?", timestamp = "10:02 AM"),
        ChatMessage(senderId = UserRepo.user2.id, message = "Hey Alex! Going well, almost done with the UI.", timestamp = "10:01 AM"),
        ChatMessage(senderId = UserRepo.user1.id, message = "Hey Jordan, how's the project going?", timestamp = "10:00 AM"),
    )

    private val chat13Messages = listOf(
        ChatMessage(senderId = UserRepo.user1.id, message = "👍", timestamp = "11:32 AM"),
        ChatMessage(senderId = UserRepo.user3.id, message = "The usual spot?", timestamp = "11:31 AM"),
        ChatMessage(senderId = UserRepo.user1.id, message = "Sure, where?", timestamp = "11:31 AM"),
        ChatMessage(senderId = UserRepo.user3.id, message = "Lunch today?", timestamp = "11:30 AM")
    )

    private val chat23Messages = listOf(
        ChatMessage(senderId = UserRepo.user2.id, message = "Did you see the email from marketing?", timestamp = "Yesterday"),
        ChatMessage(senderId = UserRepo.user3.id, message = "Not yet, what's up?", timestamp = "Yesterday"),
        ChatMessage(senderId = UserRepo.user2.id, message = "New campaign details.", timestamp = "Yesterday")
    )

    val allSampleChats = listOf(
        Chat(chatId = "chat_1_2", participantIds = listOf(UserRepo.user1.id, UserRepo.user2.id), messages = chat12Messages),
        Chat(chatId = "chat_1_3", participantIds = listOf(UserRepo.user1.id, UserRepo.user3.id), messages = chat13Messages),
        Chat(chatId = "chat_2_3", participantIds = listOf(UserRepo.user2.id, UserRepo.user3.id), messages = chat23Messages)
    )
    private val chatsById = allSampleChats.associateBy { it.chatId }

    fun getMessagesForChat(chatId: String): List<ChatMessage> {
        return chatsById[chatId]?.messages ?: emptyList()
    }
}