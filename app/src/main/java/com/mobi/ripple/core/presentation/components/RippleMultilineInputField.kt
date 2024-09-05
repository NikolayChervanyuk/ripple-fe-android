package com.mobi.ripple.core.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
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
import com.mobi.ripple.core.theme.Shapes

@Composable
fun RippleMultilineInputField(
    modifier: Modifier = Modifier,
    placeholder: String = "",
    placeholderColor: Color = colorScheme.onSurfaceVariant,
    text: String = "",
    textColor: Color = colorScheme.onSurface,
    textAlignment: Alignment = Alignment.CenterStart,
    readOnly: Boolean = false,
    onTextChanged: (String) -> Unit,
    minLines: Int = 3,
    visualTransformation: VisualTransformation = VisualTransformation.None,
    keyboardOptions: KeyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
) {
    var textEntered by remember {
        mutableStateOf(text)
    }
    var isFocused by remember {
        mutableStateOf(false)
    }
    BasicTextField(
        modifier = modifier
            .heightIn(min = 43.dp)
            .border(
                width = 1.dp,
                color = colorScheme.outline,
                shape = Shapes.medium
            )
            .clip(Shapes.medium)
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
        minLines = minLines,
    ) { innerTextField ->
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 10.dp, vertical = 8.dp), // inner padding
        ) {
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
        }
    }

}

@Preview
@Composable
private fun RippleMultilineInputFieldPreview() {
    RippleTheme {
        RippleMultilineInputField(
            minLines = 5,
            text = "aladin@gmail.com",
            onTextChanged = {}
        )
    }
}