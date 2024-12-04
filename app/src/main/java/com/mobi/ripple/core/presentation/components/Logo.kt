package com.mobi.ripple.core.presentation.components

import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import com.mobi.ripple.R

@Composable
fun Logo(modifier: Modifier) {
    Icon(
        painter = painterResource(id = R.drawable.logo),
        contentDescription = "Ripple logo",
        tint = MaterialTheme.colorScheme.primary,
        modifier = modifier
    )
}