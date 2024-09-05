package com.mobi.ripple.core.presentation.components

import androidx.compose.material3.Badge
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale

@Composable
fun RippleBadge(modifier: Modifier = Modifier, hasNews: Boolean = false, counter: Int?) {

    if (counter != null && counter > 0) {
        Badge(
            modifier = modifier
        ) {
            val numberString = if (counter > 99) "+99"
            else counter.toString()
            Text(text = numberString)
        }
    } else if (hasNews) {
        Badge(modifier = modifier.scale(0.6f)) { }
    }
}