package com.mobi.ripple.core.presentation.components

import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun DefaultSearchField(
    modifier: Modifier = Modifier,
    onTextChanged: (newText: String) -> Unit,
    placeholder: String,
    text: String = ""
) {
    RippleInputField(
        modifier = modifier,
        leadingIcon = {
            SearchIcon(
                modifier = Modifier
                    .size(18.dp)
                    .offset(x = (-3).dp)
            )
        },
        text = text,
        alwaysShowLeadingIcon = true,
        placeholder = placeholder,
        onTextChanged = { onTextChanged(it) }
    )
}