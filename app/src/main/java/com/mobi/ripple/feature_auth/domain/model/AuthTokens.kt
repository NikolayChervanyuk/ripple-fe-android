package com.mobi.ripple.feature_auth.domain.model

import kotlinx.serialization.Serializable

@Serializable
open class AuthTokens(
    open val accessToken: String,
    open val refreshToken: String
) {
    suspend fun extractUsernameFromTokens() {

    }
}