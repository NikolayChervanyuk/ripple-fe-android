package com.mobi.ripple.feature_app.feature_chat.presentation.new_chat.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandHorizontally
import androidx.compose.animation.expandVertically
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
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
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.mobi.ripple.core.theme.Shapes

@Composable
fun ChatNameField(
    modifier: Modifier = Modifier,
    visible: Boolean,
    onTextChanged: (String) -> Unit
) {
    var textEntered by remember { mutableStateOf("") }
    var isFocused by remember {
        mutableStateOf(false)
    }
    AnimatedVisibility(
        visible = visible,
        enter = expandVertically(
            expandFrom = Alignment.Top,
            animationSpec = tween(200)
        ),
        exit = shrinkVertically(
            shrinkTowards = Alignment.Top,
            animationSpec = tween(200)
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 12.dp)
                .padding(top = 4.dp)
        ) {
            Text(
                text =  "Chat name",
                color = colorScheme.onSurfaceVariant,
                style = typography.labelMedium,
                modifier = Modifier.padding(bottom = 4.dp)
            )
            BasicTextField(
                modifier = modifier
                    .wrapContentSize()
                    .background(colorScheme.surface)
                    .onFocusChanged {
                        isFocused = it.isFocused
                    }
                    .drawBehind {
                        drawLine(
                            color = Color.LightGray,
                            start = Offset(0f, size.height),
                            end = Offset(size.width, size.height),
                            strokeWidth = 3f
                        )
                    },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
                value = textEntered,
                textStyle = typography.titleMedium.copy(color = colorScheme.onSurface),
                onValueChange = {
                    textEntered = it
                    onTextChanged(it)
                },
                cursorBrush = SolidColor(colorScheme.onSurface),
                singleLine = true
            ) { innerTextField ->

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 4.dp)
                        .padding(horizontal = 6.dp),
                    contentAlignment = Alignment.CenterStart
                ) {
                    innerTextField()
                    if (textEntered.isEmpty()) {
                        Text(
                            text = "Give your chat an awesome name",
                            color = colorScheme.onSurfaceVariant,
                            style = typography.titleMedium
                        )
                    }
                }
            }
        }
    }
}