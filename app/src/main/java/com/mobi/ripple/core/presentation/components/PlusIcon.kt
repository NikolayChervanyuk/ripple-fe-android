package com.mobi.ripple.core.presentation.components

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.mobi.ripple.R

@Composable
fun PlusIcon(modifier: Modifier = Modifier) {
    Icon(
        painter = painterResource(id = R.drawable.plus_icon),
        modifier = modifier,
        tint = MaterialTheme.colorScheme.onSurface,
        contentDescription = "add"
    )
}