package com.mobi.ripple.core.presentation.components

import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import com.mobi.ripple.R

@Composable
fun ForwardIcon(
    modifier: Modifier = Modifier,
    tint: Color = MaterialTheme.colorScheme.onSurfaceVariant
) {
    Icon(
        modifier = modifier,
        painter = painterResource(id = R.drawable.forward_arrow_icon),
        tint = tint,
        contentDescription = "share icon"
    )
}