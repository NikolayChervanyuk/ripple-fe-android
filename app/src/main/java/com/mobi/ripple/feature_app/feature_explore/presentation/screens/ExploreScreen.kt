package com.mobi.ripple.feature_app.feature_explore.presentation.screens

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.mobi.ripple.core.util.RouteType
import kotlinx.serialization.Serializable

@Composable
fun ExploreScreen(modifier: Modifier = Modifier) {
    Text(text = "explore screen")
}

@Serializable
object ExploreScreenRoute : RouteType