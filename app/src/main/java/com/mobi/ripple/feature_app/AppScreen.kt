package com.mobi.ripple.feature_app

import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.rememberNavController
import com.mobi.ripple.core.presentation.DefaultSnackbar
import com.mobi.ripple.core.theme.RippleTheme
import com.mobi.ripple.core.util.RouteType
import kotlinx.serialization.Serializable

@Composable
fun AppScreen() {
    val snackbarHostState = remember { SnackbarHostState() }
    val appNavController = rememberNavController()
    Scaffold(
        snackbarHost = { DefaultSnackbar(hostState = snackbarHostState) },
        bottomBar = { BottomBar(appNavController) }
    ) { paddingValues ->
        AppNavGraph(appNavController, snackbarHostState, paddingValues)
    }
}

@Serializable
object AppScreenRoute : RouteType

@Preview(showSystemUi = true)
@Composable
private fun AppScreenPreview() {
    RippleTheme {
        AppScreen()
    }
}