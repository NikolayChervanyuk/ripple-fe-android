package com.mobi.ripple.feature_auth.presentation.login

import com.mobi.ripple.feature_auth.presentation.login.model.UserLoginModel

data class LoginState(
    val user: UserLoginModel = UserLoginModel("", ""),
)