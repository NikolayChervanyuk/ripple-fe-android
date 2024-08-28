package com.mobi.ripple.feature_auth.presentation.login.model

import com.mobi.ripple.feature_auth.domain.model.UserLogin

data class UserLoginModel(
    var identifier: String,
    var password: String
){

    fun asUserLogin() = UserLogin(
        identifier,
        password
    )
}
