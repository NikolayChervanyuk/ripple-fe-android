package com.mobi.ripple.feature_auth.presentation.register.components

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.mobi.ripple.R
import com.mobi.ripple.core.theme.SuccessGreen

@Composable
fun CheckmarkIcon() {
    Row {
        Icon(
            modifier = Modifier
                .size(16.dp)
                .padding(2.dp),
            painter = painterResource(id = R.drawable.check_mark_icon),
            tint = SuccessGreen,
            contentDescription = "requirement met"
        )
    }
}