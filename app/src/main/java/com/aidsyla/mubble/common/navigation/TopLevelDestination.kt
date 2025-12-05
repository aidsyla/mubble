package com.aidsyla.mubble.common.navigation

import com.aidsyla.mubble.ui.theme.MubbleTheme
import kotlin.reflect.KClass

enum class TopLevelDestination(
    val route: KClass<*>,
    val selectedIcon: Int,
    val unselectedIcon: Int,
    val iconText: String,
) {
    HOME(
        route = HomeRoute::class,
        selectedIcon = MubbleTheme.TopLevelDestinationIcons.HomeSelected,
        unselectedIcon = MubbleTheme.TopLevelDestinationIcons.Home,
        iconText = "Home",
    ),
    EXPLORE(
        route = ExploreRoute::class,
        selectedIcon = MubbleTheme.TopLevelDestinationIcons.ExploreSelected,
        unselectedIcon = MubbleTheme.TopLevelDestinationIcons.Explore,
        iconText = "Explore",
    ),
    VIDEOS(
        route = VideosRoute::class,
        selectedIcon = MubbleTheme.TopLevelDestinationIcons.VideosSelected,
        unselectedIcon = MubbleTheme.TopLevelDestinationIcons.Videos,
        iconText = "Videos",
    ),
    CHATS(
        route = ChatListRoute::class,
        selectedIcon = MubbleTheme.TopLevelDestinationIcons.ChatSelected,
        unselectedIcon = MubbleTheme.TopLevelDestinationIcons.Chat,
        iconText = "Chats",
    ),
    PROFILE(
        route = ProfileRoute::class,
        selectedIcon = MubbleTheme.TopLevelDestinationIcons.ProfileSelected,
        unselectedIcon = MubbleTheme.TopLevelDestinationIcons.Profile,
        iconText = "Profile",
    ),
}
