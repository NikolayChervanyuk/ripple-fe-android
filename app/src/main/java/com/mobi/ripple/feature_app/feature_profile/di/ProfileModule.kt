package com.mobi.ripple.feature_app.feature_profile.di

import com.mobi.ripple.feature_app.feature_profile.data.data_source.remote.ProfileApiService
import com.mobi.ripple.feature_app.feature_profile.data.data_source.remote.ProfileApiServiceImpl
import com.mobi.ripple.feature_app.feature_profile.data.repository.ProfileRepositoryImpl
//import com.mobi.ripple.feature_app.feature_profile.data.repository.ProfileRepositoryImpl
import com.mobi.ripple.feature_app.feature_profile.domain.repository.ProfileRepository
import com.mobi.ripple.feature_app.feature_profile.domain.use_case.GetProfileInfoUseCase
import com.mobi.ripple.feature_app.feature_profile.domain.use_case.GetProfilePictureUseCase
import com.mobi.ripple.feature_app.feature_profile.domain.use_case.GetSimplePostsUseCase
import com.mobi.ripple.feature_app.feature_profile.domain.use_case.ProfileUseCases
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
    fun provideProfileRepository(profileApiService: ProfileApiService): ProfileRepository {
        return ProfileRepositoryImpl(profileApiService)
    }

    @Provides
    @Singleton
    fun providesProfileUseCases(repository: ProfileRepository): ProfileUseCases {
        return ProfileUseCases(
            getProfilePictureUseCase = GetProfilePictureUseCase(repository),
            getProfileInfoUseCase = GetProfileInfoUseCase(repository),
            getSimplePostsUseCase = GetSimplePostsUseCase(repository)
        )
    }
}