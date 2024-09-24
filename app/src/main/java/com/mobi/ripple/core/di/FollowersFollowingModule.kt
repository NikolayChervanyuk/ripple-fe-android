package com.mobi.ripple.core.di

import com.mobi.ripple.core.data.followers_following.data_source.remote.FollowersFollowingApiService
import com.mobi.ripple.core.data.followers_following.data_source.remote.FollowersFollowingApiServiceImpl
import com.mobi.ripple.core.data.followers_following.repository.FollowersFollowingRepositoryImpl
import com.mobi.ripple.core.domain.followers_following.repository.FollowersFollowingRepository
import com.mobi.ripple.core.domain.followers_following.use_case.FollowersFollowingUseCases
import com.mobi.ripple.core.domain.followers_following.use_case.GetFollowersUseCase
import com.mobi.ripple.core.domain.followers_following.use_case.GetFollowingUseCase
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