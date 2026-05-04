package com.aidsyla.mubble.common.navigation

import androidx.annotation.DrawableRes
import com.aidsyla.mubble.common.navigation.sharedelements.PostOrigin
import com.aidsyla.mubble.feature.profile.components.FullScreenMediaType
import com.aidsyla.mubble.model.FollowType
import kotlinx.serialization.Serializable

@Serializable object HomeRoute

@Serializable object ExploreRoute

@Serializable object VideosRoute

@Serializable object ChatListRoute

@Serializable object ActivityRoute

@Serializable object ProfileRoute

@Serializable object NewPostScreenRoute

@Serializable data class OtherProfileRoute(
    val userId: String? = null
)

@Serializable data class CircleRoute(
    val circleId: String,
    val origin: PostOrigin
)

@Serializable data class PostDetailsRoute(
    val postId: String,
    val origin: PostOrigin
)

@Serializable data class UserProfileRoute(
    val userId: String
)

@Serializable data class UserPostsRoute(
    val userId: String
)

@Serializable data class FollowScreenRoute(
    val type: FollowType
)

@Serializable data class OtherFollowScreenRoute(
    val type: FollowType
)

@Serializable data class FullScreenMediaViewer(
    @param:DrawableRes val imageId: Int,
    val type: FullScreenMediaType
)

@Serializable object SettingsStartRoute

@Serializable object SettingsNotificationsRoute

@Serializable object SettingsDevicePermissionsRoute

@Serializable object SettingsManageAccountRoute

@Serializable
data class ChatRoute(
    val chatId: String,
    val otherUserId: String
)

@Serializable data class ChatDetailsRoute(
    val otherUserId: String
)
