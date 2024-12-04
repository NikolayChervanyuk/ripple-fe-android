package com.mobi.ripple.feature_auth.presentation.login.components

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.mobi.ripple.R

@Composable
fun ForgottenPasswordText(onClick: () -> Unit) {
    Text(
        text = stringResource(R.string.forgotten_password),
        style = MaterialTheme.typography.titleSmall,
        color = MaterialTheme.colorScheme.secondary,
        modifier = Modifier.padding(vertical = 24.dp)
    )
}