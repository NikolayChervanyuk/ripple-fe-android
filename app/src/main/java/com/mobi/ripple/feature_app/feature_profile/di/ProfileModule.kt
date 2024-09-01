package com.mobi.ripple.feature_app.feature_profile.di

import com.mobi.ripple.RootAppManager
import com.mobi.ripple.feature_app.feature_profile.data.data_source.remote.ProfileApiService
import com.mobi.ripple.feature_app.feature_profile.data.data_source.remote.ProfileApiServiceImpl
import com.mobi.ripple.feature_app.feature_profile.data.repository.ProfileRepositoryImpl
import com.mobi.ripple.feature_app.feature_profile.domain.repository.ProfileRepository
import com.mobi.ripple.feature_app.feature_profile.domain.use_case.profile.DeletePfpUseCase
import com.mobi.ripple.feature_app.feature_profile.domain.use_case.profile.EditProfileInfoUseCase
import com.mobi.ripple.feature_app.feature_profile.domain.use_case.profile.GetProfileInfoUseCase
import com.mobi.ripple.feature_app.feature_profile.domain.use_case.profile.GetProfilePictureUseCase
import com.mobi.ripple.feature_app.feature_profile.domain.use_case.profile.GetSimplePostsUseCase
import com.mobi.ripple.feature_app.feature_profile.domain.use_case.profile.IsOtherUserWithEmailExistsUseCase
import com.mobi.ripple.feature_app.feature_profile.domain.use_case.profile.IsOtherUserWithUsernameExistsUseCase
import com.mobi.ripple.feature_app.feature_profile.domain.use_case.profile.ProfileUseCases
import com.mobi.ripple.feature_app.feature_profile.domain.use_case.profile.UploadPfpUseCase
import com.mobi.ripple.feature_app.feature_profile.domain.use_case.settings.LogoutAllDevicesUsesCase
import com.mobi.ripple.feature_app.feature_profile.domain.use_case.settings.SettingsUseCases
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
            uploadPfpUseCase = UploadPfpUseCase(repository),
            deletePfpUseCase = DeletePfpUseCase(repository),
            getSimplePostsUseCase = GetSimplePostsUseCase(repository),
            getProfileInfoUseCase = GetProfileInfoUseCase(repository),
            isOtherUserWithUsernameExists = IsOtherUserWithUsernameExistsUseCase(repository),
            isOtherUserWithEmailExists = IsOtherUserWithEmailExistsUseCase(repository),
            editProfileInfoUseCase = EditProfileInfoUseCase(repository)
        )
    }
}