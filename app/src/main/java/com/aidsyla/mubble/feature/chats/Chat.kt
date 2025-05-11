package com.aidsyla.mubble.feature.chats

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.aidsyla.mubble.data.ChatMessage

fun LazyListScope.chat(
    chatMessages: List<ChatMessage>,
    currentUserId: String
) {
    itemsIndexed(
        items = chatMessages,
        key = { _, message -> message.id }
    ) { index, message ->
        val isOutgoing = message.senderId == currentUserId
        if (index > 0) {
            val messageVisuallyBelow = chatMessages[index - 1]
            if (message.senderId != messageVisuallyBelow.senderId) {
                Spacer(modifier = Modifier.height(8.dp))
            }
        }
        ChatMessageBubble(message.message, message.timestamp, isOutgoing)
    }
}