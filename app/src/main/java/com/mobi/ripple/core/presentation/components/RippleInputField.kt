package com.mobi.ripple.core.presentation.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandHorizontally
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.MaterialTheme.typography
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.mobi.ripple.core.theme.RippleTheme

@Composable
fun RippleInputField(
    modifier: Modifier = Modifier,
    placeholder: String = "",
    placeholderColor: Color = colorScheme.onSurfaceVariant,
    text: String = "",
    textColor: Color = colorScheme.onSurface,
    textAlignment: Alignment = Alignment.CenterStart,
    readOnly: Boolean = false,
    onTextChanged: (String) -> Unit = {},
    visualTransformation: VisualTransformation = VisualTransformation.None,
    keyboardOptions: KeyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
    alwaysShowLeadingIcon: Boolean = false,
    leadingIcon: @Composable (() -> Unit)? = null,
    trailingIcon: @Composable (() -> Unit)? = null
) {
    var textEntered by remember {
        mutableStateOf(text)
    }
    var isFocused by remember {
        mutableStateOf(false)
    }

    BasicTextField(
        modifier = modifier
            .wrapContentSize()
            .height(43.dp)
            .border(
                width = 1.dp,
                color = colorScheme.outline,
                shape = CircleShape
            )
            .clip(CircleShape)
            .background(colorScheme.surface)
            .onFocusChanged {
                isFocused = it.isFocused
            },
        keyboardOptions = keyboardOptions,
        visualTransformation = visualTransformation,
        value = textEntered,
        readOnly = readOnly,
        textStyle = typography.bodyMedium.copy(
            color =
            if (readOnly) colorScheme.onSurfaceVariant
            else textColor
        ),
        onValueChange = {
            textEntered = it
            onTextChanged(it)
        },
        cursorBrush = SolidColor(colorScheme.onSurface),
        singleLine = true
    ) { innerTextField ->
        Row(
            modifier = Modifier
//                .let {
//                    if (readOnly) it.clickable { onClick() }
//                    else it
//                }
                .fillMaxWidth()
                .padding(horizontal = 20.dp, vertical = 12.dp), // inner padding
        ) {
            leadingIcon?.let {
                AnimatedVisibility(
                    visible = isFocused || alwaysShowLeadingIcon,
                    enter = expandHorizontally()
                ) {
                    Row {
                        it()
                        Spacer(
                            modifier = Modifier
                                .padding(7.dp, 0.dp)
                                .width(1.dp)
                                .height(24.dp)
                                .align(Alignment.CenterVertically)
                                .background(colorScheme.outline),
                        )
                    }
                }
            }
            Box(
                modifier = Modifier,
                contentAlignment = textAlignment
            ) {
                innerTextField()
                if (textEntered.isEmpty()) {
                    Text(
                        text = placeholder,
                        color = placeholderColor,
                        style = typography.bodyMedium
                    )
                }
            }
            trailingIcon?.let {
                Spacer(modifier = Modifier.weight(1f))
                Spacer(
                    modifier = Modifier
                        .padding(7.dp, 0.dp)
                        .width(1.dp)
                        .height(24.dp)
                        .align(Alignment.CenterVertically)
                        .background(colorScheme.outline),
                )
                it()
            }
        }
    }
}

@Preview
@Composable
private fun RippleInputFieldPreview() {
    RippleTheme {
        RippleInputField(text = "aladin@gmail.com")
    }
}