package com.aidsyla.mubble.feature.home.model

import androidx.annotation.DrawableRes
import com.aidsyla.mubble.common.components.post.PostVariant
import java.util.UUID

data class PostFeedItem(
    val id: String = UUID.randomUUID().toString(),
    val variant: PostVariant,
    val userName: String,
    @DrawableRes val userAvatarResId: Int,
    val postDescription: String,
    val datePosted: String,
    @DrawableRes val postImageResId: Int?,
    val likeCount: Int,
    val commentCount: Int,
    val shareCount: Int,
    val circleName: String? = null
)