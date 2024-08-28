package com.mobi.ripple.feature_auth.presentation.register.components

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun PasswordNotMatchingText(modifier: Modifier = Modifier, show: Boolean) {
    InvalidFieldMessage(show = show, message = "Passwords do not match")
}