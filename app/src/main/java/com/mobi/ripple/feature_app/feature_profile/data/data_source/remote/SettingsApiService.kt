package com.mobi.ripple.feature_app.feature_profile.data.data_source.remote

import com.mobi.ripple.core.data.ApiResponse

interface SettingsApiService {
    suspend fun logoutAllDevices(): ApiResponse<Boolean>
}