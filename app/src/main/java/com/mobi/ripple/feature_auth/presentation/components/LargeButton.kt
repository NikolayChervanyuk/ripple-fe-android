package com.mobi.ripple.feature_auth.presentation.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.mobi.ripple.core.theme.PaddingLarge
import com.mobi.ripple.core.theme.Shapes

@Composable
fun LargeButton(modifier: Modifier = Modifier, text: String?, onClick: () -> Unit) {
    Button(
        modifier = modifier.fillMaxWidth(0.7f),
        shape = Shapes.small,
        colors = ButtonDefaults.buttonColors()
            .copy(containerColor = MaterialTheme.colorScheme.secondary),
        contentPadding = PaddingLarge,
        onClick = { onClick() }
    ) {
        Text(
            text = text.orEmpty(),
            style = MaterialTheme.typography.titleLarge,
            color = MaterialTheme.colorScheme.onSecondary,
        )
    }
}