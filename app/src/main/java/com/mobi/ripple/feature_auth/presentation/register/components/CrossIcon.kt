package com.mobi.ripple.feature_auth.presentation.register.components

import androidx.compose.foundation.layout.Row
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
fun CrossIcon() {
    Row {
        Icon(
            modifier = Modifier
                .size(16.dp)
                .padding(2.dp),
            painter = painterResource(id = R.drawable.cross_icon),
            tint = MaterialTheme.colorScheme.error,
            contentDescription = "requirement not met"
        )
    }
}