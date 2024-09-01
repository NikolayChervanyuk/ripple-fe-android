package com.mobi.ripple.feature_auth.presentation.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.mobi.ripple.R
import com.mobi.ripple.core.presentation.InvalidFieldMessage
import com.mobi.ripple.core.presentation.RippleInputField

@Composable
fun EmailTextField(
    modifier: Modifier = Modifier,
    onTextChanged: (String) -> Unit,
    showInvalidMessage: Boolean = false, //TODO: show if email is taken
    showEmailTakenMessage: Boolean = false
) {
    val emailText = rememberSaveable { mutableStateOf("") }
    Column(modifier = modifier) {
        RippleInputField(
            leadingIcon = { MailIcon() },
            placeholder = stringResource(R.string.email),
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Ascii,
                imeAction = ImeAction.Next
            ),
            onTextChanged = {
                emailText.value = it
                onTextChanged(it)
            }
        )

        if (!showInvalidMessage) {
            EmailTakenMessage(show = showEmailTakenMessage, emailText = emailText.value)
        }
        InvalidEmailMessage(show = showInvalidMessage)
    }
}

@Composable
private fun InvalidEmailMessage(modifier: Modifier = Modifier, show: Boolean) {
    InvalidFieldMessage(
        modifier = modifier,
        show = show,
        message = "Invalid email"
    )
}

@Composable
private fun EmailTakenMessage(modifier: Modifier = Modifier, show: Boolean, emailText: String) {
    InvalidFieldMessage(
        modifier = modifier,
        show = show,
        message = "$emailText already exists"
    )
}

@Composable
private fun MailIcon() {
    Icon(
        modifier = Modifier
            .padding(1.dp)
            .size(21.dp),
        painter = painterResource(id = R.drawable.envelope_line_icon),
        contentDescription = "E-mail icon",
        tint = MaterialTheme.colorScheme.onSurfaceVariant
    )
}