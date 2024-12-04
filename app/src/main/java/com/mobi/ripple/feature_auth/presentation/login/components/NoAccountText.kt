package com.mobi.ripple.feature_auth.presentation.login.components

import androidx.compose.foundation.clickable
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.withStyle
import com.mobi.ripple.R

@Composable
fun NoAccountText(onClick: () -> Unit) {
    Text(
        text =
        buildAnnotatedString {
            append(stringResource(R.string.don_t_have_an_account))
            withStyle(
                style = SpanStyle(
                    fontWeight = MaterialTheme.typography.titleSmall.fontWeight,
                    color = MaterialTheme.colorScheme.primary
                )
            ) {
                append(stringResource(R.string.sign_up))
            }
        },
        style = MaterialTheme.typography.bodyMedium,
        color = MaterialTheme.colorScheme.onBackground,
        modifier = Modifier.clickable { onClick() }
    )
}