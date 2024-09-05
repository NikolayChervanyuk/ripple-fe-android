package com.mobi.ripple.feature_app.feature_profile.presentation.profile

import androidx.compose.material3.SnackbarHostState
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.mobi.ripple.core.util.RouteType
import com.mobi.ripple.core.util.sharedViewModel
import com.mobi.ripple.feature_app.feature_profile.presentation.profile.post.CreatePostScreen
import com.mobi.ripple.feature_app.feature_profile.presentation.profile.post.CreatePostScreenRoute
import com.mobi.ripple.feature_app.feature_profile.presentation.profile.profile.PersonalProfileScreen
import com.mobi.ripple.feature_app.feature_profile.presentation.profile.profile.PersonalProfileScreenRoute
import com.mobi.ripple.feature_app.feature_profile.presentation.profile.profile.PersonalProfileViewModel
import com.mobi.ripple.feature_app.feature_profile.presentation.profile.settings.EditProfileScreenRoute
import com.mobi.ripple.feature_app.feature_profile.presentation.profile.settings.EditProfileScreen
import com.mobi.ripple.feature_app.feature_profile.presentation.profile.settings.SettingsScreenRoute
import com.mobi.ripple.feature_app.feature_profile.presentation.profile.settings.SettingsScreen
import com.mobi.ripple.feature_app.feature_profile.presentation.profile.settings.SettingsViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.serialization.Serializable

fun NavGraphBuilder.personalProfileGraph(
    mainNavController: NavHostController,
    snackbarHostState: SnackbarHostState,
    sharedCoroutineScope: CoroutineScope
) {
    navigation<PersonalProfileGraphRoute>(startDestination = PersonalProfileScreenRoute) {
        composable<PersonalProfileScreenRoute> {
            val personalProfileViewModel =
                it.sharedViewModel<PersonalProfileViewModel>(navController = mainNavController)
            PersonalProfileScreen(personalProfileViewModel, mainNavController, snackbarHostState)
        }
        composable<SettingsScreenRoute> {
            val settingsViewModel =
                it.sharedViewModel<SettingsViewModel>(navController = mainNavController)
            SettingsScreen(settingsViewModel, mainNavController, snackbarHostState)
        }
        composable<EditProfileScreenRoute> {
            val personalProfileViewModel =
                it.sharedViewModel<PersonalProfileViewModel>(navController = mainNavController)
            EditProfileScreen(
                personalProfileViewModel,
                mainNavController,
                sharedCoroutineScope,
                snackbarHostState
            )
        }
        composable<CreatePostScreenRoute> {
            val personalProfileViewModel =
                it.sharedViewModel<PersonalProfileViewModel>(navController = mainNavController)
            CreatePostScreen(
                personalProfileViewModel,
                mainNavController,
                sharedCoroutineScope,
                snackbarHostState
            )
        }
    }
}

@Serializable
object PersonalProfileGraphRoute : RouteType