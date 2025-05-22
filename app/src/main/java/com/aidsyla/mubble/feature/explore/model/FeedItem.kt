package com.aidsyla.mubble.feature.explore.model

import androidx.annotation.DrawableRes
import java.util.UUID

sealed interface FeedItem {
    val id: String
    val username: String
    val displayName: String
    @get:DrawableRes val userAvatarResId: Int
    val postDescription: String
    val datePosted: String
    val likeCount: Int
    val commentCount: Int
    val shareCount: Int
    val circleName: String?
}

data class ImagePostFeedItem(
    override val id: String = UUID.randomUUID().toString(),
    override val username: String,
    override val displayName: String,
    @DrawableRes override val userAvatarResId: Int,
    override val postDescription: String,
    override val datePosted: String,
    @DrawableRes val postImageResId: Int,
    override val likeCount: Int,
    override val commentCount: Int,
    override val shareCount: Int,
    override val circleName: String? = null
) : FeedItem

data class BubbleFeedItem(
    override val id: String = UUID.randomUUID().toString(),
    override val username: String,
    override val displayName: String,
    @DrawableRes override val userAvatarResId: Int,
    override val postDescription: String,
    override val datePosted: String,
    override val likeCount: Int,
    override val commentCount: Int,
    override val shareCount: Int,
    override val circleName: String? = null
) : FeedItem