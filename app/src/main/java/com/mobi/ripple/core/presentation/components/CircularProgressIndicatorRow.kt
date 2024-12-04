package com.mobi.ripple.core.presentation.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun CircularProgressIndicatorRow() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 40.dp, top = 15.dp),
        contentAlignment = Alignment.Center
    ) {
        DefaultCircularProgressIndicator()
    }
}