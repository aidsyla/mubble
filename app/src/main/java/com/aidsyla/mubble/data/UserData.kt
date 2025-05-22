package com.aidsyla.mubble.data

import androidx.annotation.DrawableRes
import com.aidsyla.mubble.R
import java.util.UUID

data class User(
    val id: String = UUID.randomUUID().toString(),
    @DrawableRes val bannerResId: Int,
    @DrawableRes val profilePictureResId: Int,
    val username: String,
    val displayName: String,
    val followerCount: Int,
    val followingCount: Int,
    val description: String? = null
)

object UserRepo {
    val user1 = User(
        id = "user_1",
        bannerResId = R.drawable.post_1,
        profilePictureResId = R.drawable.profile_1,
        username = "alex_smith",
        displayName = "Alex Smith",
        followerCount = 21,
        followingCount = 453,
        description = null
    )
    val user2 = User(
        id = "user_2",
        bannerResId = R.drawable.post_2,
        profilePictureResId = R.drawable.profile_2,
        username = "jordan_lee",
        displayName = "Jordan Lee",
        followerCount = 875,
        followingCount = 54,
        description = "Obviously the Material You design doesn't improve branding, but you already know the point is for cohesion. Obviously the Material You design doesn't improve branding, but you already know the point is for cohesion. Obviously the Material You design doesn't improve branding, but you already know the "
    )
    val user3 = User(
        id = "user_3",
        bannerResId = R.drawable.post_3,
        profilePictureResId = R.drawable.profile_3,
        username = "casey_williams",
        displayName = "Casey Williams",
        followerCount = 54,
        followingCount = 112,
        description = "I am Casey Williams"
    )

    private val allUsers = listOf(user1, user2, user3)
    private val usersById = allUsers.associateBy { it.id }

    fun getUser(userId: String): User? {
        return usersById[userId]
    }

    fun getAllUsers(): List<User> {
        return allUsers
    }
}