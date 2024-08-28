package com.mobi.ripple.core.presentation

import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import com.mobi.ripple.R

@Composable
fun BackArrow(modifier: Modifier = Modifier) {
    Icon(
        modifier = modifier,
        painter = painterResource(id = R.drawable.left_arrow_icon),
        contentDescription = "Ripple logo",
        tint = MaterialTheme.colorScheme.onSurface,
    )
}