package com.mobi.ripple.feature_app.feature_profile.domain.repository

import com.mobi.ripple.core.domain.Response

interface SettingsRepository {
    suspend fun logoutAllDevices(): Response<Boolean?>
}