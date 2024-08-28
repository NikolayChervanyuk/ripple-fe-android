package com.mobi.ripple.feature_auth.presentation.register

sealed class RegisterEvent {
    data object BackButtonPressed: RegisterEvent()
    data class FullNameChanged(val newText: String) : RegisterEvent()
    data class UsernameChanged(val newText: String) : RegisterEvent()
    data class EmailChanged(val newText: String) : RegisterEvent()
    data class PasswordChanged(val newText: String) : RegisterEvent()
    data class ConfirmPasswordChanged(val newText: String) : RegisterEvent()
    data object RegisterButtonClicked : RegisterEvent()
}