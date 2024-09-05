package com.mobi.ripple.core.presentation.components

import androidx.annotation.DrawableRes
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.mobi.ripple.R
import com.mobi.ripple.core.theme.RippleTheme

@Composable
fun OptionItem(
    @DrawableRes iconId: Int?,
    text: String,
    color: Color? = null,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .clickable { onClick() }
            .fillMaxWidth()
            .padding(horizontal = 6.dp, vertical = 10.dp),
        horizontalArrangement = Arrangement.Start,
        verticalAlignment = Alignment.CenterVertically
    ) {
        iconId?.let { iconId ->
            Icon(
                painter = painterResource(id = iconId),
                contentDescription = "option icon",
                tint = color ?: MaterialTheme.colorScheme.onBackground,
                modifier = Modifier.size(21.dp)
            )
            Spacer(modifier = Modifier.padding(horizontal = 5.dp))
        }
        iconId ?: Spacer(modifier = Modifier.padding(horizontal = 26.dp))

        Text(
            text = text,
            style = MaterialTheme.typography.bodyLarge,
            color = color ?: MaterialTheme.colorScheme.onBackground
        )
    }
}

@Preview
@Composable
private fun OptionItemPreview() {
    RippleTheme {
        Surface {
            OptionItem(
                iconId = R.drawable.setting_icon,
                text = "Change profile picture",
                onClick = {})
        }
    }
}