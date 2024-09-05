package com.mobi.ripple.core.presentation.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandVertically
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.mobi.ripple.R
import com.mobi.ripple.core.theme.RippleTheme
import com.mobi.ripple.core.theme.SuccessGreen
import com.mobi.ripple.core.util.validator.FieldValidator
import com.mobi.ripple.core.util.validator.UsernameValidator

@Composable
fun UsernameTextField(
    modifier: Modifier = Modifier,
    onTextChanged: (String) -> Unit,
    isUsernameTaken: Boolean,
    showUsernameTaken: Boolean,
    text: String = "",
    placeholder: String = stringResource(R.string.username),
    leadingIcon: @Composable (() -> Unit)? = { UserIcon() }
) {

    val isFocused = rememberSaveable { mutableStateOf(false) }
    val hasBeenFocused = rememberSaveable { mutableStateOf(false) }
    val username = rememberSaveable { mutableStateOf("") }
    Column(
        modifier = modifier.fillMaxWidth()
    ) {
        RippleInputField(
            modifier = Modifier
                .onFocusChanged {
                    isFocused.value = it.isFocused
                    if (it.isFocused) hasBeenFocused.value = true
                },
            leadingIcon = leadingIcon,
            placeholder = placeholder,
            text = text,
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Ascii,
                imeAction = ImeAction.Next
            ),
            onTextChanged = {
                onTextChanged(it)
                username.value = it
            }
        )
        Column(Modifier.fillMaxWidth()) {

            IsUsernameTakenRow(
                isFocused = isFocused.value,
                isUsernameTaken = isUsernameTaken,
                username = username.value,
                show = showUsernameTaken
            )

            UsernameRequirementsText(
                isFocused = isFocused.value,
                hasEnoughLength = UsernameValidator.hasLength(username.value),
                hasAllowedCharactersOnly = UsernameValidator.hasAllowedCharacterOnly(username.value),
                hasNoConsequentSeparators = UsernameValidator.hasNoConsequentSeparators(username.value),
                notStartOrEndWithComma = UsernameValidator.notStartOrEndWithComma(username.value)
            )
        }
    }
}

@Composable
private fun UsernameRequirementsText(
    isFocused: Boolean,
    hasEnoughLength: Boolean,
    hasAllowedCharactersOnly: Boolean,
    hasNoConsequentSeparators: Boolean,
    notStartOrEndWithComma: Boolean
) {
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
                .padding(top = 10.dp),
        ) {
            Text(
                text = "Username should:",
                style = MaterialTheme.typography.labelLarge,
                color = MaterialTheme.colorScheme.onBackground
            )
            Column(Modifier.padding(start = 10.dp, top = 5.dp)) {
                HasLengthRow(hasEnoughLength = hasEnoughLength)
                HasAllowedCharactersOnlyRow(hasAllowedCharactersOnly = hasAllowedCharactersOnly)
                HasNoConsequentSeparatorsRow(hasNoConsequentSeparators = hasNoConsequentSeparators)
                NotStartOrEndWithCommaRow(notStartOrEndWithComma = notStartOrEndWithComma)
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
            text = "Be between ${FieldValidator.MIN_USERNAME_LENGTH} and " +
                    "${FieldValidator.MAX_USERNAME_LENGTH} characters",
            style = MaterialTheme.typography.labelSmall,
            color = if (hasEnoughLength) SuccessGreen
            else MaterialTheme.colorScheme.error
        )
    }
}

@Composable
private fun HasAllowedCharactersOnlyRow(hasAllowedCharactersOnly: Boolean) {
    Row(
        modifier = Modifier.padding(bottom = 2.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        if (hasAllowedCharactersOnly) CheckmarkIcon()
        else CrossIcon()

        Spacer(modifier = Modifier.width(5.dp))
        Text(
            text = "Have at least 1 letter or digit and no special characters",
            style = MaterialTheme.typography.labelSmall,
            color = if (hasAllowedCharactersOnly) SuccessGreen
            else MaterialTheme.colorScheme.error
        )
    }
}

@Composable
private fun HasNoConsequentSeparatorsRow(hasNoConsequentSeparators: Boolean) {
    Row(
        modifier = Modifier.padding(bottom = 2.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        if (hasNoConsequentSeparators) CheckmarkIcon()
        else CrossIcon()

        Spacer(modifier = Modifier.width(5.dp))
        Text(
            text = "Not have 2 commas or underscores after each other",
            style = MaterialTheme.typography.labelSmall,
            color = if (hasNoConsequentSeparators) SuccessGreen
            else MaterialTheme.colorScheme.error
        )
    }
}

@Composable
private fun NotStartOrEndWithCommaRow(notStartOrEndWithComma: Boolean) {
    Row(
        modifier = Modifier.padding(bottom = 2.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        if (notStartOrEndWithComma) CheckmarkIcon()
        else CrossIcon()

        Spacer(modifier = Modifier.width(5.dp))
        Text(
            text = "Not start or end with a comma",
            style = MaterialTheme.typography.labelSmall,
            color = if (notStartOrEndWithComma) SuccessGreen
            else MaterialTheme.colorScheme.error
        )
    }
}

@Composable
private fun IsUsernameTakenRow(
    isFocused: Boolean,
    isUsernameTaken: Boolean,
    username: String,
    show: Boolean
) {
    AnimatedVisibility(
        visible = isFocused && username.isNotEmpty() && show,
        enter = expandVertically(
            expandFrom = Alignment.Top,
            animationSpec = tween(200)
        ),
        exit = shrinkVertically(
            shrinkTowards = Alignment.Top,
            animationSpec = tween(200)
        )
    ) {
        Row(
            modifier = Modifier.padding(start = 8.dp, top = 6.dp, bottom = 2.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            val usernameFreeOrTakenText: String =
                if (isUsernameTaken) {
                    CrossIcon()
                    "$username is already taken"
                } else {
                    CheckmarkIcon()
                    "${username.lowercase()} is free"
                }
            Spacer(modifier = Modifier.width(5.dp))
            Text(
                text = usernameFreeOrTakenText,
                style = MaterialTheme.typography.labelSmall,
                color = if (!isUsernameTaken) SuccessGreen
                else MaterialTheme.colorScheme.error
            )
        }
    }
}

@Composable
private fun UserIcon() {
    Icon(
        modifier = Modifier
            .size(21.dp),
        painter = painterResource(id = R.drawable.user_profile_btn),
        contentDescription = "Username",
        tint = MaterialTheme.colorScheme.onSurfaceVariant
    )
}

@Preview
@Composable
private fun UsernameTextFieldPreview() {
    RippleTheme {
        UsernameTextField(
            onTextChanged = {},
            isUsernameTaken = true,
            showUsernameTaken = true
        )
    }
}