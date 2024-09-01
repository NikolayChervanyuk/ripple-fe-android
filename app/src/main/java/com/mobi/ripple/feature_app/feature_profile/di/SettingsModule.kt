package com.mobi.ripple.feature_app.feature_profile.di

import com.mobi.ripple.RootAppManager
import com.mobi.ripple.feature_app.feature_profile.data.data_source.remote.SettingsApiService
import com.mobi.ripple.feature_app.feature_profile.data.data_source.remote.SettingsApiServiceImpl
import com.mobi.ripple.feature_app.feature_profile.data.repository.SettingsRepositoryImpl
import com.mobi.ripple.feature_app.feature_profile.domain.repository.ProfileRepository
import com.mobi.ripple.feature_app.feature_profile.domain.repository.SettingsRepository
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
object SettingsModule {

    @Provides
    @Singleton
    fun provideSettingsApiService(client: HttpClient): SettingsApiService {
        return SettingsApiServiceImpl(client)
    }

    @Provides
    @Singleton
    fun provideSettingsRepository(settingsApiService: SettingsApiService): SettingsRepository {
        return SettingsRepositoryImpl(settingsApiService)
    }

    @Provides
    @Singleton
    fun provideSettingsUseCases(repository: SettingsRepository, rootManager: RootAppManager): SettingsUseCases {
        return SettingsUseCases(
            logoutAllDevicesUsesCase = LogoutAllDevicesUsesCase(repository)
        )
    }
}