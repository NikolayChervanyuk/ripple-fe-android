package com.mobi.ripple.feature_auth.presentation.login

sealed class LoginEvent {
    data class IdentifierTextChanged(val newText: String) : LoginEvent()
    data class PasswordTextChanged(val newText: String) : LoginEvent()
    data object Login : LoginEvent()
    data object PasswordChange : LoginEvent()
    data object SignUp : LoginEvent()
}