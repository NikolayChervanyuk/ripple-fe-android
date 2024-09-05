package com.mobi.ripple.core.presentation.components

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.mobi.ripple.R
import com.mobi.ripple.core.theme.WarningYellow

@Composable
fun ExclamationMarkIcon(modifier: Modifier = Modifier) {
    Icon(
        modifier = Modifier
            .size(16.dp)
            .padding(2.dp),
        painter = painterResource(id = R.drawable.exclamation_mark),
        tint = WarningYellow,
        contentDescription = "warning message"
    )
}