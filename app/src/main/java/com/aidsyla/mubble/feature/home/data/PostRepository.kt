package com.aidsyla.mubble.feature.home.data

import com.aidsyla.mubble.R
import com.aidsyla.mubble.feature.explore.model.BubbleFeedItem
import com.aidsyla.mubble.feature.explore.model.FeedItem
import com.aidsyla.mubble.feature.explore.model.ImagePostFeedItem

object DummyPostRepository {

    val dummyFeedItems: List<FeedItem> = listOf(
        ImagePostFeedItem(
            id = "user_1",
            username = "john",
            displayName = "John Doe",
            userAvatarResId = R.drawable.profile_1,
            postDescription = "Just exploring the beautiful woods today! #nature #adventure",
            datePosted = "2h ago",
            postImageResId = R.drawable.post_1,
            likeCount = 15,
            commentCount = 3,
            shareCount = 1,
            circleName = "Nature Lovers"
        ),
        BubbleFeedItem(
            id = "user_2",
            username = "bob-builder",
            displayName = "Bob The Builder",
            userAvatarResId = R.drawable.profile_2,
            postDescription = "Anyone have recommendations for a good hammer? Mine just broke. Need something sturdy for heavy-duty work.",
            datePosted = "5h ago",
            likeCount = 8,
            commentCount = 12,
            shareCount = 0,
//            circleName = "DIY Projects"
        ),
        ImagePostFeedItem(
            id = "user_3",
            username = "ch.chaplin",
            displayName = "Charlie Chaplin",
            userAvatarResId = R.drawable.profile_3,
            postDescription = "Enjoying a quiet evening with a cup of tea and a good book.",
            datePosted = "1d ago",
            postImageResId = R.drawable.post_3,
            likeCount = 42,
            commentCount = 5,
            shareCount = 2
        ),
        BubbleFeedItem(
            username = "Diana Prince",
            displayName = "Diana Prince",
            userAvatarResId = R.drawable.profile_4,
            postDescription = "Thinking about the concept of justice and how it applies in modern society. What are your thoughts?",
            datePosted = "3d ago",
            likeCount = 112,
            commentCount = 55,
            shareCount = 23,
//            circleName = "Philosophy Club"
        ),
        ImagePostFeedItem(
            username = "alice_51",
            displayName = "Alice Wonderland",
            userAvatarResId = R.drawable.profile_5,
            postDescription = "Found this amazing little cafe downtown!",
            datePosted = "4d ago",
            postImageResId = R.drawable.post_4,
            likeCount = 28,
            commentCount = 6,
            shareCount = 4,
//            circleName = "Foodies"
        ),
        ImagePostFeedItem(
            username = "alice_51_2",
            displayName = "Alice Wonderland _ 2",
            userAvatarResId = R.drawable.profile_5,
            postDescription = "Found this amazing little cafe downtown!",
            datePosted = "4d ago",
            postImageResId = R.drawable.post_5,
            likeCount = 28,
            commentCount = 6,
            shareCount = 4,
//            circleName = "Foodies"
        ),
    )
}