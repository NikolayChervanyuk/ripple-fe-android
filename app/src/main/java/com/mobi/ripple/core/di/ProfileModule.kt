package com.mobi.ripple.core.di

import com.mobi.ripple.core.data.common.AppDatabase
import com.mobi.ripple.core.data.profile.data_source.remote.ProfileApiService
import com.mobi.ripple.core.data.profile.data_source.remote.ProfileApiServiceImpl
import com.mobi.ripple.core.data.profile.repository.ProfileRepositoryImpl
import com.mobi.ripple.core.domain.profile.repository.ProfileRepository
import com.mobi.ripple.core.domain.profile.use_case.ChangeFollowingStateUseCase
import com.mobi.ripple.core.domain.profile.use_case.GetProfileInfoUseCase
import com.mobi.ripple.core.domain.profile.use_case.GetProfilePictureUseCase
import com.mobi.ripple.core.domain.profile.use_case.GetSimplePostsFlowUseCase
import com.mobi.ripple.core.domain.profile.use_case.ProfileUseCases
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