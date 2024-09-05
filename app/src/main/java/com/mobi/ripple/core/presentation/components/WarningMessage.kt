package com.mobi.ripple.core.presentation.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandVertically
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.mobi.ripple.core.theme.WarningYellow

@Composable
fun WarningMessage(modifier: Modifier = Modifier, show: Boolean, message: String) {
    AnimatedVisibility(
        modifier = modifier.padding(top = 2.dp),
        visible = show,
        enter = expandVertically(
            expandFrom = Alignment.Top,
            animationSpec = tween(200)
        ),
        exit = shrinkVertically(
            shrinkTowards = Alignment.Top,
            animationSpec = tween(200)
        )
    ) {
        Row(
            modifier = Modifier.padding(bottom = 2.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            ExclamationMarkIcon()
            Spacer(modifier = Modifier.width(5.dp))
            Text(
                text = message,
                style = MaterialTheme.typography.labelSmall,
                color = WarningYellow
            )
        }

    }
}