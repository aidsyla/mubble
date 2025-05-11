package com.aidsyla.mubble.feature.profile

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import com.aidsyla.mubble.common.navigation.OtherProfileRoute
import com.aidsyla.mubble.common.navigation.ProfileRoute

fun NavController.navigateToOtherProfile(
    username: String,
    navOptions: NavOptions? = null,
) {
    this.navigate(OtherProfileRoute(username = username), navOptions)
}

fun NavController.navigateToProfile(
    navOptions: NavOptions? = null,
) {
    this.navigate(ProfileRoute, navOptions)
}

fun NavGraphBuilder.profileScreen(
    onNavigateToSettings: () -> Unit,
) {
    composable<ProfileRoute> {
        ProfileScreen(onNavigateToSettings = onNavigateToSettings)
    }
}

fun NavGraphBuilder.otherProfileScreen(

) {
    composable<OtherProfileRoute> {
        ProfileScreen { }
    }
}