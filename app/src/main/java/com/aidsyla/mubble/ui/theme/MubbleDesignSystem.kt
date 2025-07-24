package com.aidsyla.mubble.ui.theme

import androidx.compose.material.icons.rounded.Settings
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import com.aidsyla.mubble.R
import androidx.compose.material.icons.Icons as MaterialIcons

object MubbleTheme {

    object ProfileTabs {
        val iconsSelected @Composable get() = listOf(Icons.PostsSelected, Icons.BubblesSelected)
        val icons @Composable get() = listOf(Icons.Posts, Icons.Bubbles)
    }

    object AppearanceTabs {
        val iconsSelected
            @Composable get() = listOf(
                Icons.AutoSelected,
                Icons.DarkModeSelected,
                Icons.LightModeSelected
            )
        val icons @Composable get() = listOf(Icons.Auto, Icons.DarkMode, Icons.LightMode)
    }

    object AccountVisibility {
        val iconsSelected
            @Composable get() = listOf(
                Icons.AccountPublicSelected,
                Icons.AccountPrivateSelected
            )
        val icons @Composable get() = listOf(Icons.AccountPublic, Icons.AccountPrivate)
    }

    object TopLevelDestinationIcons {
        val HomeSelected = R.drawable.home_filled
        val Home = R.drawable.home

        val ExploreSelected = R.drawable.globe_filled
        val Explore = R.drawable.globe

        val VideosSelected = R.drawable.slow_motion_video
        val Videos = VideosSelected

        val ChatSelected = R.drawable.chat_filled
        val Chat = R.drawable.chat

        val ActivitySelected = R.drawable.notifications_filled
        val Activity = R.drawable.notifications

        val ProfileSelected = R.drawable.account_circle_filled
        val Profile = R.drawable.account_circle
    }

    object Icons {
        val MubbleIcon @Composable get() = painterResource(id = R.drawable.mubble_icon)

        val Pause @Composable get() = painterResource(id = R.drawable.pause)
        val Resume @Composable get() = painterResource(id = R.drawable.resume)
        val VolumeUp @Composable get() = painterResource(id = R.drawable.volume_up)
        val VolumeOff @Composable get() = painterResource(id = R.drawable.no_sound)
        val Speed0_5x @Composable get() = painterResource(id = R.drawable.speed_0_5x)
        val Speed1x @Composable get() = painterResource(id = R.drawable.speed_1x)
        val Speed2x @Composable get() = painterResource(id = R.drawable.speed_2x)
        val ExpandContent @Composable get() = painterResource(id = R.drawable.expand_content)
        val CollapseContent @Composable get() = painterResource(id = R.drawable.collapse_content)

        val MoreHorizontal @Composable get() = painterResource(id = R.drawable.more_horiz)
        val Sort @Composable get() = painterResource(id = R.drawable.sort)
        val Check @Composable get() = painterResource(id = R.drawable.check)

        val Reply @Composable get() = painterResource(id = R.drawable.reply)

        val Favorite @Composable get() = painterResource(id = R.drawable.favorite)
        val FavoriteFilled @Composable get() = painterResource(id = R.drawable.favorite_filled)
        val Comment @Composable get() = painterResource(id = R.drawable.comment)
        val Send @Composable get() = painterResource(id = R.drawable.send)
        val Save @Composable get() = painterResource(id = R.drawable.bookmark)

        val ArrowBack @Composable get() = painterResource(id = R.drawable.arrow_back)
        val Close @Composable get() = painterResource(id = R.drawable.close)
        val Search @Composable get() = painterResource(id = R.drawable.search)
        val EditFilled @Composable get() = painterResource(id = R.drawable.edit_filled)
        val Edit @Composable get() = painterResource(id = R.drawable.edit)
        val Settings = MaterialIcons.Rounded.Settings

        val AppearanceFilled @Composable get() = painterResource(id = R.drawable.routine_filled)
        val Appearance @Composable get() = painterResource(id = R.drawable.routine)

        val AutoSelected @Composable get() = painterResource(id = R.drawable.brightness_auto_filled)
        val Auto @Composable get() = painterResource(id = R.drawable.brightness_auto)

        val DarkModeSelected @Composable get() = painterResource(id = R.drawable.dark_mode_filled)
        val DarkMode @Composable get() = painterResource(id = R.drawable.dark_mode)

        val LightModeSelected @Composable get() = painterResource(id = R.drawable.light_mode_filled)
        val LightMode @Composable get() = painterResource(id = R.drawable.light_mode)


        val NotificationFilled @Composable get() = painterResource(id = R.drawable.notifications_filled)
        val Notification @Composable get() = painterResource(id = R.drawable.notifications)
        val NotificationsOff @Composable get() = painterResource(id = R.drawable.notifications_off)


        val DevicePermissions @Composable get() = painterResource(id = R.drawable.perm_device_information)


        val PhotoLibrary @Composable get() = painterResource(id = R.drawable.photo_library)
        val Camera @Composable get() = painterResource(id = R.drawable.photo_camera)


        val ManageAccount @Composable get() = painterResource(id = R.drawable.person)
        val AccountVisibility @Composable get() = painterResource(id = R.drawable.visibility)

        val AccountPublicSelected @Composable get() = painterResource(id = R.drawable.public_filled)
        val AccountPublic @Composable get() = painterResource(id = R.drawable.public_)

        val AccountPrivateSelected @Composable get() = painterResource(id = R.drawable.lock_filled)
        val AccountPrivate @Composable get() = painterResource(id = R.drawable.lock)

        val Delete @Composable get() = painterResource(id = R.drawable.delete)


        val Logout @Composable get() = painterResource(id = R.drawable.logout)


        val PersonAdd @Composable get() = painterResource(id = R.drawable.person_add_filled)
        val Message @Composable get() = painterResource(id = R.drawable.chat_filled)

        val PostsSelected @Composable get() = painterResource(id = R.drawable.grid_view_filled)
        val Posts @Composable get() = painterResource(id = R.drawable.grid_view)

        val BubblesSelected @Composable get() = painterResource(id = R.drawable.table_rows_filled)
        val Bubbles @Composable get() = painterResource(id = R.drawable.table_rows)

        val InCircle @Composable get() = painterResource(id = R.drawable.prompt_suggestion)
    }

    object Gradients {
        val fadingBlackGradient = Brush.verticalGradient(
            colorStops = arrayOf(
                0.0f to Color(0x80000000),
                0.05f to Color(0x73000000),
                0.10f to Color(0x66000000),
                0.15f to Color(0x57000000),
                0.20f to Color(0x47000000),
                0.25f to Color(0x38000000),
                0.30f to Color(0x29000000),
                0.35f to Color(0x1A000000),
                0.40f to Color(0x0F000000),
                0.50f to Color(0x08000000),
                0.60f to Color(0x03000000),
            )
        )

        val captionScrimGradient = Brush.verticalGradient(
            colorStops = arrayOf(
                0.0f to Color(0x80000000),
                0.10f to Color(0x73000000),
                0.15f to Color(0x66000000),
                0.22f to Color(0x57000000),
                0.28f to Color(0x47000000),
                0.33f to Color(0x38000000),
                0.40f to Color(0x29000000),
                0.45f to Color(0x1A000000),
                0.50f to Color(0x08000000),
                0.60f to Color(0x03000000),
            )
        )

        val fadingBlackGradientReversed = Brush.verticalGradient(
            startY = Float.POSITIVE_INFINITY,
            endY = 0f,
            colorStops = arrayOf(
                0.00f to Color(0xCC000000),
                0.08f to Color(0xBF000000),
                0.13f to Color(0xAD000000),
                0.18f to Color(0x97000000),
                0.23f to Color(0x78000000),
                0.28f to Color(0x55000000),
                0.33f to Color(0x40000000),
                0.38f to Color(0x2E000000),
                0.43f to Color(0x1A000000),
                0.50f to Color(0x0F000000),
                0.70f to Color(0x00000000)
            )
        )
    }
}