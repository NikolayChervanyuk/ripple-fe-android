package com.mobi.ripple.core.presentation.post

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.mobi.ripple.GlobalAppManager
import com.mobi.ripple.core.presentation.post.components.PostContent
import com.mobi.ripple.core.presentation.post.components.PostFooter
import com.mobi.ripple.core.presentation.post.components.PostHeader
import com.mobi.ripple.core.presentation.post.components.comments.CommentAction
import com.mobi.ripple.core.presentation.post.components.comments.CommentsBottomSheet
import com.mobi.ripple.core.presentation.post.model.PostModel
import com.mobi.ripple.core.presentation.profile.ProfileScreenRoute
import com.mobi.ripple.feature_app.feature_profile.presentation.profile.profile.PersonalProfileScreenRoute
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PostItem(
    modifier: Modifier = Modifier,
    postModel: PostModel,
    navController: NavHostController,
    snackbarHost: SnackbarHostState,
    onStateMutation: (PostModel) -> Unit
) {

    val coroutineScope = rememberCoroutineScope()
    val model by remember { mutableStateOf(postModel) } //.viewModel.state.collectAsStateWithLifecycle()
    val showCommentsBottomSheet = remember { mutableStateOf(false) }

    val refreshTrigger = remember { mutableStateOf(false) }

    val commentOperationInProgress = remember { mutableStateOf(false) }

    LaunchedEffect(key1 = true) {
        model.eventFlow.collectLatest { event ->
            when (event) {
                is PostModel.UiEvent.ShowSnackBar -> {
                    snackbarHost.showSnackbar(event.message)
                }

                is PostModel.UiEvent.ShowCommentsSection -> {
                    showCommentsBottomSheet.value = true
                }

                is PostModel.UiEvent.CommentOperationSuccessful -> {
                    event.message?.let { snackbarHost.showSnackbar(it) }
                    commentOperationInProgress.value = false
                    if (event.triggerRefresh) {
                        refreshTrigger.value = !refreshTrigger.value
                    }
                }

                is PostModel.UiEvent.CommentOperationFailure -> {
                    snackbarHost.showSnackbar(event.message)
                    commentOperationInProgress.value = false
                }
                is PostModel.UiEvent.StateMutated -> {
                    onStateMutation(model)
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
            onProfileNavigationRequest = { requestedUsername ->
                navigateToUser(navController, requestedUsername)
            },
            postModel = model
        )
        PostContent(
//            postSimpleUser = state.value.postSimpleUserModel.value,
            postModel = model
        )
        PostFooter(
//            postSimpleUser = state.value.postSimpleUserModel.value,
            postModel = model,
            likesCount = model.likesCount.longValue,
            isLiked = model.liked.value,
            commentsCount = model.commentsCount.longValue,
            onLikeClicked = {
                coroutineScope.launch {
                    model.onEvent(PostEvent.PostLikeClicked(model.id.value))
                }
            },
            onCommentsClicked = {
                coroutineScope.launch {
                    model.onEvent(PostEvent.PostCommentsClicked(model.id.value))
                }
            },
            onShareClicked = {
                coroutineScope.launch {
                    model.onEvent(PostEvent.PostShareClicked(model.id.value))
                }
            }
        )
    }

    //TODO: During loading of posts in PostsScreen, show state gets reset. Move this to PostsScreen
    CommentsBottomSheet(
        postId = model.id.value,
        show = showCommentsBottomSheet.value,
        commentsFlow = model.postCommentsFlow.value,
        commentsCount = model.commentsCount.longValue,
        refreshTrigger = refreshTrigger.value,
        onProfileNavigationRequest = { username -> navigateToUser(navController, username) },
        operationInProgress = commentOperationInProgress.value,
//        sheetState = commentsSheetState,
        onDismissRequest = { showCommentsBottomSheet.value = false },
        onCommentAction = { commentAction ->
            if (commentAction !is CommentAction.LikeComment &&
                commentAction !is CommentAction.LikeReply &&
                commentAction !is CommentAction.LoadReplies
            ) {
                commentOperationInProgress.value = true
            }
            coroutineScope.launch {
                model.onEvent(
                    PostEvent.CommentActionRequested(model.id.value, commentAction)
                )
            }
        }
    )
}

private fun navigateToUser(navController: NavHostController, username: String) {
    GlobalAppManager.storedUsername?.let { storedUsername ->
        if (storedUsername == username) {
            navController.navigate(PersonalProfileScreenRoute)
        } else navController.navigate(ProfileScreenRoute(username))
    }
}