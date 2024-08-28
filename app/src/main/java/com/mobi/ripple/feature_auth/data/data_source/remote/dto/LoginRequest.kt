package com.mobi.ripple.feature_auth.data.data_source.remote.dto

import com.mobi.ripple.feature_auth.domain.model.UserLogin
import kotlinx.serialization.Serializable

@Serializable
data class LoginRequest(
    val identifier: String,
    val password: String
)

fun UserLogin.asLoginRequest() = LoginRequest(
    identifier = identifier,
    password = password
)
