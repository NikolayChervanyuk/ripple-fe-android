package com.mobi.ripple.feature_app.feature_profile.di

import com.mobi.ripple.core.data.common.AppDatabase
import com.mobi.ripple.core.domain.profile.use_case.GetProfileInfoUseCase
import com.mobi.ripple.core.domain.profile.use_case.GetProfilePictureUseCase
import com.mobi.ripple.core.domain.profile.use_case.GetSimplePostsFlowUseCase
import com.mobi.ripple.feature_app.feature_profile.data.data_source.remote.PersonalProfileApiService
import com.mobi.ripple.feature_app.feature_profile.data.data_source.remote.PersonalProfileApiServiceImpl
import com.mobi.ripple.feature_app.feature_profile.data.repository.PersonalProfileRepositoryImpl
import com.mobi.ripple.feature_app.feature_profile.domain.repository.PersonalProfileRepository
import com.mobi.ripple.feature_app.feature_profile.domain.use_case.profile.DeletePfpUseCase
import com.mobi.ripple.feature_app.feature_profile.domain.use_case.profile.EditProfileInfoUseCase
import com.mobi.ripple.feature_app.feature_profile.domain.use_case.profile.IsOtherUserWithEmailExistsUseCase
import com.mobi.ripple.feature_app.feature_profile.domain.use_case.profile.IsOtherUserWithUsernameExistsUseCase
import com.mobi.ripple.feature_app.feature_profile.domain.use_case.profile.PersonalProfileUseCases
import com.mobi.ripple.feature_app.feature_profile.domain.use_case.profile.UploadPfpUseCase
import com.mobi.ripple.feature_app.feature_profile.domain.use_case.profile.UploadPostUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import io.ktor.client.HttpClient
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object PersonalProfileModule {

    @Provides
    @Singleton
    fun providePersonalProfileApiService(client: HttpClient): PersonalProfileApiService {
        return PersonalProfileApiServiceImpl(client)
    }


    @Provides
    @Singleton
    fun providePersonalProfileRepository(
        personalProfileApiService: PersonalProfileApiService,
        database: AppDatabase
    ): PersonalProfileRepository {
        return PersonalProfileRepositoryImpl(personalProfileApiService, database)
    }

    @Provides
    @Singleton
    fun provideProfileUseCases(repository: PersonalProfileRepository): PersonalProfileUseCases {
        return PersonalProfileUseCases(
            getProfilePictureUseCase = GetProfilePictureUseCase(repository),
            uploadPfpUseCase = UploadPfpUseCase(repository),
            deletePfpUseCase = DeletePfpUseCase(repository),
            getSimplePostsFlowUseCase = GetSimplePostsFlowUseCase(repository),
            getProfileInfoUseCase = GetProfileInfoUseCase(repository),
            isOtherUserWithUsernameExists = IsOtherUserWithUsernameExistsUseCase(repository),
            isOtherUserWithEmailExists = IsOtherUserWithEmailExistsUseCase(repository),
            editProfileInfoUseCase = EditProfileInfoUseCase(repository),
            uploadPostUseCase = UploadPostUseCase(repository)
        )
    }
}