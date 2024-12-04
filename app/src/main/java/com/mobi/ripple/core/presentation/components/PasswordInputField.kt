package com.mobi.ripple.core.presentation.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandVertically
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.relocation.BringIntoViewRequester
import androidx.compose.foundation.relocation.bringIntoViewRequester
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.mobi.ripple.R
import com.mobi.ripple.core.theme.RippleTheme
import com.mobi.ripple.core.theme.SuccessGreen
import com.mobi.ripple.core.util.validator.PasswordValidator
import com.mobi.ripple.core.util.validator.PasswordValidator.Companion.PasswordRequirements
import com.mobi.ripple.feature_auth.presentation.register.components.PasswordNotMatchingText
import kotlinx.coroutines.launch

@Composable
fun PasswordTextInput(
    modifier: Modifier = Modifier,
    placeholder: String = stringResource(R.string.password),
    onTextChanged: (String) -> Unit,
    keyboardOptions: KeyboardOptions,
    shouldShowPasswordRequirement: Boolean = false,
    showPasswordsNotMatching: Boolean = false
) {

    val passwordVisibility = remember {
        mutableStateOf(false)
    }
    val isFocused = rememberSaveable { mutableStateOf(false) }
    val hasBeenFocused = rememberSaveable { mutableStateOf(false) }
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.Start
    ) {
        val passwordText = rememberSaveable { mutableStateOf("") }
        RippleInputField(
            modifier = Modifier.onFocusChanged {
                isFocused.value = it.isFocused
                if (it.isFocused) hasBeenFocused.value = true
            },
            onTextChanged = {
                passwordText.value = it
                onTextChanged(it)

            },
            keyboardOptions = keyboardOptions,//KeyboardOptions(keyboardType = KeyboardType.Password),
            visualTransformation = if (passwordVisibility.value) VisualTransformation.None else PasswordVisualTransformation(),
            leadingIcon = { StarPasswordIcon() },
            trailingIcon = {
                IconButton(
                    modifier = Modifier.size(24.dp),
                    onClick = { passwordVisibility.value = !passwordVisibility.value }
                ) {
                    if (passwordVisibility.value) {
                        VisibilityOnIcon()
                    } else {
                        VisibilityOffIcon()
                    }
                }
            },
            placeholder = placeholder
        )
        if (shouldShowPasswordRequirement) {
            PasswordRequirementsText(
                isFocused = isFocused.value,
                hasEnoughLength = PasswordValidator.hasLength(passwordText.value),
                hasLowerCase = PasswordValidator.hasLowerCase(passwordText.value),
                hasUpperCase = PasswordValidator.hasUpperCase(passwordText.value),
                hasDigit = PasswordValidator.hasDigit(passwordText.value)
            )
        }
        PasswordNotMatchingText(show = showPasswordsNotMatching)
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun PasswordRequirementsText(
    isFocused: Boolean,
    hasEnoughLength: Boolean,
    hasLowerCase: Boolean,
    hasUpperCase: Boolean,
    hasDigit: Boolean
) {
    val bringIntoViewRequester = remember { BringIntoViewRequester() }
    val coroutineScope = rememberCoroutineScope()
    AnimatedVisibility(

        visible = isFocused,
        enter = expandVertically(
            expandFrom = Alignment.Top,
            animationSpec = tween(200)
        ),
        exit = shrinkVertically(
            shrinkTowards = Alignment.Top,
            animationSpec = tween(200)
        )
    ) {
        Column(
            Modifier
                .fillMaxWidth()
                .padding(top = 10.dp)
                .bringIntoViewRequester(bringIntoViewRequester)
                .onSizeChanged {
                    coroutineScope.launch {
                        if (isFocused) {
                            bringIntoViewRequester.bringIntoView()
                        }
                    }
                }

        ) {
            Text(
                text = "Password should have:",
                style = MaterialTheme.typography.labelLarge,
                color = MaterialTheme.colorScheme.onBackground
            )
            Column(Modifier.padding(start = 10.dp, top = 5.dp)) {
                HasLengthRow(hasEnoughLength = hasEnoughLength)
                HasLowerCaseRow(hasLowerCase = hasLowerCase)
                HasUpperCaseRow(hasUpperCase = hasUpperCase)
                HasDigitCaseRow(hasDigit = hasDigit)

            }
        }
    }
}

@Composable
private fun HasLengthRow(hasEnoughLength: Boolean) {
    Row(
        modifier = Modifier.padding(bottom = 2.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        if (hasEnoughLength) CheckmarkIcon()
        else CrossIcon()

        Spacer(modifier = Modifier.width(5.dp))
        Text(
            text = "At least ${PasswordRequirements.MIN_CHAR_LENGTH} characters",
            style = MaterialTheme.typography.labelSmall,
            color = if (hasEnoughLength) SuccessGreen
            else MaterialTheme.colorScheme.error
        )
    }
}

@Composable
private fun HasLowerCaseRow(hasLowerCase: Boolean) {
    Row(
        modifier = Modifier.padding(bottom = 2.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        if (hasLowerCase) CheckmarkIcon()
        else CrossIcon()

        Spacer(modifier = Modifier.width(5.dp))
        Text(
            text = "At least 1 lowercase letter",
            style = MaterialTheme.typography.labelSmall,
            color = if (hasLowerCase) SuccessGreen
            else MaterialTheme.colorScheme.error
        )
    }
}

@Composable
private fun HasUpperCaseRow(hasUpperCase: Boolean) {
    Row(
        modifier = Modifier.padding(bottom = 2.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        if (hasUpperCase) CheckmarkIcon()
        else CrossIcon()

        Spacer(modifier = Modifier.width(5.dp))
        Text(
            text = "At least 1 uppercase letter",
            style = MaterialTheme.typography.labelSmall,
            color = if (hasUpperCase) SuccessGreen
            else MaterialTheme.colorScheme.error
        )
    }
}

@Composable
private fun HasDigitCaseRow(hasDigit: Boolean) {
    Row(
        modifier = Modifier.padding(bottom = 2.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        if (hasDigit) CheckmarkIcon()
        else CrossIcon()

        Spacer(modifier = Modifier.width(5.dp))
        Text(
            text = "At least 1 digit",
            style = MaterialTheme.typography.labelSmall,
            color = if (hasDigit) SuccessGreen
            else MaterialTheme.colorScheme.error
        )
    }
}

@Composable
private fun VisibilityOffIcon(modifier: Modifier = Modifier) {
    Icon(
        modifier = modifier
            .size(21.dp),
        painter = painterResource(id = R.drawable.closed_eye_icon),
        contentDescription = "Visibility off icon",
        tint = MaterialTheme.colorScheme.onSurfaceVariant
    )
}

@Composable
private fun VisibilityOnIcon(modifier: Modifier = Modifier) {
    Icon(
        modifier = modifier
            .size(21.dp),
        painter = painterResource(id = R.drawable.opened_eye_icon),
        contentDescription = "Visibility on icon",
        tint = MaterialTheme.colorScheme.onSurfaceVariant
    )
}

@Composable
private fun StarPasswordIcon(modifier: Modifier = Modifier) {
    Icon(
        modifier = modifier
            .size(19.dp),
        painter = painterResource(id = R.drawable.asterisk_sign_icon),
        contentDescription = "Visibility off icon",
        tint = MaterialTheme.colorScheme.onSurfaceVariant
    )
}

@Preview
@Composable
private fun PasswordInputFieldPreview() {
    RippleTheme {
        PasswordTextInput(onTextChanged = {}, keyboardOptions = KeyboardOptions())
    }
}

@Preview
@Composable
private fun PasswordRequirementsTextPreview() {
    RippleTheme {
        Surface {
            PasswordRequirementsText(
                isFocused = true,
                hasEnoughLength = true,
                hasLowerCase = false,
                hasUpperCase = false,
                hasDigit = true
            )
        }
    }
}