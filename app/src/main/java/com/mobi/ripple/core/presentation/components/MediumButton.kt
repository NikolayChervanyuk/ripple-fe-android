package com.mobi.ripple.core.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun MediumButton(
    modifier: Modifier = Modifier,
    containerColor: Color = MaterialTheme.colorScheme.surface,
    text: String,
    textColor: Color = MaterialTheme.colorScheme.onSurface,
    icon: @Composable (() -> Unit)? = null,
    onClick: () -> Unit
) {
    Row(
        modifier = modifier
            .clickable { onClick() }
            .background(containerColor)
            .padding(horizontal = 8.dp , vertical = 6.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        icon?.let {
            it.invoke()
            Spacer(modifier = Modifier.width(4.dp))
        }
        Text(
            modifier = Modifier,
            text = text,
            style = MaterialTheme.typography.titleSmall,
            color = textColor
        )
    }
}