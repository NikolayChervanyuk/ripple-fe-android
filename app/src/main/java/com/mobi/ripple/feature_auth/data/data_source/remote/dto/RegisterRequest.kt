package com.mobi.ripple.feature_auth.data.data_source.remote.dto

import com.mobi.ripple.feature_auth.domain.model.UserRegister
import kotlinx.serialization.Serializable

@Serializable
data class RegisterRequest(
    val fullName: String,
    val username: String,
    val email: String,
    val password: String
)

fun UserRegister.asRegisterRequest() = RegisterRequest(
    fullName = fullName,
    username = username,
    email = email,
    password = password
)
