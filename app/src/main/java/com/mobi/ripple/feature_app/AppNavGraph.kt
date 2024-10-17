package com.mobi.ripple.feature_app

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import com.mobi.ripple.core.presentation.followers_following.FollowersFollowingScreen
import com.mobi.ripple.core.presentation.followers_following.FollowersFollowingScreenRoute
import com.mobi.ripple.core.presentation.followers_following.FollowersFollowingViewModel
import com.mobi.ripple.core.presentation.followers_following.GetType
import com.mobi.ripple.core.presentation.posts.PostsScreen
import com.mobi.ripple.core.presentation.posts.PostsScreenRoute
import com.mobi.ripple.core.presentation.posts.PostsViewModel
import com.mobi.ripple.core.presentation.profile.ProfileScreen
import com.mobi.ripple.core.presentation.profile.ProfileScreenRoute
import com.mobi.ripple.core.presentation.profile.ProfileViewModel
import com.mobi.ripple.feature_app.feature_chat.presentation.chatsGraph
import com.mobi.ripple.feature_app.feature_explore.presentation.screens.ExploreScreen
import com.mobi.ripple.feature_app.feature_explore.presentation.screens.ExploreScreenRoute
import com.mobi.ripple.feature_app.feature_feed.presentation.screens.FeedScreen
import com.mobi.ripple.feature_app.feature_feed.presentation.screens.FeedScreenRoute
import com.mobi.ripple.feature_app.feature_profile.presentation.profile.personalProfileGraph
import com.mobi.ripple.feature_app.feature_search.presentation.search.SearchScreen
import com.mobi.ripple.feature_app.feature_search.presentation.search.SearchScreenRoute
import com.mobi.ripple.feature_app.feature_search.presentation.search.SearchViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import kotlin.reflect.typeOf

@Composable
fun AppNavGraph(
    mainNavController: NavHostController,
    snackbarHostState: SnackbarHostState,
    coroutineScope: CoroutineScope,
    messageManager: MessageManager,
    paddingValues: PaddingValues
) {
    LaunchedEffect(key1 = true) {
        coroutineScope.launch {
            messageManager.openConnection()
        }
    }
    DisposableEffect(key1 = true) {
        onDispose {
            coroutineScope.launch {
                messageManager.closeConnection()
            }
        }
    }
    NavHost(
        navController = mainNavController,
        startDestination = FeedScreenRoute,
        modifier = Modifier
            .padding(paddingValues)
            .consumeWindowInsets(paddingValues)
    ) {
        composable<FeedScreenRoute> {
            //viewmodel here
            FeedScreen(mainNavController, messageManager)
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

        composable<FollowersFollowingScreenRoute>(
            typeMap = mapOf(
                typeOf<String>() to NavType.StringType,
                typeOf<GetType>() to NavType.EnumType(GetType::class.java)
            )
        ) {
            val route = it.toRoute<FollowersFollowingScreenRoute>()
            val viewModel = hiltViewModel<FollowersFollowingViewModel>()
            FollowersFollowingScreen(
                username = route.username,
                getType = route.getType,
                viewModel = viewModel,
                navController = mainNavController
            )
        }

        composable<PostsScreenRoute>(
            typeMap = mapOf(
                typeOf<Int>() to NavType.IntType,
                typeOf<String>() to NavType.StringType,
                typeOf<String>() to NavType.StringType
            )
        ) {
            val route = it.toRoute<PostsScreenRoute>()
            val viewModel = hiltViewModel<PostsViewModel>()
            PostsScreen(
                startItemIndex = route.startItemIndex,
                startItemId = route.startItemId,
                authorId = route.authorId,
                viewModel = viewModel,
                navController = mainNavController,
                snackbarHost = snackbarHostState
            )
        }

        chatsGraph(mainNavController, snackbarHostState, coroutineScope, messageManager)
    }
}
