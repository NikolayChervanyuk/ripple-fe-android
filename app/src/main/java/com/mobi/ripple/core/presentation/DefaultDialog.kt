package com.mobi.ripple.core.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.compose.ui.zIndex

@Composable
fun DefaultDialog(
    onDismissRequest: () -> Unit,
    properties: DialogProperties = DialogProperties(
        usePlatformDefaultWidth = false
    ),
    header: @Composable (() -> Unit)? = null,
    content: @Composable () -> Unit
) {
    Dialog(
        onDismissRequest = { onDismissRequest() },
        properties = properties
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
                .zIndex(10f)
                .safeDrawingPadding(),
            contentAlignment = Alignment.Center
        ) {
            Column(
                modifier = Modifier.fillMaxSize()
            ) {
                header?.let {
                    Box(modifier = Modifier.padding(horizontal = 8.dp, vertical = 6.dp)) {
                        it.invoke()
                    }
                    HorizontalDivider(
                        thickness = 2.dp,
                        color = MaterialTheme.colorScheme.outline
                    )
                }
                Box(
                    modifier = Modifier
                        .padding(start = 8.dp, end = 8.dp)
                ) {
                    content()
                }
            }
        }
    }
}