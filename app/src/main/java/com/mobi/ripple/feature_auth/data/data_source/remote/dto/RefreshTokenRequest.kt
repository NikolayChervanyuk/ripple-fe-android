package com.mobi.ripple.feature_auth.data.data_source.remote.dto

import kotlinx.serialization.Serializable

@Serializable
data class RefreshTokenRequest(
    val refreshToken: String
)