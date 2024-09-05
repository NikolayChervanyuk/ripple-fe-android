package com.mobi.ripple.feature_app

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import com.mobi.ripple.core.presentation.profile.ProfileScreen
import com.mobi.ripple.core.presentation.profile.ProfileScreenRoute
import com.mobi.ripple.core.presentation.profile.ProfileViewModel
import com.mobi.ripple.feature_app.feature_explore.presentation.screens.ExploreScreen
import com.mobi.ripple.feature_app.feature_explore.presentation.screens.ExploreScreenRoute
import com.mobi.ripple.feature_app.feature_feed.presentation.screens.FeedScreen
import com.mobi.ripple.feature_app.feature_feed.presentation.screens.FeedScreenRoute
import com.mobi.ripple.feature_app.feature_profile.presentation.profile.personalProfileGraph
import com.mobi.ripple.feature_app.feature_search.presentation.search.SearchScreen
import com.mobi.ripple.feature_app.feature_search.presentation.search.SearchScreenRoute
import com.mobi.ripple.feature_app.feature_search.presentation.search.SearchViewModel
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
            val viewModel = hiltViewModel<SearchViewModel>()
            SearchScreen(
                viewModel,
                mainNavController,
                coroutineScope,
                snackbarHostState
            )
        }
        composable<ExploreScreenRoute> {
            //viewmodel here
            ExploreScreen()
        }
        personalProfileGraph(mainNavController, snackbarHostState, coroutineScope)

        composable<ProfileScreenRoute> {
            val username = it.toRoute<ProfileScreenRoute>().username
            val viewModel = hiltViewModel<ProfileViewModel>()
            ProfileScreen(
                username,
                viewModel,
                mainNavController,
                snackbarHostState
            )
        }
    }
}