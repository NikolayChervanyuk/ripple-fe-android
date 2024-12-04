package com.mobi.ripple.feature_app.feature_profile.data.data_source.remote

import com.mobi.ripple.core.config.AppUrls
import com.mobi.ripple.core.data.common.data_source.wrappers.ApiRequest
import com.mobi.ripple.core.data.common.data_source.wrappers.ApiResponse
import io.ktor.client.HttpClient
import io.ktor.client.request.post

class SettingsApiServiceImpl(
    private val client: HttpClient
) : SettingsApiService {
    override suspend fun logoutAllDevices(): ApiResponse<Boolean> =
        ApiRequest<Boolean> {
            client.post(AppUrls.AuthUrls.LOGOUT_URL)
        }.sendRequest()
}