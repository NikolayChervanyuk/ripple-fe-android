package com.mobi.ripple.feature_auth.presentation.register

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import com.mobi.ripple.feature_auth.presentation.register.model.UserRegisterModel

data class RegisterState(
    val userRegister: UserRegisterModel =
        UserRegisterModel("", "", "", "", ""),
    var showFullNameInvalidError: MutableState<Boolean> = mutableStateOf(false),
    var isUsernameInvalid: MutableState<Boolean> = mutableStateOf(false),
    var isUsernameTaken: MutableState<Boolean> = mutableStateOf(false),
    var showUsernameTaken: MutableState<Boolean> = mutableStateOf(false),
    var isEmailTaken: MutableState<Boolean> = mutableStateOf(false),
    var showEmailInvalidError: MutableState<Boolean> = mutableStateOf(false),

    var showPasswordsNotMatchingError: MutableState<Boolean> = mutableStateOf(false),
    var isPasswordInvalid: MutableState<Boolean> = mutableStateOf(false),
    var hasConfirmPasswordBeenChanged: MutableState<Boolean> = mutableStateOf(false),

    var showRegistrationError: MutableState<Boolean> = mutableStateOf(false),
    var registrationErrorMessage: MutableState<String> = mutableStateOf("")
)