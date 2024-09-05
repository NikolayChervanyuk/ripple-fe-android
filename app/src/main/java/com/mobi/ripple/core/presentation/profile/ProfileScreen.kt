package com.mobi.ripple.core.presentation.profile

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import com.mobi.ripple.R
import com.mobi.ripple.core.presentation.components.DefaultHeader
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
            }
        }
    }
    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        DefaultHeader(
            onBackButtonClicked = { navController.popBackStack() },
            title = state.value.userProfileInfoState.value.userName,
            actionComposable = { OthersProfileAction(onClick = { TODO() }) }
        )
        Column(
            modifier = Modifier.fillMaxSize()
        ) {
            //Header should collapse
            ProfileHeaderSection(
                userProfileInfoModel = state.value.userProfileInfoState.value,
                profilePicture = state.value.userProfilePicture.value,
                onPfpClicked = { TODO("Show image in dialog") }
            )
            ProfilePostsSection(
                state.value.userProfileSimplePosts
            )
        }
    }

}

@Composable
private fun OthersProfileAction(modifier: Modifier = Modifier, onClick: () -> Unit) {
    var wasBackClicked = false;
    Icon(
        modifier = Modifier
            .size(24.dp)
            .padding(2.dp)
            .clickable {
                if (!wasBackClicked) {
                    wasBackClicked = true
                    onClick()
                }
            },
        painter = painterResource(id = R.drawable.dots_icon),
        tint = MaterialTheme.colorScheme.onSurface,
        contentDescription = "options icon"
    )
}

@Serializable
data class ProfileScreenRoute(
    val username: String
)