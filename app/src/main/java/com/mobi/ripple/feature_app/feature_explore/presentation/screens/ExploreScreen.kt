package com.mobi.ripple.feature_app.feature_explore.presentation.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.mobi.ripple.R
import com.mobi.ripple.core.util.RouteType
import kotlinx.serialization.Serializable

@Composable
fun ExploreScreen(modifier: Modifier = Modifier) {
    Column(
        modifier = Modifier
            .fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Image(
            modifier = Modifier
                .padding(top = 120.dp)
                .size(100.dp),
            painter = painterResource(id = R.drawable.under_construction_icon),
            contentDescription = "feature is under construction"
        )
        Text(
            modifier = Modifier.padding(top = 12.dp),
            text = "Currently working on this feature",
            style = MaterialTheme.typography.titleLarge
        )
    }
}

@Serializable
object ExploreScreenRoute : RouteType