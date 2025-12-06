package com.aidsyla.mubble.feature.circle.model

import androidx.annotation.DrawableRes
import com.aidsyla.mubble.R
import java.util.UUID

data class Circle(
    val id: String = UUID.randomUUID().toString(),
    val name: String,
    val memberCount: Int,
    @param:DrawableRes
    val bannerResId: Int
)

object CircleRepo {
    val dummyCircles =
        listOf(
            Circle(
                name = "Nature",
                memberCount = 1254,
                bannerResId = R.drawable.circle_1
            ),
            Circle(
                name = "Art",
                memberCount = 843,
                bannerResId = R.drawable.circle_2
            ),
            Circle(
                name = "Photography",
                memberCount = 1920,
                bannerResId = R.drawable.circle_3
            ),
            Circle(
                name = "Food",
                memberCount = 1478,
                bannerResId = R.drawable.circle_4
            ),
            Circle(
                name = "Cars",
                memberCount = 1083,
                bannerResId = R.drawable.circle_5
            ),
            Circle(
                name = "Travel",
                memberCount = 2034,
                bannerResId = R.drawable.circle_6
            )
        )
}
