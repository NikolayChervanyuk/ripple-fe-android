package com.mobi.ripple.feature_app.feature_profile.data.repository

import com.mobi.ripple.core.domain.common.Response
import com.mobi.ripple.feature_app.feature_profile.data.data_source.remote.SettingsApiService
import com.mobi.ripple.feature_app.feature_profile.domain.repository.SettingsRepository

class SettingsRepositoryImpl(
    private val settingsApiService: SettingsApiService
) : SettingsRepository {
    override suspend fun logoutAllDevices(): Response<Boolean?> {
        val apiResponse = settingsApiService.logoutAllDevices()

        return apiResponse.toResponse(apiResponse.content)
    }

}