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
        selectedIcon = MubbleTheme.TopLevelDestinationIcons.HomeSelected,
        unselectedIcon = MubbleTheme.TopLevelDestinationIcons.Home,
        iconText = "Home",
        titleText = "mubble"
    ),
    EXPLORE(
        route = ExploreRoute::class,
        selectedIcon = MubbleTheme.TopLevelDestinationIcons.SearchSelected,
        unselectedIcon = MubbleTheme.TopLevelDestinationIcons.Search,
        iconText = "Explore",
        titleText = "Explore"
    ),
    CHATS(
        route = ChatListRoute::class,
        selectedIcon = MubbleTheme.TopLevelDestinationIcons.ChatSelected,
        unselectedIcon = MubbleTheme.TopLevelDestinationIcons.Chat,
        iconText = "Chats",
        titleText = "Chats"
    ),
    ACTIVITY(
        route = ActivityRoute::class,
        selectedIcon = MubbleTheme.TopLevelDestinationIcons.ActivitySelected,
        unselectedIcon = MubbleTheme.TopLevelDestinationIcons.Activity,
        iconText = "Activity",
        titleText = "Activity"
    ),
    PROFILE(
        route = ProfileRoute::class,
        selectedIcon = MubbleTheme.TopLevelDestinationIcons.ProfileSelected,
        unselectedIcon = MubbleTheme.TopLevelDestinationIcons.Profile,
        iconText = "Profile",
        titleText = "Profile"
    )
}