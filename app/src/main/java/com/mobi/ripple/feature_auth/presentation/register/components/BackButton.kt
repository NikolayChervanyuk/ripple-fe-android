package com.mobi.ripple.feature_auth.presentation.register.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.mobi.ripple.core.presentation.BackArrow

@Composable
fun BackButton(modifier: Modifier = Modifier, onClick: () -> Unit) {
    BackArrow(modifier = modifier
        .padding(start = 16.dp, top = 16.dp)
        .size(24.dp)
        .clickable(
            indication = null,
            interactionSource = remember {
                MutableInteractionSource()
            }) { onClick() }
    )
}