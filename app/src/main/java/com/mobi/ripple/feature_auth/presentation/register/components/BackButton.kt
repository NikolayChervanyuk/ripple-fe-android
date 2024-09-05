package com.mobi.ripple.feature_auth.presentation.register.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.calculateEndPadding
import androidx.compose.foundation.layout.calculateStartPadding
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import com.mobi.ripple.core.presentation.components.BackArrow

@Composable
fun BackButton(paddingValues: PaddingValues = PaddingValues(0.dp), onClick: () -> Unit) {
    val horizontalPadding =
        paddingValues.calculateStartPadding(LayoutDirection.Ltr) +
                paddingValues.calculateEndPadding(LayoutDirection.Ltr)

    val verticalPadding =
        paddingValues.calculateTopPadding() +
                paddingValues.calculateBottomPadding()


    BackArrow(modifier = Modifier
        .width(24.dp + horizontalPadding)
        .height(24.dp + verticalPadding)
        .padding(paddingValues)
        .clickable(
            indication = null,
            interactionSource = remember {
                MutableInteractionSource()
            }) { onClick() }
    )
}