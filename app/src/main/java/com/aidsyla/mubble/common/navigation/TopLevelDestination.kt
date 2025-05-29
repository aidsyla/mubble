package com.aidsyla.mubble.common.navigation

import com.aidsyla.mubble.ui.theme.MubbleTheme
import kotlin.reflect.KClass

enum class TopLevelDestination(
    val route: KClass<*>,
    val selectedIcon: Int,
    val unselectedIcon: Int,
    val iconText: String,
    val titleText: String
) {
    HOME(
        route = HomeRoute::class,
        selectedIcon = MubbleTheme.Icons.HomeSelected,
        unselectedIcon = MubbleTheme.Icons.Home,
        iconText = "Home",
        titleText = "mubble"
    ),
    EXPLORE(
        route = ExploreRoute::class,
        selectedIcon = MubbleTheme.Icons.SearchSelected,
        unselectedIcon = MubbleTheme.Icons.Search,
        iconText = "Explore",
        titleText = "Explore"
    ),
    CHATS(
        route = ChatListRoute::class,
        selectedIcon = MubbleTheme.Icons.ChatSelected,
        unselectedIcon = MubbleTheme.Icons.Chat,
        iconText = "Chats",
        titleText = "Chats"
    ),
    ACTIVITY(
        route = ActivityRoute::class,
        selectedIcon = MubbleTheme.Icons.ActivitySelected,
        unselectedIcon = MubbleTheme.Icons.Activity,
        iconText = "Activity",
        titleText = "Activity"
    ),
    PROFILE(
        route = ProfileRoute::class,
        selectedIcon = MubbleTheme.Icons.ProfileSelected,
        unselectedIcon = MubbleTheme.Icons.Profile,
        iconText = "Profile",
        titleText = "Profile"
    )
}