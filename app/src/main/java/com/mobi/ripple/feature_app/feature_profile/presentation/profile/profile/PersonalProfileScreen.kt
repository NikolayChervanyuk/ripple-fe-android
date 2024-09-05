package com.mobi.ripple.feature_app.feature_profile.presentation.profile.profile

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.input.nestedscroll.NestedScrollConnection
import androidx.compose.ui.input.nestedscroll.NestedScrollSource
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import com.mobi.ripple.core.util.RouteType
import com.mobi.ripple.feature_app.feature_profile.presentation.profile.components.ProfileHeaderSection
import com.mobi.ripple.core.presentation.profile.components.ProfilePostsSection
import com.mobi.ripple.feature_app.feature_profile.presentation.profile.post.CreatePostScreenRoute
import com.mobi.ripple.feature_app.feature_profile.presentation.profile.settings.SettingsScreenRoute
import kotlinx.coroutines.flow.collectLatest
import kotlinx.serialization.Serializable

@Composable
fun PersonalProfileScreen(
    viewModel: PersonalProfileViewModel,
    navController: NavHostController,
    snackbarHostState: SnackbarHostState
) {
//    var profileHeaderHeight by remember { mutableIntStateOf(240) }
//    var postSectionOffset by remember { mutableIntStateOf(0) }
    val connection = remember {
        object : NestedScrollConnection {
            override fun onPreScroll(
                available: Offset,
                source: NestedScrollSource
            ): Offset {
                return Offset(0f, available.y)
                //return super.onPreScroll(available, source)
            }
        }
    }

    val state = viewModel.state.collectAsStateWithLifecycle()
    LaunchedEffect(key1 = true) {
        viewModel.eventFlow.collectLatest { event ->
            when (event) {
                is PersonalProfileViewModel.UiEvent.ShowSnackbar -> {
                    snackbarHostState.showSnackbar(event.message)
                }

                is PersonalProfileViewModel.UiEvent.SettingsClicked -> {
                    navController.navigate(SettingsScreenRoute)
                }

                is PersonalProfileViewModel.UiEvent.CreatePostRequest -> {
                    navController.navigate(CreatePostScreenRoute)
                }

                else -> {}
            }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
        ,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // FIXME: Header should collapse when scrolling up
        ProfileHeaderSection(
            userProfileInfoModel = state.value.userProfileInfoState.value,
            profilePicture = state.value.userProfilePicture.value,
            onSettingsClicked = { viewModel.onEvent(PersonalProfileEvent.SettingsClicked) },
            onUploadPfpRequested = { imageUri ->
                viewModel.onEvent(PersonalProfileEvent.UploadPfpRequested(imageUri))
            },
            onDeletePfpRequested = { viewModel.onEvent(PersonalProfileEvent.DeletePfpRequested) },
            onCreatePostRequested = { imageUri ->
                viewModel.onEvent(PersonalProfileEvent.CreatePostRequested(imageUri))
            }
        )
        ProfilePostsSection(
            state.value.userProfileSimplePosts
        )
    }
}

@Serializable
object PersonalProfileScreenRoute : RouteType
