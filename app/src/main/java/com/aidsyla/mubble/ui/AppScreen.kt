package com.aidsyla.mubble.ui

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.MutableTransitionState
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavDestination
import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.navigation.NavDestination.Companion.hierarchy
import com.aidsyla.mubble.common.components.SubtleHorizontalDivider
import com.aidsyla.mubble.common.navigation.AppNavHost
import com.aidsyla.mubble.common.navigation.FollowScreenRoute
import com.aidsyla.mubble.common.navigation.HomeRoute
import com.aidsyla.mubble.common.navigation.SettingsDevicePermissionsRoute
import com.aidsyla.mubble.common.navigation.SettingsManageAccountRoute
import com.aidsyla.mubble.common.navigation.SettingsNotificationsRoute
import com.aidsyla.mubble.common.navigation.SettingsStartRoute
import com.aidsyla.mubble.common.navigation.TopLevelDestination
import kotlin.reflect.KClass

val LocalBottomBarPadding = compositionLocalOf { 0.dp }

@Composable
fun AppScreen() {
    val appState = rememberAppState()
    val currentDestination = appState.currentDestination
    val currentTopLevelDestination = appState.currentTopLevelDestination

    val isVideoScreen = currentDestination.isRouteInHierarchy(TopLevelDestination.VIDEOS.route)

    val showNavBar =
        (
            currentTopLevelDestination != null ||
                currentDestination.isRouteInSettingsHierarchy() ||
                currentDestination.isRouteInHierarchy(
                    FollowScreenRoute::class,
                )
        ) &&
            !isVideoScreen

    val navBarVisibilityState =
        remember {
            MutableTransitionState(initialState = true)
        }

    LaunchedEffect(showNavBar) {
        navBarVisibilityState.targetState = showNavBar
    }

    Scaffold(
        bottomBar = {
            AnimatedVisibility(
                visibleState = navBarVisibilityState,
                enter = fadeIn(animationSpec = tween(300)),
                exit = fadeOut(animationSpec = tween(300)),
            ) {
                Box {
                    NavigationBar(
                        modifier = Modifier.height(80.dp),
                        containerColor = MaterialTheme.colorScheme.surfaceContainerLow,
                    ) {
                        appState.topLevelDestinations.forEach { destination ->
                            val selected =
                                when (destination) {
                                    TopLevelDestination.PROFILE -> {
                                        currentDestination.isRouteInHierarchy(TopLevelDestination.PROFILE.route) ||
                                            currentDestination.isRouteInSettingsHierarchy() ||
                                            currentDestination.isRouteInHierarchy(
                                                FollowScreenRoute::class,
                                            )
                                    }

                                    else -> {
                                        currentDestination.isRouteInHierarchy(destination.route)
                                    }
                                }
                            NavigationBarItem(
                                selected = selected,
                                onClick = {
                                    if (destination == TopLevelDestination.PROFILE &&
                                        (
                                            currentDestination.isRouteInSettingsHierarchy() ||
                                                currentDestination.isRouteInHierarchy(
                                                    FollowScreenRoute::class,
                                                )
                                        )
                                    ) {
                                        appState.navigateToProfileFromSettings()
                                    } else {
                                        appState.navigateToTopLevelDestination(destination)
                                    }
                                },
                                icon = {
                                    val icon =
                                        if (selected) destination.selectedIcon else destination.unselectedIcon
                                    Icon(
                                        painterResource(icon),
                                        contentDescription = destination.iconText,
                                    )
                                },
                            )
                        }
                    }
                    SubtleHorizontalDivider(
                        modifier = Modifier.align(Alignment.TopCenter),
                    )
                }
            }
        },
    ) {
        val bottomPadding = it.calculateBottomPadding()
        CompositionLocalProvider(LocalBottomBarPadding provides bottomPadding) {
            AppNavHost(
                modifier = Modifier,
                appState = appState,
                startDestination = HomeRoute,
            )
        }
    }
}

private fun NavDestination?.isRouteInSettingsHierarchy(): Boolean {
    val settingsRoutes =
        setOf(
            SettingsStartRoute::class.qualifiedName,
            SettingsNotificationsRoute::class.qualifiedName,
            SettingsDevicePermissionsRoute::class.qualifiedName,
            SettingsManageAccountRoute::class.qualifiedName,
        )
    return this?.hierarchy?.any { navDest -> navDest.route in settingsRoutes } ?: false
}

private fun NavDestination?.isRouteInHierarchy(route: KClass<*>) =
    this?.hierarchy?.any {
        it.hasRoute(route)
    } ?: false
