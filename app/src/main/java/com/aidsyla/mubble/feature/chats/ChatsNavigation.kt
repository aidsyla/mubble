package com.aidsyla.mubble.feature.chats

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import com.aidsyla.mubble.common.navigation.ChatDetailsRoute
import com.aidsyla.mubble.common.navigation.ChatListRoute
import com.aidsyla.mubble.common.navigation.ChatRoute

fun NavController.navigateToChatList(navOptions: NavOptions) =
    navigate(route = ChatListRoute, navOptions)

fun NavController.navigateToChat(
    chatId: String,
    otherUserId: String,
    navOptions: NavOptions? = null,
) {
    this.navigate(ChatRoute(chatId = chatId, otherUserId = otherUserId), navOptions)
}

fun NavController.navigateToChatDetails(
    otherUserId: String,
    navOptions: NavOptions? = null
) {
    this.navigate(ChatDetailsRoute(otherUserId = otherUserId), navOptions = navOptions)
}


fun NavGraphBuilder.chatListScreen(
    onChatClick: (
        chatId: String,
        otherUserId: String,
    ) -> Unit,
) {
    composable<ChatListRoute> {
        ChatListScreen(
            onChatClick = onChatClick
        )
    }
}

fun NavGraphBuilder.chatScreen(
    onBackClick: () -> Unit,
    onProfileClick: (userId: String) -> Unit,
    onMoreClick: (userId: String) -> Unit
) {
    composable<ChatRoute> {
        ChatScreen(
            onBackClick = onBackClick,
            onProfileClick = onProfileClick,
            onMoreClick = onMoreClick
        )
    }
}

fun NavGraphBuilder.chatDetailsScreen(
    onBackClick: () -> Unit,
    onProfileClick: (String) -> Unit
) {
    composable<ChatDetailsRoute> { backStackEntry ->
        val args: ChatDetailsRoute = backStackEntry.toRoute()
        ChatDetailsScreen(onBackClick = onBackClick, onProfileClick = onProfileClick, otherUserId = args.otherUserId)
    }
}
