package com.mobi.ripple.feature_auth.domain.model

data class UserRegister(
    val fullName: String,
    val username: String,
    val email: String,
    val password: String
)
