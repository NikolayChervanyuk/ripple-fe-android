package com.mobi.ripple.feature_app.feature_search.presentation.search

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import androidx.navigation.NavOptions
import com.mobi.ripple.core.presentation.components.RippleInputField
import com.mobi.ripple.core.presentation.components.SearchIcon
import com.mobi.ripple.core.presentation.components.SimpleUserItem
import com.mobi.ripple.core.presentation.profile.ProfileScreenRoute
import com.mobi.ripple.core.util.RouteType
import com.mobi.ripple.feature_app.feature_profile.presentation.profile.profile.PersonalProfileScreenRoute
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import kotlinx.serialization.Serializable

@Composable
fun SearchScreen(
    viewModel: SearchViewModel,
    navController: NavHostController,
    sharedCoroutineScope: CoroutineScope,
    snackbarHostState: SnackbarHostState
) {
    val state = viewModel.state.collectAsStateWithLifecycle()
    val focusRequester = remember { FocusRequester() }
    LaunchedEffect(key1 = true) {
        focusRequester.requestFocus()
        viewModel.eventFlow.collectLatest { event ->
            when (event) {
                is SearchViewModel.UiEvent.SearchError -> {
                    sharedCoroutineScope.launch {
                        snackbarHostState
                            .showSnackbar("Service unavailable, try again later")
                    }
                }

                is SearchViewModel.UiEvent.UserItemClicked -> {
                    if (event.isMe) navController.navigate(PersonalProfileScreenRoute)
                    else navController.navigate(ProfileScreenRoute(event.username))
                }
            }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.surface)
    ) {
        RippleInputField(
            modifier = Modifier
                .padding(15.dp)
                .focusRequester(focusRequester),
            leadingIcon = {
                SearchIcon(
                    modifier = Modifier
                        .size(18.dp)
                        .offset(x = (-3).dp)
                )
            },
            alwaysShowLeadingIcon = true,
            placeholder = "Search users",
            onTextChanged = { viewModel.onEvent(SearchEvent.SearchTextChanged(it)) }
        )
        LazyColumn(
            modifier = Modifier.fillMaxSize()
        ) {
            state.value.foundUsersList.onEach { simpleUserModel ->
                item {
                    SimpleUserItem(
                        userModel = simpleUserModel.asSimpleUserItemModel(),
                        onClick = { userId, username ->
                            viewModel.onEvent(SearchEvent.UserItemClicked(userId, username))
                        }
                    )
                }
            }
        }
    }
}

@Serializable
object SearchScreenRoute : RouteType