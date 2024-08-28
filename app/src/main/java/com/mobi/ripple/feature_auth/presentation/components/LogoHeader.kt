package com.mobi.ripple.feature_auth.presentation.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.mobi.ripple.R
import com.mobi.ripple.core.presentation.Logo
import com.mobi.ripple.core.theme.jostFamily

@Composable
fun LogoHeader(headerMessage: String? = null) {
    Column(
        modifier = Modifier.padding(top = 40.dp, bottom = 24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Logo(
            Modifier
                .padding(bottom = 16.dp)
                .size(92.dp)
        )
        LogoText()
        MessageText(headerMessage)
    }
}

@Composable
private fun LogoText() {
    Text(
        text = stringResource(id = R.string.app_name),
        fontFamily = jostFamily,
        fontWeight = FontWeight.Bold,
        fontSize = 32.sp,
        color = MaterialTheme.colorScheme.onBackground,
    )
}

@Composable
private fun MessageText(message: String?) {
    message?.let {
        Text(
            text = message,
            style = MaterialTheme.typography.titleLarge,
            color = MaterialTheme.colorScheme.onBackground,
            modifier = Modifier.padding(top = 24.dp)
        )
    }
}
