package com.aidsyla.mubble.common.navigation

import com.aidsyla.mubble.common.navigation.shared_elements.PostOrigin
import kotlinx.serialization.Serializable

@Serializable object HomeRoute
@Serializable object ExploreRoute
@Serializable object VideosRoute
@Serializable object ChatListRoute
@Serializable object ActivityRoute
@Serializable object ProfileRoute
@Serializable data class OtherProfileRoute(val userId: String? = null)

@Serializable data class PostDetailsRoute(val postId: String, val origin: PostOrigin)
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