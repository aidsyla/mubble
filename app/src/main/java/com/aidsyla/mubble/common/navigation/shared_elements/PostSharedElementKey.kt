package com.aidsyla.mubble.common.navigation.shared_elements

data class PostSharedElementKey(
    val postId: String,
    val origin: PostOrigin,
    val type: PostSharedElementType,
)

enum class PostOrigin {
    None,
    HomeFollowing,
    HomeMyCircles,
    ExploreCircles,
    ExploreMedia,
    ExploreBubbles,
    ProfileMedia,
    ProfileBubbles,
}

enum class PostSharedElementType {
    ProfileAvatar,
    Bounds,
    DisplayName,
    Image,
    Video,
    Bubble,
}
