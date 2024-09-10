package com.mobi.ripple.core.presentation.followers_following

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import com.mobi.ripple.core.presentation.components.DefaultHeader
import com.mobi.ripple.core.presentation.components.SimpleUserItem
import com.mobi.ripple.core.presentation.profile.ProfileScreenRoute
import com.mobi.ripple.core.theme.Shapes
import com.mobi.ripple.feature_app.feature_profile.presentation.profile.profile.PersonalProfileScreenRoute
import com.mobi.ripple.feature_app.feature_search.presentation.search.SearchScreenRoute
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import kotlinx.serialization.Serializable

@Composable
fun FollowersFollowingScreen(
    username: String,
    getType: GetType,
    viewModel: FollowersFollowingViewModel,
    navController: NavHostController
) {
    val state = viewModel.state.collectAsStateWithLifecycle()
    val listState = rememberLazyListState()
    val coroutineScope = rememberCoroutineScope()

    val subHeaderText = when (getType) {
        GetType.FOLLOWERS -> "Followers"
        GetType.FOLLOWING -> "Follows"
    }
    LaunchedEffect(key1 = true) {
        viewModel.onEvent(FollowersFollowingEvent.InitScreen(username, getType))
        viewModel.eventFlow.collectLatest { event ->
            when (event) {
                is FollowersFollowingViewModel.UiEvent.BackButtonClicked -> {
                    navController.popBackStack()
                }

                is FollowersFollowingViewModel.UiEvent.ExploreUsersClicked -> {
                    navController.navigate(SearchScreenRoute)
                }

                is FollowersFollowingViewModel.UiEvent.UserSelected -> {
                    coroutineScope.launch {
                        if (event.isMe) {
                            navController.navigate(PersonalProfileScreenRoute)
                            return@launch
                        }
                        navController.navigate(ProfileScreenRoute(event.selectedUsername))
                    }
                }
            }
        }
    }
    Column {
        DefaultHeader(
            onBackButtonClicked = { navController.popBackStack() },
            title = if (username.length > 20) {
                username.substring(0..20) + "..."
            } else username
        )
        DefaultHeader(
            modifier = Modifier
                .fillMaxWidth()
                .background(MaterialTheme.colorScheme.background)
                .padding(horizontal = 6.dp, vertical = 5.dp),
            title = subHeaderText,
            height = null,
            textStyle = MaterialTheme.typography.titleSmall
        )
        LazyColumn(
            modifier = Modifier
                .background(MaterialTheme.colorScheme.surface)
                .fillMaxSize(),
            state = listState,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            when (getType) {
                GetType.FOLLOWERS -> {
                    if (state.value.followersList.isEmpty()) {
                        if (state.value.isMeState.value){
                            emptyListMessage("You have no followers")
                        } else emptyListMessage("@$username has no followers")
                    } else {
                        //TODO!: Implement pagination
                        items(state.value.followersList) {
                            SimpleUserItem(userModel = it.asSimpleUserItemModel()) { _, username ->
                                viewModel.onEvent(FollowersFollowingEvent.UserSelected(username))
                            }
                        }
                    }
                }

                GetType.FOLLOWING -> {
                    if (state.value.followingList.isEmpty()) {
                        if(state.value.isMeState.value) {
                            emptyListMessage("You don't follow anyone yet")
                            exploreUsersButton( onExploreUsersClicked = {
                                viewModel.onEvent(FollowersFollowingEvent.ExploreUsersClicked)
                            })
                        } else emptyListMessage("@$username does not follow anyone")
                    } else {
                        items(state.value.followingList) {
                            SimpleUserItem(userModel = it.asSimpleUserItemModel()) { _, username ->
                                viewModel.onEvent(FollowersFollowingEvent.UserSelected(username))
                            }
                        }
                    }
                }
            }
        }
    }
}

private fun LazyListScope.emptyListMessage(message: String) {
    item {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(
                    start = 16.dp,
                    end = 16.dp,
                    top = 60.dp
                ),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = message,
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                textAlign = TextAlign.Center
            )
        }
    }
}

private fun LazyListScope.exploreUsersButton(
    onExploreUsersClicked: () -> Unit
) {
    item {

        Text(
            modifier = Modifier
                .padding(top = 30.dp)
                .background(
                    color = MaterialTheme.colorScheme.onSurface,
                    shape = Shapes.medium
                )
                .clickable(
                    interactionSource = remember { MutableInteractionSource() },
                    indication = null
                ) { onExploreUsersClicked() }
                .padding(horizontal = 12.dp, vertical = 12.dp),
            text = "Explore users",
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.background,
            textAlign = TextAlign.Center
        )
    }
}
@Serializable
data class FollowersFollowingScreenRoute(val username: String, val getType: GetType)

@Serializable
enum class GetType {
    FOLLOWERS,
    FOLLOWING
}