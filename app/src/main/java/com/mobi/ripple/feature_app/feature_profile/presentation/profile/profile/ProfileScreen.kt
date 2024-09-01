package com.mobi.ripple.feature_app.feature_profile.presentation.profile.profile

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import com.mobi.ripple.core.util.RouteType
import com.mobi.ripple.feature_app.feature_profile.presentation.profile.components.ProfileHeaderSection
import com.mobi.ripple.feature_app.feature_profile.presentation.profile.components.ProfilePostsSection
import com.mobi.ripple.feature_app.feature_profile.presentation.profile.post.CreatePostScreenRoute
import com.mobi.ripple.feature_app.feature_profile.presentation.profile.settings.SettingsScreenRoute
import kotlinx.coroutines.flow.collectLatest
import kotlinx.serialization.Serializable

@Composable
fun ProfileScreen(
    viewModel: ProfileViewModel,
    navController: NavHostController,
    snackbarHostState: SnackbarHostState
) {
    val state = viewModel.state.collectAsStateWithLifecycle()
    LaunchedEffect(key1 = true) {
        viewModel.eventFlow.collectLatest { event ->
            when (event) {
                is ProfileViewModel.UiEvent.ShowSnackbar -> {
                    snackbarHostState.showSnackbar(event.message)
                }
                is ProfileViewModel.UiEvent.SettingsClicked -> {
                    navController.navigate(SettingsScreenRoute)
                }

                else -> {}
            }
        }
    }

    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        ProfileHeaderSection(
            userProfileInfoModel = state.value.userProfileInfoState.value,
            profilePicture = state.value.userProfilePicture.value,
            onSettingsClicked = { viewModel.onEvent(ProfileEvent.SettingsClicked) },
            onUploadPfpRequested = { imageUri ->
                viewModel.onEvent(ProfileEvent.UploadPfpRequested(imageUri))
            },
            onDeletePfpRequested = { viewModel.onEvent(ProfileEvent.DeletePfpRequested) },
            onUploadPostRequested = {
                viewModel.onEvent(ProfileEvent.UploadPostRequested(it))
                navController.navigate(CreatePostScreenRoute)
            }
        )
        ProfilePostsSection(state.value.userProfileSimplePosts)
    }
}

@Serializable
object ProfileScreenRoute : RouteType
