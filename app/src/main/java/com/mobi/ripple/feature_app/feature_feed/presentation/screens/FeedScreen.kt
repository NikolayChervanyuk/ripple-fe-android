package com.mobi.ripple.feature_app.feature_feed.presentation.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.unit.dp
import com.mobi.ripple.core.util.RouteType
import com.mobi.ripple.feature_app.feature_feed.presentation.components.FeedTopBar
import kotlinx.serialization.Serializable

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FeedScreen() {
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior(rememberTopAppBarState())
    Scaffold(
        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            FeedTopBar(scrollBehavior)
            
        }
    ) { innerPaddingValues ->
        Surface(
            modifier = Modifier
                .windowInsetsPadding(WindowInsets(top = innerPaddingValues.calculateTopPadding()))
                .fillMaxSize()
        ) {

            LazyColumn {
                item { FeedScreenInternal() }
                item {
                    Box(modifier = Modifier
                        .size(160.dp)
                        .background(Color.Cyan))
                    TextField(value = "sample text", onValueChange = {})

                }
                item {
                    Box(modifier = Modifier
                        .size(160.dp)
                        .background(Color.Cyan))
                    TextField(value = "sample text", onValueChange = {})

                }
                item {
                    Box(modifier = Modifier
                        .size(160.dp)
                        .background(Color.Cyan))
                    TextField(value = "sample text", onValueChange = {})

                }
                item {
                    Box(modifier = Modifier
                        .size(160.dp)
                        .background(Color.Cyan))
                    TextField(value = "sample text", onValueChange = {})

                }
            }
        }
    }
}

@Composable
private fun FeedScreenInternal() {
    Text(text = "feed screen")
}

@Serializable
object FeedScreenRoute : RouteType