package com.mobi.ripple.feature_auth.data.data_source.remote.dto

data class RefreshTokenResponse(
    val accessToken: String,
    val refreshToken: String
)
