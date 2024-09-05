package com.mobi.ripple.core.presentation.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme.typography
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import androidx.constraintlayout.compose.ConstraintLayout
import com.mobi.ripple.core.theme.PaddingSmall

@Composable
fun DefaultSnackbar(hostState: SnackbarHostState, modifier: Modifier = Modifier) {

    ConstraintLayout(
        modifier = Modifier
            .zIndex(Float.MAX_VALUE - 1)
            .fillMaxSize()
    ) {
        val (snackBar) = createRefs()
        SnackbarHost(
            hostState = hostState,
            modifier = Modifier
                .constrainAs(snackBar) {
                    bottom.linkTo(parent.bottom, margin = 80.dp)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                }
        ) {
            Snackbar(
                modifier = Modifier
                    .zIndex(Float.MAX_VALUE)
                    .padding(PaddingSmall),
                shape = CircleShape,
            ) {
                Box(
                    modifier = Modifier.fillMaxWidth(),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = it.visuals.message,
                        style = typography.bodyMedium
                    )
                }
            }

        }
    }
}