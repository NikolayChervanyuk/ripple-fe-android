package com.mobi.ripple.core.presentation.components

import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.mobi.ripple.feature_auth.presentation.register.components.BackButton

@Composable
fun DefaultActionBar(
    modifier: Modifier = Modifier,
    actionComposable: @Composable (() -> Unit)? = null,
    onBackButtonClicked: (() -> Unit)? = null,
    height: Dp? = 45.dp,
    title: String,
    textStyle: TextStyle = MaterialTheme.typography.headlineMedium
) {
    val rowModifier =
        height?.let { modifier.height(it) } ?: modifier
    Row(
        modifier = rowModifier
            .fillMaxWidth(),
        horizontalArrangement = Arrangement.Start,
        verticalAlignment = Alignment.CenterVertically
    ) {
        onBackButtonClicked?.let {
            BackButton(
                paddingValues = PaddingValues(start = 5.dp),
                onClick = { it.invoke() }
            )
        }
        Spacer(modifier = Modifier.padding(horizontal = 5.dp))
        Text(
            text = title,
            style = textStyle,
            color = MaterialTheme.colorScheme.onSurface,
            overflow = TextOverflow.Ellipsis
        )
        Spacer(modifier = Modifier.weight(1f))
        actionComposable?.invoke()
        Spacer(modifier = Modifier.padding(horizontal = 5.dp))
    }
}

@Preview
@Composable
private fun DefaultDialogHeaderPrev() {
    MaterialTheme {
        Surface {
            DefaultActionBar(
                onBackButtonClicked = { },
                title = "Settings"
            )
        }
    }
}