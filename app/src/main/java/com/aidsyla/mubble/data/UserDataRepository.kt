package com.aidsyla.mubble.data

import com.aidsyla.mubble.model.DarkThemeConfig
import kotlinx.coroutines.flow.Flow

data class UserData(
    val darkThemeConfig: DarkThemeConfig,
)

interface UserDataRepository {

    val userData: Flow<UserData>

    suspend fun setDarkThemeConfig(darkThemeConfig: DarkThemeConfig)

}