package com.mobi.ripple.feature_auth.presentation.register.components

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.mobi.ripple.core.presentation.InvalidFieldMessage

@Composable
fun RegistrationErrorText(modifier: Modifier = Modifier, show: Boolean, message: String) {
    InvalidFieldMessage(show = show, message = message)
}