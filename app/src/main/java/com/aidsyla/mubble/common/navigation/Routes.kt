package com.aidsyla.mubble.common.navigation // Or your package

import kotlinx.serialization.Serializable

@Serializable object HomeRoute
@Serializable object ExploreRoute
@Serializable object ChatListRoute
@Serializable object ActivityRoute
@Serializable object ProfileRoute
@Serializable data class OtherProfileRoute(val username: String)

@Serializable data class PostDetailsRoute(val postId: String)
@Serializable data class UserProfileRoute(val userId: String)
@Serializable data class UserPostsRoute(val userId: String)

@Serializable object SettingsStartRoute
@Serializable object SettingsNotificationsRoute
@Serializable object SettingsDevicePermissionsRoute
@Serializable object SettingsManageAccountRoute

@Serializable
data class ChatRoute(
    val chatId: String,
    val otherUserId: String
)
@Serializable data class ChatDetailsRoute(val otherUserId: String)