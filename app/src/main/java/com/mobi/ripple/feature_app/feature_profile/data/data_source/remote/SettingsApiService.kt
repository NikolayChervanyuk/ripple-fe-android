package com.mobi.ripple.feature_app.feature_profile.data.data_source.remote

import com.mobi.ripple.core.data.data_source.remote.wrappers.ApiResponse

interface SettingsApiService {
    suspend fun logoutAllDevices(): ApiResponse<Boolean>
}