package com.mobi.ripple.core.di

import com.mobi.ripple.core.data.data_source.remote.followers_following.FollowersFollowingApiService
import com.mobi.ripple.core.data.data_source.remote.followers_following.FollowersFollowingApiServiceImpl
import com.mobi.ripple.core.data.repository.FollowersFollowingRepositoryImpl
import com.mobi.ripple.core.domain.repository.FollowersFollowingRepository
import com.mobi.ripple.core.domain.use_case.followers_following.FollowersFollowingUseCases
import com.mobi.ripple.core.domain.use_case.followers_following.GetFollowersUseCase
import com.mobi.ripple.core.domain.use_case.followers_following.GetFollowingUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import io.ktor.client.HttpClient
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object FollowersFollowingModule {

    @Provides
    @Singleton
    fun provideFollowersFollowingApiService(client: HttpClient): FollowersFollowingApiService {
        return FollowersFollowingApiServiceImpl(client)
    }

    @Provides
    @Singleton
    fun provideFollowersFollowingRepository(
        apiService: FollowersFollowingApiService
    ): FollowersFollowingRepository {
        return FollowersFollowingRepositoryImpl(apiService)
    }

    @Provides
    @Singleton
    fun provideFollowersFollowingUseCases(
        repository: FollowersFollowingRepository
    ): FollowersFollowingUseCases {
        return FollowersFollowingUseCases(
            GetFollowersUseCase(repository),
            GetFollowingUseCase(repository)
        )
    }
}