package com.mobi.ripple.feature_auth.presentation.register.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.mobi.ripple.R
import com.mobi.ripple.core.presentation.InvalidFieldMessage
import com.mobi.ripple.core.presentation.RippleInputField
import com.mobi.ripple.core.util.validator.FieldValidator

@Composable
fun FullNameTextField(
    modifier: Modifier = Modifier,
    onTextChanged: (String) -> Unit,
    showInvalidMessage: Boolean = false
) {
    Column(modifier = modifier) {
        RippleInputField(
            modifier = Modifier.padding(bottom = 8.dp),
            leadingIcon = { IdCardIcon() },
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Ascii,
                imeAction = ImeAction.Next
            ),
            placeholder = stringResource(R.string.full_name),
            onTextChanged = { onTextChanged(it) }
        )
        InvalidFullNameMessage(show = showInvalidMessage)
    }
}

@Composable
private fun InvalidFullNameMessage(modifier: Modifier = Modifier, show: Boolean) {
    InvalidFieldMessage(
        modifier = modifier,
        show = show,
        message = "Name should be no more than " +
                "${FieldValidator.MAX_FULLNAME_LENGTH} characters long"
    )
}

@Composable
private fun IdCardIcon() {
    Icon(
        modifier = Modifier
            .size(21.dp),
        painter = painterResource(id = R.drawable.name_id_icon),
        contentDescription = "Full name",
        tint = MaterialTheme.colorScheme.onSurfaceVariant
    )
}