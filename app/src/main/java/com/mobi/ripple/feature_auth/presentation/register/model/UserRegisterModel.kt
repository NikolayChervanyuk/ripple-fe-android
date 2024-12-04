package com.mobi.ripple.feature_auth.presentation.register.model

import com.mobi.ripple.feature_auth.domain.model.UserLogin
import com.mobi.ripple.feature_auth.domain.model.UserRegister

data class UserRegisterModel(
    var fullName: String,
    var username: String,
    var email: String,
    var password: String,
    var confirmPassword: String
) {

    fun asUserRegister() = UserRegister(
        fullName, username, email, password
    )

    fun asUserLogin() = UserLogin(
        username, password
    )
}