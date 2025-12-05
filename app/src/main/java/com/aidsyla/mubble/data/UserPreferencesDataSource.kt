package com.aidsyla.mubble.data

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import com.aidsyla.mubble.model.DarkThemeConfig
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

private val DARK_THEME_CONFIG = stringPreferencesKey("dark_theme_config")

class UserPreferencesDataSource
    @Inject
    constructor(
        private val dataStore: DataStore<Preferences>,
    ) {
        val darkThemeConfig: Flow<DarkThemeConfig> =
            dataStore.data
                .map { preferences ->
                    val configName = preferences[DARK_THEME_CONFIG] ?: DarkThemeConfig.FOLLOW_SYSTEM.name
                    DarkThemeConfig.valueOf(configName)
                }

        suspend fun setDarkThemeConfig(darkThemeConfig: DarkThemeConfig) {
            dataStore.edit { preferences ->
                preferences[DARK_THEME_CONFIG] = darkThemeConfig.name
            }
        }
    }
