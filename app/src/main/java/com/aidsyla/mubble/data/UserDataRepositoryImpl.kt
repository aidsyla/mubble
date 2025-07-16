package com.aidsyla.mubble.data

import com.aidsyla.mubble.model.DarkThemeConfig
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class UserDataRepositoryImpl @Inject constructor(
    private val userPreferencesDataSource: UserPreferencesDataSource,
) : UserDataRepository {

    override val userData: Flow<UserData> =
        userPreferencesDataSource.darkThemeConfig.map { config ->
            UserData(darkThemeConfig = config)
        }

    override suspend fun setDarkThemeConfig(darkThemeConfig: DarkThemeConfig) {
        userPreferencesDataSource.setDarkThemeConfig(darkThemeConfig)
    }
}