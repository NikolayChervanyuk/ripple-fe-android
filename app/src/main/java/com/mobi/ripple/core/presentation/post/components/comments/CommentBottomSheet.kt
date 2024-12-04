package com.mobi.ripple.core.presentation.post.components.comments

import android.app.Activity
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandVertically
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredHeight
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.core.view.WindowCompat
import androidx.paging.LoadState
import androidx.paging.PagingData
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.itemKey
import androidx.paging.map
import com.mobi.ripple.GlobalAppManager
import com.mobi.ripple.core.presentation.components.CancelButton
import com.mobi.ripple.core.presentation.components.CircularProgressIndicatorRow
import com.mobi.ripple.core.presentation.components.InvalidFieldMessage
import com.mobi.ripple.core.presentation.components.PictureFrame
import com.mobi.ripple.core.presentation.components.RippleInputField
import com.mobi.ripple.core.presentation.components.SendIcon
import com.mobi.ripple.core.presentation.effects.fadingEdge
import com.mobi.ripple.core.presentation.post.model.CommentModel
import com.mobi.ripple.core.presentation.post.model.asReplyModel
import com.mobi.ripple.core.util.BitmapUtils
import com.mobi.ripple.core.util.FormattableNumber
import com.mobi.ripple.core.util.validator.FieldValidator
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import timber.log.Timber

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CommentsBottomSheet(
    postId: String,
    commentsFlow: Flow<PagingData<CommentModel>>,
    commentsCount: Long,
    refreshTrigger: Boolean,
    onProfileNavigationRequest: (username: String) -> Unit,
    show: Boolean,
    operationInProgress: Boolean,
//    sheetState: SheetState,
    onDismissRequest: () -> Unit,
    onCommentAction: (CommentAction) -> Unit,
) {

    val coroutineScope = rememberCoroutineScope()
    val sheetState = rememberModalBottomSheetState()
    val window = (LocalContext.current as Activity).window
    WindowCompat.setDecorFitsSystemWindows(window, true)

    val density = LocalDensity.current

    val topPadding = 18.dp
    val commentTextFieldHeight = remember { mutableStateOf(50.dp) }
    val commentsLazyPaging = commentsFlow.collectAsLazyPagingItems()

    val commentActionState: MutableState<CommentAction> = remember {
        mutableStateOf(CommentAction.NewComment(""))
    }

    LaunchedEffect(key1 = refreshTrigger) {
        commentsLazyPaging.refresh()
    }

    AnimatedVisibility(
        visible = show,
        enter = expandVertically(
            expandFrom = Alignment.Bottom,
            animationSpec = tween(200)
        ),
        exit = shrinkVertically(
            shrinkTowards = Alignment.Bottom,
            animationSpec = tween(200)
        )
    ) {
        ModalBottomSheet(
            modifier = Modifier
                .padding(top = topPadding),
            onDismissRequest = { onDismissRequest() },
            sheetState = sheetState,
            dragHandle = {
                BottomSheetDragHandle(
                    "${
                        FormattableNumber.format(
                            number = commentsCount,
                            shouldTrimOnZero = true
                        )
                    } comments"
                )
            }
        ) {
            Column(
                Modifier.height(LocalConfiguration.current.screenHeightDp.dp + topPadding)
            ) {
                val fade = Brush.verticalGradient(
                    0.95f to Color.White,
                    1f to Color.Transparent
                )
                LazyColumn(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(
                            LocalConfiguration.current.screenHeightDp.dp -
                                    WindowInsets.navigationBars
                                        .asPaddingValues()
                                        .calculateBottomPadding()
                                    - commentTextFieldHeight.value
                        )
                        .fadingEdge(fade)
                        .padding(start = 8.dp, end = 8.dp, top = 6.dp, bottom = topPadding + 4.dp),
                    horizontalAlignment = Alignment.CenterHorizontally

                ) {
                    if (commentsLazyPaging.loadState.refresh is LoadState.NotLoading &&
                        commentsLazyPaging.itemCount == 0
                    ) {
                        item {
                            NoCommentsMessage(
                                modifier = Modifier.padding(top = 32.dp),
                            )
                        }
                    } else if (commentsLazyPaging.loadState.refresh is LoadState.Loading) {
                        item { CircularProgressIndicatorRow() }
                    }

                    items(
                        count = commentsLazyPaging.itemCount,
                        key = commentsLazyPaging.itemKey { it.commentId },
                    ) { index ->
                        val item = commentsLazyPaging[index]
                        item?.let {
                            val commentState = remember { mutableStateOf(it) }
                            CommentItem(
                                comment = commentState.value,
                                onPfpClicked = { username ->
                                    onProfileNavigationRequest(username)
                                },
                                onCommentAction = { commentAction ->
                                    when (commentAction) {
                                        is CommentAction.LikeComment -> {
                                            commentState.value = commentState.value.copy(
                                                likesCount = if (commentState.value.liked) {
                                                    commentState.value.likesCount - 1
                                                } else commentState.value.likesCount + 1,
                                                liked = !commentState.value.liked
                                            )
                                            onCommentAction(commentAction)
                                        }

                                        is CommentAction.EditComment -> {
                                            commentAction.text = it.comment
                                            commentActionState.value = commentAction
                                        }

                                        is CommentAction.DeleteComment -> {
                                            onCommentAction(CommentAction.DeleteComment(it.commentId))
                                        }

                                        is CommentAction.NewReply -> {
                                            commentActionState.value =
                                                CommentAction.NewReply(
                                                    commentId = it.commentId,
                                                    actionMessage = "Reply to ${it.authorUsername}"
                                                )
                                        }

                                        is CommentAction.LoadReplies -> {
                                            coroutineScope.launch {
                                                commentState.value.repliesFlow.value =
                                                    commentState.value.postUseCases
                                                        .getCommentRepliesFlowUseCase(
                                                            postId,
                                                            commentState.value.commentId
                                                        ).map { pagingData ->
                                                            pagingData.map { it.asReplyModel() }
                                                        }
                                                onCommentAction(CommentAction.LoadReplies(it.commentId))
                                            }
                                        }

                                        is CommentAction.LikeReply -> {
                                            onCommentAction(
                                                CommentAction.LikeReply(
                                                    commentAction.commentId,
                                                    commentAction.replyId
                                                )
                                            )
                                        }

                                        is CommentAction.EditReply -> {
                                            commentAction.text = it.comment
                                            commentActionState.value = commentAction
                                            onCommentAction(
                                                CommentAction.EditReply(
                                                    commentAction.commentId,
                                                    commentAction.replyId,
                                                    commentAction.actionMessage,
                                                    commentAction.text
                                                )
                                            )
                                        }

                                        is CommentAction.DeleteReply -> {
                                            onCommentAction(
                                                CommentAction.DeleteReply(
                                                    commentAction.commentId,
                                                    commentAction.replyId
                                                )
                                            )
                                        }
                                    }
                                }
                            )
                        }
                    }
                }
                val screenHeight = LocalConfiguration.current.screenHeightDp.dp
                CommentTextField(
                    modifier = Modifier
                        .onGloballyPositioned {
                            commentTextFieldHeight.value = with(density) { it.size.height.toDp() }
                        }
                        .offset {
                            IntOffset(
                                0,
                                (-sheetState
                                    .requireOffset()
                                    .toInt() + 6.dp.roundToPx() - topPadding.roundToPx())
                                    .coerceAtLeast(-screenHeight.roundToPx() + 100.dp.roundToPx())
                            )
                        },
                    action = commentActionState.value,
                    operationInProgress = operationInProgress,
                    onCancelAction = { commentActionState.value = CommentAction.NewComment("") },
                    onSendText = { text ->
                        commentActionState.value.text = text
                        onCommentAction(commentActionState.value)
                    }
                )
                Box(modifier = Modifier
                    .requiredHeight(
                        WindowInsets.navigationBars
                            .asPaddingValues()
                            .calculateBottomPadding()
                    )
                    .offset {
                        IntOffset(
                            0,
                            -sheetState
                                .requireOffset()
                                .toInt() + 12.dp.roundToPx() - topPadding.roundToPx()
                        )
                    }
                    .background(MaterialTheme.colorScheme.surface)
                    .fillMaxWidth()
                )
            }
        }
    }
}

@Composable
private fun NoCommentsMessage(modifier: Modifier = Modifier) {
    Text(
        modifier = modifier,
        text = "No comments yet",
        style = MaterialTheme.typography.headlineSmall,
        color = MaterialTheme.colorScheme.onSurfaceVariant
    )
}

@Composable
private fun CommentTextField(
    modifier: Modifier = Modifier,
    action: CommentAction,
    operationInProgress: Boolean,
    onCancelAction: () -> Unit,
    onSendText: (String) -> Unit
) {

    val personalPfp = remember { mutableStateOf<ImageBitmap?>(null) }
    LaunchedEffect(key1 = true) {
        personalPfp.value = GlobalAppManager.getProfilePicture()?.let {
            BitmapUtils.convertImageByteArrayToBitmap(it).asImageBitmap()
        }
    }

    val keyboardController = LocalSoftwareKeyboardController.current

    val fade = Brush.verticalGradient(
        0f to Color.Transparent,
        0.1f to Color.White
    )
    val commentText = rememberSaveable { mutableStateOf(action.text ?: "") }

    val sendButtonTint = if (commentText.value.isBlank() || operationInProgress) {
        MaterialTheme.colorScheme.onSurfaceVariant
    } else MaterialTheme.colorScheme.onBackground

    LaunchedEffect(key1 = action) {
        commentText.value = action.text ?: ""
    }
    val showInvalidCommentMessage = rememberSaveable { mutableStateOf(false) }
    Column(
        modifier = modifier
            .fadingEdge(fade)
            .background(MaterialTheme.colorScheme.surface)
            .padding(start = 8.dp, end = 8.dp, top = 12.dp, bottom = 6.dp),
    ) {
        if (operationInProgress) {
            CircularProgressIndicatorRow()
        }
        ActionMessage(
            modifier = Modifier.fillMaxWidth(),
            show = action !is CommentAction.NewComment,
            message = action.actionMessage ?: "",
            onCancelAction = {
                commentText.value = ""
                onCancelAction()
            }
        )
        InvalidCommentMessage(
            modifier = Modifier.padding(bottom = 3.dp),
            show = showInvalidCommentMessage.value
        )
        Row(
            modifier = Modifier
                .background(MaterialTheme.colorScheme.surface)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            PictureFrame(
                modifier = Modifier
                    .padding(end = 8.dp)
                    .size(32.dp),
                picture = personalPfp.value,
                borderWidthDp = 1.dp
            )
            RippleInputField(
                placeholder = "Comment here...",
                text = commentText.value,
                onTextChanged = {
                    if (FieldValidator.isCommentValid(it) || it.isBlank()) {
                        commentText.value = it
                        showInvalidCommentMessage.value = false
                    }
                },
                trailingIcon = {
                    SendIcon(
                        modifier = Modifier
                            .clickable {
                                if (!operationInProgress) {
                                    if (FieldValidator.isCommentValid(commentText.value)) {
                                        keyboardController?.hide()
                                        onSendText(commentText.value)
                                    } else {
                                        showInvalidCommentMessage.value = true
                                    }
                                }
                            }
                            .size(18.dp),
                        tint = sendButtonTint
                    )
                }
            )
        }
    }
}

@Composable
private fun ActionMessage(
    modifier: Modifier = Modifier,
    show: Boolean,
    message: String,
    onCancelAction: () -> Unit
) {
    AnimatedVisibility(
        modifier = modifier,
        visible = show,
        enter = expandVertically(
            expandFrom = Alignment.Bottom,
            animationSpec = tween(200)
        ),
        exit = shrinkVertically(
            shrinkTowards = Alignment.Bottom,
            animationSpec = tween(200)
        )
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 6.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                modifier = Modifier.weight(.9f),
                text = message,
                overflow = TextOverflow.Ellipsis,
                maxLines = 1,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurface
            )
            CancelButton(
                modifier = Modifier
                    .padding(start = 4.dp)
                    .clickable { onCancelAction() }
                    .size(14.dp)
                    .weight(.1f),
                horizontalArrangement = Arrangement.End
            )
        }
    }
}

@Composable
private fun InvalidCommentMessage(
    modifier: Modifier = Modifier,
    show: Boolean
) {
    InvalidFieldMessage(
        modifier = modifier,
        show = show,
        message = "Comment should be less than ${FieldValidator.MAX_COMMENT_LENGTH} characters and not blank",
        enter = expandVertically(
            expandFrom = Alignment.Bottom,
            animationSpec = tween(200)
        ),
        exit = shrinkVertically(
            shrinkTowards = Alignment.Bottom,
            animationSpec = tween(200)
        )
    )

}

@Composable
fun BottomSheetDragHandle(
    text: String
) {
    Column(
        modifier = Modifier
            .padding(14.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier
                .clip(CircleShape)
                .background(color = MaterialTheme.colorScheme.onSurfaceVariant)
                .size(width = 50.dp, height = 5.dp)
                .padding(bottom = 10.dp)
        )
        Text(
            modifier = Modifier.padding(top = 10.dp, bottom = 12.dp),
            text = text,
            style = MaterialTheme.typography.titleSmall,
            color = MaterialTheme.colorScheme.onSurface
        )
    }
}

open class CommentAction(
    open var text: String? = null,
    open val actionMessage: String? = null
) {
    data class NewComment(override var text: String?) : CommentAction()

    data class EditComment(
        val commentId: String,
        override val actionMessage: String,
        override var text: String?
    ) : CommentAction()

    data class DeleteComment(val commentId: String) : CommentAction()

    data class LikeComment(val commentId: String) : CommentAction(null)

    data class LoadReplies(val commentId: String) : CommentAction()

    data class NewReply(
        val commentId: String,
        override val actionMessage: String,
        override var text: String? = null
    ) : CommentAction()

    data class EditReply(
        val commentId: String,
        val replyId: String,
        override val actionMessage: String,
        override var text: String?
    ) : CommentAction()

    data class DeleteReply(val commentId: String, val replyId: String) : CommentAction()

    data class LikeReply(val commentId: String, val replyId: String) : CommentAction(null)
}