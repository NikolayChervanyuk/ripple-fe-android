package com.mobi.ripple.core.presentation.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import com.mobi.ripple.R

@Composable
fun CancelButton(
    modifier: Modifier,
    tint: Color = MaterialTheme.colorScheme.onSurface,
    horizontalArrangement: Arrangement.Horizontal = Arrangement.Center) {
    Row(
        modifier = modifier,
        horizontalArrangement = horizontalArrangement
    ) {
        Icon(
            modifier = Modifier.fillMaxSize(),
            painter = painterResource(id = R.drawable.cross_icon),
            tint = tint,
            contentDescription = "cancel"
        )
    }
}