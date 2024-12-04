package com.mobi.ripple.core.presentation.components

import androidx.compose.foundation.layout.size
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun DefaultCircularProgressIndicator() {
    CircularProgressIndicator(
        modifier = Modifier
            .size(36.dp),
        strokeWidth = 5.dp,
        color = MaterialTheme.colorScheme.primary,
        trackColor = MaterialTheme.colorScheme.onSurfaceVariant
    )
}