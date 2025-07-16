package com.aidsyla.mubble.di

import com.aidsyla.mubble.data.ChatRepo
import com.aidsyla.mubble.data.UserDataRepository
import com.aidsyla.mubble.data.UserDataRepositoryImpl
import com.aidsyla.mubble.data.UserRepo
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    abstract fun bindUserDataRepository(
        userDataRepository: UserDataRepositoryImpl,
    ): UserDataRepository

    companion object {
        @Provides
        @Singleton
        fun provideUserRepo(): UserRepo {
            return UserRepo
        }

        @Provides
        @Singleton
        fun provideChatRepo(): ChatRepo {
            return ChatRepo
        }
    }
}