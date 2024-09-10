package com.mobi.ripple.core.presentation.post

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.mobi.ripple.core.presentation.post.components.CommentsBottomSheet
import com.mobi.ripple.core.presentation.post.components.PostContent
import com.mobi.ripple.core.presentation.post.components.PostFooter
import com.mobi.ripple.core.presentation.post.components.PostHeader
import com.mobi.ripple.core.presentation.post.model.PostModel
import kotlinx.coroutines.flow.collectLatest

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PostItem(
    modifier: Modifier = Modifier,
    postModel: PostModel,
    viewModel: PostViewModel,
    snackbarHost: SnackbarHostState
) {

    val state = viewModel.state.collectAsStateWithLifecycle()
    val showCommentsBottomSheet = remember { mutableStateOf(false) }
    val commentsSheetState = rememberModalBottomSheetState()

    val refreshTrigger = remember { mutableStateOf(false) }

    LaunchedEffect(key1 = true) {
        viewModel.onEvent(PostEvent.InitPost(postModel))
        viewModel.eventFlow.collectLatest { event ->
            when (event) {
                is PostViewModel.UiEvent.ShowSnackBar -> {
                    snackbarHost.showSnackbar(event.message)
                }

                is PostViewModel.UiEvent.ShowCommentsSection -> {
                    showCommentsBottomSheet.value = true
                }

                is PostViewModel.UiEvent.UploadCommentSuccessful -> {
                    refreshTrigger.value = !refreshTrigger.value
                }
            }
        }
    }
    Column(
        modifier = modifier
    ) {
        PostHeader(
            postSimpleUser = state.value.postSimpleUserModel.value,
            postModel = state.value.postModel.value
        )
        PostContent(
            postSimpleUser = state.value.postSimpleUserModel.value,
            postModel = state.value.postModel.value
        )
        PostFooter(
            postSimpleUser = state.value.postSimpleUserModel.value,
            postModel = state.value.postModel.value,
            onLikeClicked = {
                viewModel
                    .onEvent(PostEvent.PostLikeClicked(state.value.postModel.value.id))
            },
            onCommentsClicked = {
                viewModel
                    .onEvent(PostEvent.PostCommentsClicked(state.value.postModel.value.id))
            },
            onShareClicked = {/* TODO */ }
        )
    }
    CommentsBottomSheet(
        show = showCommentsBottomSheet.value,
        commentsFlow = state.value.postCommentsFlow,
        commentsCount = state.value.postModel.value.commentsCount,
        refreshTrigger = refreshTrigger.value,
        personalPfp = state.value.personalPfp.value,
        sheetState = commentsSheetState,
        onDismissRequest = { showCommentsBottomSheet.value = false },
        onUploadComment = { commentText ->
            viewModel.onEvent(PostEvent.UploadCommentRequested(postModel.id, commentText))
        },
        onCommentLikeClicked = { TODO() }
    )
}

