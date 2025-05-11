package com.aidsyla.mubble.common.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.ui.graphics.vector.ImageVector
import kotlin.reflect.KClass

enum class TopLevelDestination(
    val route: KClass<*>,
    val selectedIcon: ImageVector,
    val unselectedIcon: ImageVector,
    val iconText: String,
    val titleText: String
) {
    HOME(
        route = HomeRoute::class,
        selectedIcon = Icons.Filled.Home,
        unselectedIcon = Icons.Outlined.Home,
        iconText = "Home",
        titleText = "mubble"
    ),
    EXPLORE(
        route = ExploreRoute::class,
        selectedIcon = Icons.Filled.Search,
        unselectedIcon = Icons.Outlined.Search,
        iconText = "Explore",
        titleText = "Explore"
    ),
    CHATS(
        route = ChatListRoute::class,
        selectedIcon = Icons.Filled.Email,
        unselectedIcon = Icons.Outlined.Email,
        iconText = "Chats",
        titleText = "Chats"
    ),
    ACTIVITY(
        route = ActivityRoute::class,
        selectedIcon = Icons.Filled.Notifications,
        unselectedIcon = Icons.Outlined.Notifications,
        iconText = "Activity",
        titleText = "Activity"
    ),
    PROFILE(
        route = ProfileRoute::class,
        selectedIcon = Icons.Filled.AccountCircle,
        unselectedIcon = Icons.Outlined.AccountCircle,
        iconText = "Profile",
        titleText = "Profile"
    )
}