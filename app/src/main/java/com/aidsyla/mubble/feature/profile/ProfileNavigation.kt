package com.aidsyla.mubble.feature.profile

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import com.aidsyla.mubble.common.navigation.ProfileRoute

fun NavController.navigateToProfile(
    userId: String? = null,
    navOptions: NavOptions? = null,
) {
    this.navigate(ProfileRoute(userId = userId), navOptions)
}

fun NavGraphBuilder.profileScreen(
    onNavigateToSettings: () -> Unit,
    onBackClick: () -> Unit,
) {
    composable<ProfileRoute> {
        ProfileScreen(
            onNavigateToSettings = onNavigateToSettings,
            onBackClick = onBackClick
        )
    }
}