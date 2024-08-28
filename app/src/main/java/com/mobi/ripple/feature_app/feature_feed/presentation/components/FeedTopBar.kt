package com.mobi.ripple.feature_app.feature_feed.presentation.components

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.mobi.ripple.core.presentation.Logo
import com.mobi.ripple.core.theme.RippleTheme
import com.mobi.ripple.core.theme.jostFamily

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FeedTopBar(scrollBehavior: TopAppBarScrollBehavior) {
    val outlineColor = MaterialTheme.colorScheme.outline
    TopAppBar(
        modifier = Modifier
            .drawBehind {
                drawLine(
                    start = Offset(0f, size.height),
                    end = Offset(size.width, size.height),
                    color = outlineColor,
                    strokeWidth = 12.dp.toPx()
                )
            },
        title = {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Logo(Modifier.size(36.dp))
                Text(
                    text = "Ripple",
                    fontSize = 24.sp,
                    modifier = Modifier.padding(start = 12.dp),
                    style = TextStyle(
                        color = MaterialTheme.colorScheme.onBackground,
                        fontFamily = jostFamily,
                        fontWeight = FontWeight.Bold
                    )
                )
            }
        },
        scrollBehavior = scrollBehavior,
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Preview(showSystemUi = true)
@Composable
private fun FeedTopBarPreview() {
    RippleTheme {
        FeedTopBar(TopAppBarDefaults.enterAlwaysScrollBehavior(rememberTopAppBarState()))
    }
}