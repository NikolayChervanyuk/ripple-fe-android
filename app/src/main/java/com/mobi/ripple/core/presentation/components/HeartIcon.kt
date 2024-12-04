package com.mobi.ripple.core.presentation.components

import androidx.compose.foundation.layout.Row
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import com.mobi.ripple.R

@Composable
fun HeartIcon(
    modifier: Modifier = Modifier,
    tint: Color = MaterialTheme.colorScheme.onSurface
) {
    Row {
        Icon(
            modifier = modifier,
            painter = painterResource(id = R.drawable.red_heart_icon),
            tint = tint,
            contentDescription = "heart icon"
        )
    }
}