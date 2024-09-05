package com.mobi.ripple.feature_app.feature_profile.domain.use_case.settings

import com.mobi.ripple.core.domain.model.Response
import com.mobi.ripple.feature_app.feature_profile.domain.repository.SettingsRepository

class LogoutAllDevicesUsesCase(
    private val repository: SettingsRepository
) {
    suspend operator fun invoke(): Response<Boolean?> {
        return repository.logoutAllDevices()
    }
}