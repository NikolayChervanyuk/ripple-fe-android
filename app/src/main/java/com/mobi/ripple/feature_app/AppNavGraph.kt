package com.mobi.ripple.feature_app

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.mobi.ripple.feature_app.feature_explore.presentation.screens.ExploreScreen
import com.mobi.ripple.feature_app.feature_explore.presentation.screens.ExploreScreenRoute
import com.mobi.ripple.feature_app.feature_feed.presentation.screens.FeedScreen
import com.mobi.ripple.feature_app.feature_feed.presentation.screens.FeedScreenRoute
import com.mobi.ripple.feature_app.feature_profile.presentation.profile.profileGraph
import com.mobi.ripple.feature_app.feature_search.presentation.screens.SearchScreen
import com.mobi.ripple.feature_app.feature_search.presentation.screens.SearchScreenRoute
import kotlinx.coroutines.CoroutineScope

@Composable
fun AppNavGraph(
    mainNavController: NavHostController,
    snackbarHostState: SnackbarHostState,
    coroutineScope: CoroutineScope,
    paddingValues: PaddingValues
) {
    NavHost(
        navController = mainNavController,
        startDestination = FeedScreenRoute,
        modifier = Modifier
            .padding(paddingValues)
            .consumeWindowInsets(paddingValues)
    ) {
        composable<FeedScreenRoute> {
            //viewmodel here
            FeedScreen()
        }
        composable<SearchScreenRoute> {
            //viewmodel here
            SearchScreen()
        }
        composable<ExploreScreenRoute> {
            //viewmodel here
            ExploreScreen()
        }
        profileGraph(mainNavController, snackbarHostState, coroutineScope)
    }
}