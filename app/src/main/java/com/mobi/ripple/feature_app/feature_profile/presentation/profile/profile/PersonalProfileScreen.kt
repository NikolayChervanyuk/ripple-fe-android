package com.mobi.ripple.feature_app.feature_profile.presentation.profile.profile

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.input.nestedscroll.NestedScrollConnection
import androidx.compose.ui.input.nestedscroll.NestedScrollSource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.itemKey
import com.mobi.ripple.core.presentation.followers_following.FollowersFollowingScreenRoute
import com.mobi.ripple.core.presentation.followers_following.GetType
import com.mobi.ripple.core.presentation.posts.PostsScreenRoute
import com.mobi.ripple.core.presentation.profile.components.ProfilePostsSection
import com.mobi.ripple.core.presentation.profile.components.SimplePostItem
import com.mobi.ripple.core.util.RouteType
import com.mobi.ripple.feature_app.feature_profile.presentation.profile.components.ProfileHeaderSection
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
    val state = viewModel.state.collectAsStateWithLifecycle()

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

                is PersonalProfileViewModel.UiEvent.FollowersClicked -> {
                    navController.navigate(
                        FollowersFollowingScreenRoute(
                            state.value.userProfileInfoState.value.userName,
                            GetType.FOLLOWERS
                        )
                    )
                }

                is PersonalProfileViewModel.UiEvent.FollowingClicked -> {
                    navController.navigate(
                        FollowersFollowingScreenRoute(
                            state.value.userProfileInfoState.value.userName,
                            GetType.FOLLOWING
                        )
                    )
                }

                else -> {}
            }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxWidth(),
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
            },
            onFollowersClicked = { viewModel.onEvent(PersonalProfileEvent.FollowersClicked) },
            onFollowingClicked = { viewModel.onEvent(PersonalProfileEvent.FollowingClicked) }
        )
        ProfilePostsSection(
            postsFlow = state.value.userProfileSimplePostsFlow,
            onPostClicked = {index, simplePostModel ->
                navController.navigate(
                    PostsScreenRoute(index, simplePostModel.id , simplePostModel.authorId)
                )
            }
        )
    }
}

@Serializable
object PersonalProfileScreenRoute : RouteType
