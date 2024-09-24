package com.mobi.ripple.core.presentation.post

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.mobi.ripple.core.presentation.post.components.comments.CommentAction
import com.mobi.ripple.core.presentation.post.components.comments.CommentsBottomSheet
import com.mobi.ripple.core.presentation.post.components.PostContent
import com.mobi.ripple.core.presentation.post.components.PostFooter
import com.mobi.ripple.core.presentation.post.components.PostHeader
import com.mobi.ripple.core.presentation.posts.model.PostItemModel
import kotlinx.coroutines.flow.collectLatest

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PostItem(
    modifier: Modifier = Modifier,
    postItemModel: PostItemModel,
    onProfileNavigationRequest: (username: String) -> Unit,
    snackbarHost: SnackbarHostState
) {

    val state = postItemModel.viewModel.state.collectAsStateWithLifecycle()
    val showCommentsBottomSheet = remember { mutableStateOf(false) }
    val commentsSheetState = rememberModalBottomSheetState()

    val refreshTrigger = remember { mutableStateOf(false) }

    val commentOperationInProgress = remember { mutableStateOf(false) }

    LaunchedEffect(key1 = true) {
        postItemModel.viewModel.onEvent(PostEvent.InitPost(postItemModel.postModel))
        postItemModel.viewModel.eventFlow.collectLatest { event ->
            when (event) {
                is PostViewModel.UiEvent.ShowSnackBar -> {
                    snackbarHost.showSnackbar(event.message)
                }

                is PostViewModel.UiEvent.ShowCommentsSection -> {
                    showCommentsBottomSheet.value = true
                }

                is PostViewModel.UiEvent.CommentOperationSuccessful -> {
                    event.message?.let { snackbarHost.showSnackbar(it) }
                    commentOperationInProgress.value = false
                    if (event.triggerRefresh) {
                        refreshTrigger.value = !refreshTrigger.value
                    }
                }

                is PostViewModel.UiEvent.CommentOperationFailure -> {
                    snackbarHost.showSnackbar(event.message)
                    commentOperationInProgress.value = false
                }
            }
        }
    }
    Column(
        modifier = modifier
            .padding(bottom = 12.dp)
            .drawBehind {
                drawLine(
                    color = Color.LightGray,
                    start = Offset.Zero,
                    end = Offset(size.width, 0f),
                    strokeWidth = 2f
                )
            }
    ) {
        PostHeader(
//            postSimpleUser = state.value.postSimpleUserModel.value,
            postModel = state.value.postModel.value
        )
        PostContent(
//            postSimpleUser = state.value.postSimpleUserModel.value,
            postModel = state.value.postModel.value
        )
        PostFooter(
//            postSimpleUser = state.value.postSimpleUserModel.value,
            postModel = state.value.postModel.value,
            onLikeClicked = {
                postItemModel.viewModel
                    .onEvent(PostEvent.PostLikeClicked(state.value.postModel.value.id))
            },
            onCommentsClicked = {
                postItemModel.viewModel
                    .onEvent(PostEvent.PostCommentsClicked(state.value.postModel.value.id))
            },
            onShareClicked = {
                postItemModel.viewModel.onEvent(
                    PostEvent.PostShareClicked(postItemModel.postModel.id)
                )
            }
        )
    }

    CommentsBottomSheet(
        show = showCommentsBottomSheet.value,
        commentsFlow = state.value.postCommentsFlow,
        commentsCount = state.value.postModel.value.commentsCount,
        refreshTrigger = refreshTrigger.value,
        personalPfp = state.value.personalPfp.value,
        onProfileNavigationRequest = onProfileNavigationRequest,
        operationInProgress = commentOperationInProgress.value,
        sheetState = commentsSheetState,
        onDismissRequest = { showCommentsBottomSheet.value = false },
        onCommentAction = { commentAction ->
            if (commentAction !is CommentAction.LikeComment &&
                commentAction !is CommentAction.LikeReply
            ) {
                commentOperationInProgress.value = true
            }
            postItemModel.viewModel.onEvent(
                PostEvent.CommentActionRequested(postItemModel.postModel.id, commentAction)
            )
        }
    )
}