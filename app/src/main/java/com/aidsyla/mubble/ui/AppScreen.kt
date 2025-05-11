package com.aidsyla.mubble.ui

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.MutableTransitionState
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavDestination
import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.navigation.NavDestination.Companion.hierarchy
import com.aidsyla.mubble.common.navigation.AppNavHost
import com.aidsyla.mubble.common.navigation.HomeRoute
import com.aidsyla.mubble.common.navigation.SettingsDevicePermissionsRoute
import com.aidsyla.mubble.common.navigation.SettingsManageAccountRoute
import com.aidsyla.mubble.common.navigation.SettingsNotificationsRoute
import com.aidsyla.mubble.common.navigation.SettingsStartRoute
import com.aidsyla.mubble.common.navigation.TopLevelDestination
import kotlin.reflect.KClass

val LocalBottomBarPadding = staticCompositionLocalOf { 0.dp }

@Composable
fun AppScreen(
) {
    val appState = rememberAppState()

    val currentDestination = appState.currentDestination
    val currentTopLevelDestination = appState.currentTopLevelDestination

    val showNavBar = currentTopLevelDestination != null || currentDestination.isRouteInSettingsHierarchy()

    val navBarVisibilityState = remember {
        MutableTransitionState(initialState = true)
    }

    LaunchedEffect(showNavBar) {
        navBarVisibilityState.targetState = showNavBar
    }

    Scaffold(
        bottomBar = {
            AnimatedVisibility(
                visibleState = navBarVisibilityState,
                enter = fadeIn(animationSpec = tween(700)),
                exit = fadeOut(animationSpec = tween(700))
            ) {
                NavigationBar {
                    appState.topLevelDestinations.forEach { destination ->
                        val selected = when (destination) {
                            TopLevelDestination.PROFILE -> {
                                currentDestination.isRouteInHierarchy(TopLevelDestination.PROFILE.route) ||
                                        currentDestination.isRouteInSettingsHierarchy()
                            }

                            else -> {
                                currentDestination.isRouteInHierarchy(destination.route)
                            }
                        }
                        NavigationBarItem(
                            selected = selected,
                            onClick = {
                                if (destination == TopLevelDestination.PROFILE && currentDestination.isRouteInSettingsHierarchy()) {
                                    appState.navigateToProfileFromSettings()
                                } else {
                                    appState.navigateToTopLevelDestination(destination)
                                }
                            },
                            icon = {
                                val icon =
                                    if (selected) destination.selectedIcon else destination.unselectedIcon
                                Icon(icon, contentDescription = destination.iconText)
                            },
                            label = { Text(destination.iconText) },
                            alwaysShowLabel = true
                        )
                    }
                }
            }
        }
    ) {
        val bottomPadding = it.calculateBottomPadding()
        CompositionLocalProvider(LocalBottomBarPadding provides bottomPadding) {
            AppNavHost(
                modifier = Modifier,
                appState = appState,
                startDestination = HomeRoute
            )
        }
    }
}

private fun NavDestination?.isRouteInSettingsHierarchy(): Boolean {
    val settingsRoutes = setOf(
        SettingsStartRoute::class.qualifiedName,
        SettingsNotificationsRoute::class.qualifiedName,
        SettingsDevicePermissionsRoute::class.qualifiedName,
        SettingsManageAccountRoute::class.qualifiedName
    )
    return this?.hierarchy?.any { navDest -> navDest.route in settingsRoutes } ?: false
}

private fun NavDestination?.isRouteInHierarchy(route: KClass<*>) =
    this?.hierarchy?.any {
        it.hasRoute(route)
    } ?: false
