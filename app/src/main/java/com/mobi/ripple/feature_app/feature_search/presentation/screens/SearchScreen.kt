package com.mobi.ripple.feature_app.feature_search.presentation.screens

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.mobi.ripple.core.util.RouteType
import kotlinx.serialization.Serializable

@Composable
fun SearchScreen(modifier: Modifier = Modifier) {
    Text(text = "search screen")
}

@Serializable
object SearchScreenRoute : RouteType