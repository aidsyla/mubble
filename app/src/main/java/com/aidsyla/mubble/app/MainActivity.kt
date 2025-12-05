package com.aidsyla.mubble.app

import android.graphics.Color
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.ProvidableCompositionLocal
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.aidsyla.mubble.feature.settings.SettingsUiState
import com.aidsyla.mubble.feature.settings.SettingsViewModel
import com.aidsyla.mubble.model.DarkThemeConfig
import com.aidsyla.mubble.ui.AppScreen
import com.aidsyla.mubble.ui.theme.MubbleTheme
import com.aidsyla.mubble.util.isSystemInDarkThemeFlow
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.launch

val LocalDarkTheme: ProvidableCompositionLocal<Boolean> =
    compositionLocalOf { error("No LocalDarkTheme provided") }

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    private val viewModel: SettingsViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        var darkTheme by mutableStateOf(false)

        lifecycleScope.launch {
            lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                combine(
                    isSystemInDarkThemeFlow(),
                    viewModel.settingsUiState,
                ) { systemIsDark, uiState ->
                    when (uiState) {
                        SettingsUiState.Loading -> systemIsDark
                        is SettingsUiState.Success ->
                            when (uiState.darkThemeConfig) {
                                DarkThemeConfig.FOLLOW_SYSTEM -> systemIsDark
                                DarkThemeConfig.LIGHT -> false
                                DarkThemeConfig.DARK -> true
                            }
                    }
                }.distinctUntilChanged()
                    .collect { useDarkTheme ->
                        darkTheme = useDarkTheme
                        enableEdgeToEdge(
                            statusBarStyle =
                                SystemBarStyle.auto(
                                    Color.TRANSPARENT,
                                    Color.TRANSPARENT,
                                ) { useDarkTheme },
                            navigationBarStyle =
                                SystemBarStyle.auto(
                                    lightScrim,
                                    darkScrim,
                                ) { useDarkTheme },
                        )
                    }
            }
        }

        setContent {
            CompositionLocalProvider(LocalDarkTheme provides darkTheme) {
                MubbleTheme(darkTheme = darkTheme) {
                    AppScreen()
                }
            }
        }
    }
}

/**
 * The default light scrim, as defined by androidx and the platform:
 * https://cs.android.com/androidx/platform/frameworks/support/+/androidx-main:activity/activity/src/main/java/androidx/activity/EdgeToEdge.kt;l=35-38;drc=27e7d52e8604a080133e8b842db10c89b4482598
 */
private val lightScrim = Color.argb(0xe6, 0xFF, 0xFF, 0xFF)

/**
 * The default dark scrim, as defined by androidx and the platform:
 * https://cs.android.com/androidx/platform/frameworks/support/+/androidx-main:activity/activity/src/main/java/androidx/activity/EdgeToEdge.kt;l=40-44;drc=27e7d52e8604a080133e8b842db10c89b4482598
 */
private val darkScrim = Color.argb(0x80, 0x1b, 0x1b, 0x1b)
