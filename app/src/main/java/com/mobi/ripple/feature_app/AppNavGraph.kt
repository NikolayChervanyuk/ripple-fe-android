package com.mobi.ripple.feature_app

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.mobi.ripple.feature_app.feature_explore.presentation.screens.ExploreScreen
import com.mobi.ripple.feature_app.feature_explore.presentation.screens.ExploreScreenRoute
import com.mobi.ripple.feature_app.feature_feed.presentation.screens.FeedScreen
import com.mobi.ripple.feature_app.feature_feed.presentation.screens.FeedScreenRoute
import com.mobi.ripple.feature_app.feature_profile.presentation.profile.ProfileScreen
import com.mobi.ripple.feature_app.feature_profile.presentation.profile.ProfileScreenRoute
import com.mobi.ripple.feature_app.feature_profile.presentation.profile.ProfileViewModel
import com.mobi.ripple.feature_app.feature_search.presentation.screens.SearchScreen
import com.mobi.ripple.feature_app.feature_search.presentation.screens.SearchScreenRoute

@Composable
fun AppNavGraph(
    navController: NavHostController,
    snackbarHostState: SnackbarHostState,
    paddingValues: PaddingValues) {
    NavHost(
        navController = navController,
        startDestination = FeedScreenRoute,
        modifier = Modifier
            .windowInsetsPadding(
                WindowInsets(
                    bottom = paddingValues.calculateBottomPadding()
                )
            )
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
        composable<ProfileScreenRoute> {
            val profileViewModel = hiltViewModel<ProfileViewModel>()
            ProfileScreen(profileViewModel, snackbarHostState)
        }
    }
}