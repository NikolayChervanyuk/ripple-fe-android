package com.mobi.ripple.core.presentation.profile

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import com.mobi.ripple.core.presentation.components.ActionButton
import com.mobi.ripple.core.presentation.components.DefaultHeader
import com.mobi.ripple.core.presentation.followers_following.FollowersFollowingScreenRoute
import com.mobi.ripple.core.presentation.followers_following.GetType
import com.mobi.ripple.core.presentation.posts.PostsScreenRoute
import com.mobi.ripple.core.presentation.profile.components.ProfileHeaderSection
import com.mobi.ripple.core.presentation.profile.components.ProfilePostsSection
import kotlinx.coroutines.flow.collectLatest
import kotlinx.serialization.Serializable

@Composable
fun ProfileScreen(
    username: String,
    viewModel: ProfileViewModel,
    navController: NavHostController,
    snackbarHostState: SnackbarHostState
) {
    val state = viewModel.state.collectAsStateWithLifecycle()
    LaunchedEffect(key1 = true) {
        viewModel.onEvent(ProfileEvent.InitializeUser(username))
        viewModel.eventFlow.collectLatest { event ->
            when (event) {
                is ProfileViewModel.UiEvent.ShowSnackbar -> {
                    snackbarHostState.showSnackbar(event.message)
                }

                is ProfileViewModel.UiEvent.BackButtonClicked -> {
                    navController.popBackStack()
                }

                is ProfileViewModel.UiEvent.FollowersClicked -> {
                    navController.navigate(
                        FollowersFollowingScreenRoute(
                            username,
                            GetType.FOLLOWERS
                        )
                    )
                }

                is ProfileViewModel.UiEvent.FollowingClicked -> {
                    navController.navigate(
                        FollowersFollowingScreenRoute(
                            username,
                            GetType.FOLLOWING
                        )
                    )
                }
            }
        }
    }
    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        DefaultHeader(
            onBackButtonClicked = { navController.popBackStack() },
            title = state.value.userProfileInfoState.value.userName,
            actionComposable = {
                ActionButton(
                    modifier = Modifier
                        .size(24.dp)
                        .padding(2.dp),
                    onClick = { TODO() }
                )
            }
        )
        Column(
            modifier = Modifier.fillMaxSize()
        ) {
            //FIXME: Header should collapse
            ProfileHeaderSection(
                userProfileInfo = state.value.userProfileInfoState.value,
                profilePicture = state.value.userProfilePicture.value,
                onPfpClicked = { TODO("Show image in dialog") },
                onFollowStateChangeRequested = { viewModel.onEvent(ProfileEvent.ChangeFollowState) },
                onFollowersClicked = { viewModel.onEvent(ProfileEvent.FollowersClicked) },
                onFollowingClicked = { viewModel.onEvent(ProfileEvent.FollowingClicked) }
            )
            ProfilePostsSection(
                postsFlow = state.value.userProfileSimplePostsFlow,
                onPostClicked = { index, simplePostModel ->
                    navController.navigate(PostsScreenRoute(index, simplePostModel.authorId))
                }
            )
        }
    }
}

@Serializable
data class ProfileScreenRoute(
    val username: String
)