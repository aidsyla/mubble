package com.aidsyla.mubble.data

import androidx.annotation.DrawableRes
import java.util.UUID

data class Comment(
    val id: String = UUID.randomUUID().toString(),
    @param:DrawableRes val userAvatarResId: Int,
    val username: String,
    val postId: String,
    val userId: String,
    val text: String,
    val createdAt: String,
    val likeCount: Int,
    val replyCount: Int = 0
)