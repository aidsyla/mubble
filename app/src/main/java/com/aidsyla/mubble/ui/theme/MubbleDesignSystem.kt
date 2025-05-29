package com.aidsyla.mubble.ui.theme

import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material.icons.rounded.Settings
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.painterResource
import com.aidsyla.mubble.R
import androidx.compose.material.icons.Icons as MaterialIcons

object MubbleTheme {

    object ProfileTabs {
        val iconsSelected @Composable get() = listOf(Icons.PostsSelected, Icons.BubblesSelected)
        val icons @Composable get() = listOf(Icons.Posts, Icons.Bubbles)
    }

    object AppearanceTabs {
        val iconsSelected @Composable get() = listOf(Icons.AutoSelected, Icons.DarkModeSelected, Icons.LightModeSelected)
        val icons @Composable get() = listOf(Icons.Auto, Icons.DarkMode, Icons.LightMode)
    }

    object AccountVisibility {
        val iconsSelected @Composable get() = listOf(Icons.AccountPublicSelected, Icons.AccountPrivateSelected)
        val icons @Composable get() = listOf(Icons.AccountPublic, Icons.AccountPrivate)
    }

    object Icons {
        val ArrowBack = MaterialIcons.AutoMirrored.Rounded.ArrowBack
        val EditFilled @Composable get() = painterResource(R.drawable.edit_filled)
        val Edit @Composable get() = painterResource(R.drawable.edit)
        val Settings = MaterialIcons.Rounded.Settings

        val HomeSelected = R.drawable.home_filled
        val Home = R.drawable.home

        val SearchSelected = R.drawable.search_filled
        val Search = R.drawable.search

        val ChatSelected = R.drawable.chat_bubble_filled
        val Chat = R.drawable.chat_bubble

        val ActivitySelected = R.drawable.notifications_filled
        val Activity = R.drawable.notifications

        val ProfileSelected = R.drawable.account_circle_filled
        val Profile = R.drawable.account_circle


        val AppearanceFilled @Composable get() = painterResource(id = R.drawable.routine_filled)
        val Appearance @Composable get() = painterResource(id = R.drawable.routine)

        val AutoSelected @Composable get() = painterResource(id = R.drawable.brightness_auto_filled)
        val Auto @Composable get() = painterResource(id = R.drawable.brightness_auto)

        val DarkModeSelected @Composable get() = painterResource(id = R.drawable.dark_mode_filled)
        val DarkMode @Composable get() = painterResource(id = R.drawable.dark_mode)

        val LightModeSelected @Composable get() = painterResource(id = R.drawable.light_mode_filled)
        val LightMode @Composable get() = painterResource(id = R.drawable.light_mode)


        val NotificationFilled @Composable get() = painterResource(R.drawable.notifications_filled)
        val Notification @Composable get() = painterResource(R.drawable.notifications)


        val DevicePermissions @Composable get() = painterResource(R.drawable.perm_device_information)

        val PhotoLibrary @Composable get() = painterResource(R.drawable.photo_library)
        val Camera @Composable get() = painterResource(R.drawable.photo_camera)


        val ManageAccount @Composable get() = painterResource(R.drawable.person)
        val AccountVisibility @Composable get() = painterResource(R.drawable.visibility)

        val AccountPublicSelected @Composable get() = painterResource(R.drawable.public_filled)
        val AccountPublic @Composable get() = painterResource(R.drawable.public_)

        val AccountPrivateSelected @Composable get() = painterResource(R.drawable.lock_filled)
        val AccountPrivate @Composable get() = painterResource(R.drawable.lock)

        val Delete @Composable get() = painterResource(R.drawable.delete)


        val Logout @Composable get() = painterResource(R.drawable.logout)


        val PersonAdd @Composable get() = painterResource(id = R.drawable.person_add_filled)
        val Message @Composable get() = painterResource(id = R.drawable.chat_filled)

        val PostsSelected @Composable get() = painterResource(id = R.drawable.grid_view_filled)
        val Posts @Composable get() = painterResource(id = R.drawable.grid_view)

        val BubblesSelected @Composable get() = painterResource(id = R.drawable.table_rows_filled)
        val Bubbles @Composable get() = painterResource(id = R.drawable.table_rows)
    }
}