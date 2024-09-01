package com.mobi.ripple.feature_app.feature_profile.presentation.profile.post

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.navigation.NavHostController
import com.mobi.ripple.core.presentation.DefaultDialog
import com.mobi.ripple.core.presentation.DefaultDialogHeader
import com.mobi.ripple.feature_app.feature_profile.presentation.profile.profile.ProfileScreenRoute
import com.mobi.ripple.feature_app.feature_profile.presentation.profile.profile.ProfileViewModel
import kotlinx.serialization.Serializable

@Composable
fun CreatePostScreen(
    viewModel: ProfileViewModel,
    navController: NavHostController
) {
    LaunchedEffect(true) {

    }
    DefaultDialog(onDismissRequest = {
        navController.popBackStack(ProfileScreenRoute, false)
    },
        header = { DefaultDialogHeader(
            onBackButtonClicked = { navController.popBackStack(ProfileScreenRoute, false) },
            title = "New post")
        }
    ) {
        //TODO: create page for new post
    }

}

@Serializable
object CreatePostScreenRoute