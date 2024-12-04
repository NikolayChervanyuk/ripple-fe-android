package com.mobi.ripple.core.presentation.components

import androidx.compose.foundation.clickable
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import com.mobi.ripple.R

@Composable
fun ReloadButton(
    modifier: Modifier = Modifier,
    tint: Color = MaterialTheme.colorScheme.onSurface,
    onClick: () -> Unit
) {
    Icon(
        modifier = modifier
            .clickable {
                onClick()
            },
        painter = painterResource(id = R.drawable.reload_icon),
        tint = tint,
        contentDescription = "reload button"
    )
}