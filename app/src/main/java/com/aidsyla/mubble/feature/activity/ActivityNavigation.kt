package com.aidsyla.mubble.feature.activity

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.*
import androidx.navigation.compose.*
import com.aidsyla.mubble.common.navigation.ActivityRoute

fun NavController.navigateToActivity(navOptions: NavOptions) = navigate(route = ActivityRoute, navOptions)

fun NavGraphBuilder.activityScreen() {
    composable<ActivityRoute> {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Text("Activity Screen")
        }
    }
}