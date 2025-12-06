package com.aidsyla.mubble.common.navigation

import com.aidsyla.mubble.ui.theme.MubbleDesignSystem
import kotlin.reflect.KClass

enum class TopLevelDestination(
    val route: KClass<*>,
    val selectedIcon: Int,
    val unselectedIcon: Int,
    val iconText: String
) {
    HOME(
        route = HomeRoute::class,
        selectedIcon = MubbleDesignSystem.TopLevelDestinationIcons.HomeSelected,
        unselectedIcon = MubbleDesignSystem.TopLevelDestinationIcons.Home,
        iconText = "Home"
    ),
    EXPLORE(
        route = ExploreRoute::class,
        selectedIcon = MubbleDesignSystem.TopLevelDestinationIcons.ExploreSelected,
        unselectedIcon = MubbleDesignSystem.TopLevelDestinationIcons.Explore,
        iconText = "Explore"
    ),
    VIDEOS(
        route = VideosRoute::class,
        selectedIcon = MubbleDesignSystem.TopLevelDestinationIcons.VideosSelected,
        unselectedIcon = MubbleDesignSystem.TopLevelDestinationIcons.Videos,
        iconText = "Videos"
    ),
    CHATS(
        route = ChatListRoute::class,
        selectedIcon = MubbleDesignSystem.TopLevelDestinationIcons.ChatSelected,
        unselectedIcon = MubbleDesignSystem.TopLevelDestinationIcons.Chat,
        iconText = "Chats"
    ),
    PROFILE(
        route = ProfileRoute::class,
        selectedIcon = MubbleDesignSystem.TopLevelDestinationIcons.ProfileSelected,
        unselectedIcon = MubbleDesignSystem.TopLevelDestinationIcons.Profile,
        iconText = "Profile"
    )
}
