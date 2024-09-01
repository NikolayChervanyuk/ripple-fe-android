package com.mobi.ripple.feature_app.feature_profile.presentation.profile

import androidx.compose.material3.SnackbarHostState
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.mobi.ripple.core.util.RouteType
import com.mobi.ripple.core.util.sharedViewModel
import com.mobi.ripple.feature_app.feature_profile.presentation.profile.post.CreatePostScreen
import com.mobi.ripple.feature_app.feature_profile.presentation.profile.post.CreatePostScreenRoute
import com.mobi.ripple.feature_app.feature_profile.presentation.profile.profile.ProfileScreen
import com.mobi.ripple.feature_app.feature_profile.presentation.profile.profile.ProfileScreenRoute
import com.mobi.ripple.feature_app.feature_profile.presentation.profile.profile.ProfileViewModel
import com.mobi.ripple.feature_app.feature_profile.presentation.profile.settings.EditProfileScreenRoute
import com.mobi.ripple.feature_app.feature_profile.presentation.profile.settings.EditProfileScreen
import com.mobi.ripple.feature_app.feature_profile.presentation.profile.settings.SettingsScreenRoute
import com.mobi.ripple.feature_app.feature_profile.presentation.profile.settings.SettingsScreen
import com.mobi.ripple.feature_app.feature_profile.presentation.profile.settings.SettingsViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.serialization.Serializable

fun NavGraphBuilder.profileGraph(
    mainNavController: NavHostController,
    snackbarHostState: SnackbarHostState,
    sharedCoroutineScope: CoroutineScope
) {
    navigation<ProfileGraphRoute>(startDestination = ProfileScreenRoute) {
        composable<ProfileScreenRoute> {
            val profileViewModel =
                it.sharedViewModel<ProfileViewModel>(navController = mainNavController)
            ProfileScreen(profileViewModel, mainNavController, snackbarHostState)
        }
        composable<SettingsScreenRoute> {
            val settingsViewModel =
                it.sharedViewModel<SettingsViewModel>(navController = mainNavController)
            SettingsScreen(settingsViewModel, mainNavController, snackbarHostState)
        }
        composable<EditProfileScreenRoute> {
            val profileViewModel =
                it.sharedViewModel<ProfileViewModel>(navController = mainNavController)
            EditProfileScreen(
                profileViewModel,
                mainNavController,
                sharedCoroutineScope,
                snackbarHostState
            )
        }
        composable<CreatePostScreenRoute> {
            val profileViewModel =
                it.sharedViewModel<ProfileViewModel>(navController = mainNavController)
            CreatePostScreen(profileViewModel, mainNavController)
        }
    }
}

@Serializable
object ProfileGraphRoute : RouteType