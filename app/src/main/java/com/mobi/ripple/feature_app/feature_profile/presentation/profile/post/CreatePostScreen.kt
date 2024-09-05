package com.mobi.ripple.feature_app.feature_profile.presentation.profile.post

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import com.mobi.ripple.core.presentation.components.DefaultDialog
import com.mobi.ripple.core.presentation.components.DefaultHeader
import com.mobi.ripple.core.presentation.components.RippleMultilineInputField
import com.mobi.ripple.core.util.BitmapUtils
import com.mobi.ripple.feature_app.feature_profile.presentation.profile.profile.PersonalProfileEvent
import com.mobi.ripple.feature_app.feature_profile.presentation.profile.profile.PersonalProfileScreenRoute
import com.mobi.ripple.feature_app.feature_profile.presentation.profile.profile.PersonalProfileViewModel
import com.mobi.ripple.feature_auth.presentation.components.LargeButton
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import kotlinx.serialization.Serializable

@Composable
fun CreatePostScreen(
    viewModel: PersonalProfileViewModel,
    navController: NavHostController,
    sharedCoroutineScope: CoroutineScope,
    snackbarHostState: SnackbarHostState
) {

    val state = viewModel.state.collectAsStateWithLifecycle()
    LaunchedEffect(true) {
        viewModel.eventFlow.collectLatest { event ->
            when (event) {
                is PersonalProfileViewModel.UiEvent.ShowSnackbar -> {
                    sharedCoroutineScope.launch {
                        snackbarHostState.showSnackbar(event.message)
                    }
                }

                is PersonalProfileViewModel.UiEvent.UiNewPostEvent.NewPostPostedSuccessfully -> {
                    //TODO: add new post to post list
                    sharedCoroutineScope.launch {
                        snackbarHostState.showSnackbar("Post uploaded successfully")
                        navController.popBackStack(PersonalProfileScreenRoute, false)
                    }
                }

                else -> {}
            }
        }
    }
    DefaultDialog(
        paddingValues = PaddingValues(0.dp),
        onDismissRequest = {
            navController.popBackStack(PersonalProfileScreenRoute, false)
        },
        header = {
            DefaultHeader(
                onBackButtonClicked = { navController.popBackStack(PersonalProfileScreenRoute, false) },
                title = "New post"
            )
        }
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            PostImage(
                imageBytes = state.value
                    .newPostState.value
                    .newPostModelState
                    .value.imageBytes
            )
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 10.dp, vertical = 12.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                CaptionTextField(
                    captionText = state.value
                        .newPostState.value
                        .newPostModelState.value
                        .captionText,
                    onTextChanged = {
                        viewModel.onEvent(PersonalProfileEvent.NewPostScreenEvent.CaptionTextChanged(it))
                    }
                )
                LargeButton(
                    modifier = Modifier.padding(top = 24.dp),
                    text = "Post",
                    onClick = { viewModel.onEvent(PersonalProfileEvent.NewPostScreenEvent.UploadPostRequested) }
                )
            }
        }
    }

}

@Composable
private fun PostImage(imageBytes: ByteArray) {
    val configuration = LocalConfiguration.current
    val screenHeight = configuration.screenHeightDp.dp
    val screenWidth = configuration.screenWidthDp.dp
    val imageSize = if (screenWidth < screenHeight) screenWidth else screenHeight
    Box(
        modifier = Modifier
            .size(screenWidth)
            .background(MaterialTheme.colorScheme.onSurfaceVariant),
        contentAlignment = Alignment.Center,
    ) {
        if(imageBytes.isNotEmpty()) {
            Image(
                bitmap =
                BitmapUtils.convertImageByteArrayToBitmap(imageBytes)
                    .asImageBitmap(),
                contentScale = ContentScale.Fit,
                contentDescription = "Image to be posted"
            )
        }
    }
}

@Composable
private fun CaptionTextField(
    captionText: String,
    onTextChanged: (String) -> Unit
) {
    Column(
        modifier = Modifier.fillMaxWidth()
    ) {
        Text(
            modifier = Modifier.padding(start = 15.dp, bottom = 2.dp),
            text = "Caption",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        RippleMultilineInputField(
            modifier = Modifier.padding(bottom = 10.dp),
            text = captionText,
            minLines = 8,
            onTextChanged = onTextChanged
        )
    }
}

@Serializable
object CreatePostScreenRoute