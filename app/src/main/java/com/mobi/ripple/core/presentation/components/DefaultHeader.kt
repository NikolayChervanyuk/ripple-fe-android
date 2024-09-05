package com.mobi.ripple.core.presentation.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.mobi.ripple.feature_auth.presentation.register.components.BackButton

@Composable
fun DefaultHeader(
    actionComposable: @Composable (() -> Unit)? = null,
    onBackButtonClicked: () -> Unit, title: String
) {
    Column(
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(45.dp),
            horizontalArrangement = Arrangement.Start,
            verticalAlignment = Alignment.CenterVertically
        ) {
            BackButton(
                paddingValues = PaddingValues(start = 5.dp),
                onClick = { onBackButtonClicked() }
            )
            Spacer(modifier = Modifier.padding(horizontal = 5.dp))
            Text(
                text = title,
                style = MaterialTheme.typography.headlineMedium,
                color = MaterialTheme.colorScheme.onSurface
            )
            Spacer(modifier = Modifier.weight(1f))
            actionComposable?.invoke()
            Spacer(modifier = Modifier.padding(horizontal = 5.dp))
        }
    }
}

@Preview
@Composable
private fun DefaultDialogHeaderPrev() {
    MaterialTheme{
        Surface {
            DefaultHeader(onBackButtonClicked = { }, title = "Settings")
        }
    }
}