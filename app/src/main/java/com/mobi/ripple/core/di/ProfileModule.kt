package com.mobi.ripple.core.di

import com.mobi.ripple.core.data.data_source.local.AppDatabase
import com.mobi.ripple.core.data.data_source.remote.profile.ProfileApiService
import com.mobi.ripple.core.data.data_source.remote.profile.ProfileApiServiceImpl
import com.mobi.ripple.core.data.repository.ProfileRepositoryImpl
import com.mobi.ripple.core.domain.repository.ProfileRepository
import com.mobi.ripple.core.domain.use_case.profile.ChangeFollowingStateUseCase
import com.mobi.ripple.core.domain.use_case.profile.GetProfileInfoUseCase
import com.mobi.ripple.core.domain.use_case.profile.GetProfilePictureUseCase
import com.mobi.ripple.core.domain.use_case.profile.GetSimplePostsFlowUseCase
import com.mobi.ripple.core.domain.use_case.profile.ProfileUseCases
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import io.ktor.client.HttpClient
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object ProfileModule {

    @Provides
    @Singleton
    fun provideProfileApiService(client: HttpClient): ProfileApiService {
        return ProfileApiServiceImpl(client)
    }

    @Provides
    @Singleton
    fun provideProfileRepository(
        apiService: ProfileApiService,
        database: AppDatabase
    ): ProfileRepository {
        return ProfileRepositoryImpl(apiService, database)
    }

    @Provides
    @Singleton
    fun provideProfileUseCases(repository: ProfileRepository): ProfileUseCases {
        return ProfileUseCases(
            getProfileInfoUseCase = GetProfileInfoUseCase(repository),
            getProfilePictureUseCase = GetProfilePictureUseCase(repository),
            getSimplePostsFlowUseCase = GetSimplePostsFlowUseCase(repository),
            changeFollowingStateUseCase = ChangeFollowingStateUseCase(repository)
        )
    }
}