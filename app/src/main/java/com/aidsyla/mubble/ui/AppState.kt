package com.aidsyla.mubble.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.navigation.NavDestination
import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navOptions
import com.aidsyla.mubble.common.navigation.ProfileRoute
import com.aidsyla.mubble.common.navigation.TopLevelDestination
import com.aidsyla.mubble.common.navigation.TopLevelDestination.ACTIVITY
import com.aidsyla.mubble.common.navigation.TopLevelDestination.CHATS
import com.aidsyla.mubble.common.navigation.TopLevelDestination.EXPLORE
import com.aidsyla.mubble.common.navigation.TopLevelDestination.HOME
import com.aidsyla.mubble.common.navigation.TopLevelDestination.PROFILE
import com.aidsyla.mubble.feature.activity.navigateToActivity
import com.aidsyla.mubble.feature.chats.navigateToChatList
import com.aidsyla.mubble.feature.explore.navigateToExplore
import com.aidsyla.mubble.feature.home.navigateToHome
import com.aidsyla.mubble.feature.profile.navigateToProfile

@Stable
class AppState(
    val navController: NavHostController,
) {
    private val previousDestination = mutableStateOf<NavDestination?>(null)

    val currentDestination: NavDestination?
        @Composable get() {
            val currentEntry = navController.currentBackStackEntryFlow
                .collectAsState(initial = null)

            return currentEntry.value?.destination.also { destination ->
                if (destination != null) {
                    previousDestination.value = destination
                }
            } ?: previousDestination.value
        }

    val currentTopLevelDestination: TopLevelDestination?
        @Composable get() {
            return TopLevelDestination.entries.firstOrNull { topLevelDestination ->
                currentDestination?.hasRoute(route = topLevelDestination.route) == true
            }
        }

    val topLevelDestinations: List<TopLevelDestination> = TopLevelDestination.entries

    fun navigateToTopLevelDestination(topLevelDestination: TopLevelDestination) {
        val topLevelNavOptions = navOptions {
            popUpTo(navController.graph.findStartDestination().id) {
                saveState = true
            }
            launchSingleTop = true
            restoreState = true
        }

        when (topLevelDestination) {
            HOME -> navController.navigateToHome(topLevelNavOptions)
            EXPLORE -> navController.navigateToExplore(topLevelNavOptions)
            CHATS -> navController.navigateToChatList(topLevelNavOptions)
            ACTIVITY -> navController.navigateToActivity(topLevelNavOptions)
            PROFILE -> navController.navigateToProfile(topLevelNavOptions)
        }
    }

    fun navigateToProfileFromSettings() {
        navController.navigate(ProfileRoute) {
            popUpTo(ProfileRoute::class.qualifiedName!!) {
                saveState = false
            }
            launchSingleTop = true
        }
    }
}

@Composable
fun rememberAppState(
    navController: NavHostController = rememberNavController(),
): AppState {
    return remember(navController) {
        AppState(navController)
    }
}