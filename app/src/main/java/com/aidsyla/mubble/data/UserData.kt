package com.aidsyla.mubble.data

import androidx.annotation.DrawableRes
import com.aidsyla.mubble.R
import java.util.UUID

data class User(
    val id: String = UUID.randomUUID().toString(),
    @param:DrawableRes val bannerResId: Int,
    @param:DrawableRes val profilePictureResId: Int,
    val username: String,
    val displayName: String,
    val followerCount: Int,
    val followingCount: Int,
    val description: String? = null
)

object UserRepo {
    val dummyUsers: List<User> =
        listOf(
            User(
                id = "user_1",
                bannerResId = R.drawable.post_1,
                profilePictureResId = R.drawable.profile_1,
                username = "jayden_03",
                displayName = "Jayden Parker",
                followerCount = 1245,
                followingCount = 312,
                description = "Nature enthusiast and adventure seeker. Always looking for the next trail. 🌳"
            ),
            User(
                id = "user_2",
                bannerResId = R.drawable.post_2,
                profilePictureResId = R.drawable.profile_2,
                username = "melissa.k",
                displayName = "Melissa King",
                followerCount = 875,
                followingCount = 54,
                description = null
            ),
            User(
                id = "user_3",
                bannerResId = R.drawable.post_3,
                profilePictureResId = R.drawable.profile_3,
                username = "nina.rae_",
                displayName = "Nina Rae",
                followerCount = 2300,
                followingCount = 180,
                description = "Living for sunsets and good vibes. Capturing life's beautiful moments. 📸"
            ),
            User(
                id = "user_4",
                bannerResId = R.drawable.post_4,
                profilePictureResId = R.drawable.profile_4,
                username = "the_real_luca",
                displayName = "Luca Romano",
                followerCount = 765,
                followingCount = 98,
                description = "Fuelled by coffee and good conversations. Building dreams one sip at a time. ☕"
            ),
            User(
                id = "user_5",
                bannerResId = R.drawable.post_5,
                profilePictureResId = R.drawable.profile_5,
                username = "alex.99",
                displayName = "Alex Cruz",
                followerCount = 150,
                followingCount = 60,
                description = null
            ),
            User(
                id = "user_6",
                bannerResId = R.drawable.post_6,
                profilePictureResId = R.drawable.profile_6,
                username = "_.matthew",
                displayName = "Matthew Lee",
                followerCount = 3100,
                followingCount = 240,
                description = "Chasing city lights and late-night adventures. Photography and urban exploration."
            ),
            User(
                id = "user_7",
                bannerResId = R.drawable.post_7,
                profilePictureResId = R.drawable.profile_7,
                username = "sophia_xoxo",
                displayName = "Sophia Lin",
                followerCount = 980,
                followingCount = 110,
                description = "Mountains are calling and I must go! Hiking, camping, and exploring the wild. 🌲"
            ),
            User(
                id = "user_8",
                bannerResId = R.drawable.post_8,
                profilePictureResId = R.drawable.profile_8,
                username = "ryan.quest",
                displayName = "Ryan Quest",
                followerCount = 4500,
                followingCount = 380,
                description = "Surf's up! Living by the ocean, loving the breeze. Beach life is the best life. 🏄‍♂️"
            ),
            User(
                id = "user_9",
                bannerResId = R.drawable.post_9,
                profilePictureResId = R.drawable.profile_9,
                username = "kiara_sun",
                displayName = "Kiara Sun",
                followerCount = 320,
                followingCount = 75,
                description = "Capturing light and shadow. A passionate photographer sharing my world through the lens. ☀️"
            ),
            User(
                id = "user_10",
                bannerResId = R.drawable.post_10,
                profilePictureResId = R.drawable.profile_10,
                username = "logan.west",
                displayName = "Logan West",
                followerCount = 1800,
                followingCount = 150,
                description = "Stargazer and night sky photographer. Finding wonder in the cosmos. ✨🌌"
            ),
            User(
                id = "user_11",
                bannerResId = R.drawable.post_11,
                profilePictureResId = R.drawable.profile_11,
                username = "zoe.riley",
                displayName = "Zoe Riley",
                followerCount = 5000,
                followingCount = 400,
                description = null
            ),
            User(
                id = "user_12",
                bannerResId = R.drawable.post_12,
                profilePictureResId = R.drawable.profile_12,
                username = "ethan_fox",
                displayName = "Ethan Fox",
                followerCount = 80,
                followingCount = 20,
                description = "A connoisseur of classic cars and vintage tunes. Appreciating the timeless things."
            ),
            User(
                id = "user_13",
                bannerResId = R.drawable.post_13,
                profilePictureResId = R.drawable.profile_13,
                username = "lily.oak",
                displayName = "Lily Oak",
                followerCount = 3800,
                followingCount = 290,
                description = "Music is my therapy. Grooving through life, one beat at a time. 🎶"
            ),
            User(
                id = "user_14",
                bannerResId = R.drawable.post_14,
                profilePictureResId = R.drawable.profile_14,
                username = "mason_haze",
                displayName = "Mason Haze",
                followerCount = 650,
                followingCount = 105,
                description = "Finding beauty in simplicity and growth. Sharing moments of peace and inspiration. 🌸"
            ),
            User(
                id = "user_15",
                bannerResId = R.drawable.post_15,
                profilePictureResId = R.drawable.profile_15,
                username = "ava.dream",
                displayName = "Ava Dream",
                followerCount = 7500,
                followingCount = 500,
                description = "Rock and roll never dies! Guitarist and music enthusiast. Living for the stage. 🎸"
            ),
            User(
                id = "user_16",
                bannerResId = R.drawable.post_1,
                profilePictureResId = R.drawable.profile_1,
                username = "urban.jay",
                displayName = "Urban Jay",
                followerCount = 450,
                followingCount = 80,
                description = "Exploring the concrete jungle, one quiet moment at a time. City life, deep thoughts."
            ),
            User(
                id = "user_17",
                bannerResId = R.drawable.post_2,
                profilePictureResId = R.drawable.profile_2,
                username = "midnight.byte",
                displayName = "Midnight Byte",
                followerCount = 310,
                followingCount = 55,
                description = "Night owl, coder, and experimental chef. My best ideas (and recipes) come after dark."
            ),
            User(
                id = "user_18",
                bannerResId = R.drawable.post_3,
                profilePictureResId = R.drawable.profile_3,
                username = "spill_the_tea",
                displayName = "Spill Tea",
                followerCount = 680,
                followingCount = 120,
                description = "Life's messy, but so am I. Finding humor and growth in everyday spills and triumphs."
            ),
            User(
                id = "user_19",
                bannerResId = R.drawable.post_4,
                profilePictureResId = R.drawable.profile_4,
                username = "ramen.rover",
                displayName = "Ramen Rover",
                followerCount = 200,
                followingCount = 30,
                description = "On a perpetual quest for the perfect bowl of ramen.🍜 Food adventures and travel."
            ),
            User(
                id = "user_20",
                bannerResId = R.drawable.post_5,
                profilePictureResId = R.drawable.profile_5,
                username = "trial.and.error",
                displayName = "Trial Error",
                followerCount = 550,
                followingCount = 90,
                description = "Embracing the journey of learning and improvement. Every mistake is a lesson."
            ),
            User(
                id = "user_21",
                bannerResId = R.drawable.post_6,
                profilePictureResId = R.drawable.profile_6,
                username = "serendipity.soul",
                displayName = "Serendipity Soul",
                followerCount = 900,
                followingCount = 160,
                description = "Believing in happy accidents and unexpected joys. Life's best moments are often unplanned."
            ),
            User(
                id = "user_22",
                bannerResId = R.drawable.post_7,
                profilePictureResId = R.drawable.profile_7,
                username = "wandering.mind",
                displayName = "Wandering Mind",
                followerCount = 780,
                followingCount = 130,
                description = "Lost in thought, found in nature. A journey of introspection and discovery."
            ),
            User(
                id = "user_23",
                bannerResId = R.drawable.post_8,
                profilePictureResId = R.drawable.profile_8,
                username = "quiet.echo",
                displayName = "Quiet Echo",
                followerCount = 100,
                followingCount = 25,
                description = "Finding solace in silence and the echoes of deep thought. Introvert by nature."
            ),
            User(
                id = "user_24",
                bannerResId = R.drawable.post_9,
                profilePictureResId = R.drawable.profile_9,
                username = "summit.chaser",
                displayName = "Summit Chaser",
                followerCount = 620,
                followingCount = 115,
                description = "Always climbing higher, literally and figuratively. The view from the top is always worth it. 🏔️"
            ),
            User(
                id = "user_25",
                bannerResId = R.drawable.post_10,
                profilePictureResId = R.drawable.profile_10,
                username = "neon.dreamer",
                displayName = "Neon Dreamer",
                followerCount = 480,
                followingCount = 85,
                description = "Lost in the glow of city nights and retro dreams. Chasing neon horizons."
            ),
            User(
                id = "user_26",
                bannerResId = R.drawable.post_11,
                profilePictureResId = R.drawable.profile_11,
                username = "whisper.softly",
                displayName = "Whisper Softly",
                followerCount = 280,
                followingCount = 45,
                description = "Listening to the quiet voices and finding strength in vulnerability."
            ),
            User(
                id = "user_27",
                bannerResId = R.drawable.post_12,
                profilePictureResId = R.drawable.profile_12,
                username = "inked.words",
                displayName = "Inked Words",
                followerCount = 250,
                followingCount = 40,
                description = "A storyteller by heart, weaving tales with inked words. Writing my own adventure."
            ),
            User(
                id = "user_28",
                bannerResId = R.drawable.post_13,
                profilePictureResId = R.drawable.profile_13,
                username = "lunar.vibes",
                displayName = "Lunar Vibes",
                followerCount = 950,
                followingCount = 170,
                description = "Guided by the moon, living by intuition. Embracing cosmic energies."
            ),
            User(
                id = "user_29",
                bannerResId = R.drawable.post_14,
                profilePictureResId = R.drawable.profile_14,
                username = "caffeinated.life",
                displayName = "Caffeinated Life",
                followerCount = 300,
                followingCount = 65,
                description = "Powered by coffee and dreams. My daily dose of inspiration and productivity."
            ),
            User(
                id = "user_30",
                bannerResId = R.drawable.post_15,
                profilePictureResId = R.drawable.profile_15,
                username = "wander.lusty",
                displayName = "Wander Lusty",
                followerCount = 1100,
                followingCount = 200,
                description = "A soul born to wander. Always chasing horizons and new experiences. 🌍"
            )
        )

    private val usersById = dummyUsers.associateBy { it.id }

    fun getUser(userId: String): User? = usersById[userId]
}
