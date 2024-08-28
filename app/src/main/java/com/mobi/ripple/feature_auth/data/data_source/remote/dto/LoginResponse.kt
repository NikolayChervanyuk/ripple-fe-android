package com.mobi.ripple.feature_auth.data.data_source.remote.dto

import com.mobi.ripple.feature_auth.domain.model.AuthTokens
import kotlinx.serialization.Serializable

@Serializable
data class LoginResponse(
    val accessToken: String,
    val refreshToken: String
) {

    fun asAuthTokens(): AuthTokens {
        return AuthTokens(accessToken, refreshToken)
    }
}