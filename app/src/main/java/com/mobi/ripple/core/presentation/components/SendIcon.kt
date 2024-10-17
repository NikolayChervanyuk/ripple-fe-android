package com.mobi.ripple.core.presentation.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import com.mobi.ripple.R

@Composable
fun SendIcon(
    modifier: Modifier = Modifier,
    tint: Color = MaterialTheme.colorScheme.onBackground,
    badge: @Composable (BoxScope.() -> Unit)? = null
) {
    if (badge != null) {
        Box(
            modifier = modifier,
            contentAlignment = Alignment.Center
        ) {
            Icon(
                modifier = Modifier,
                painter = painterResource(id = R.drawable.send_icon),
                tint = tint,
                contentDescription = "Send"
            )
            badge()
        }
    } else {
        Icon(
            modifier = modifier,
            painter = painterResource(id = R.drawable.send_icon),
            tint = tint,
            contentDescription = "Send"
        )
    }
}