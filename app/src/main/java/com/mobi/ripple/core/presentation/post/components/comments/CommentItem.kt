package com.mobi.ripple.core.presentation.post.components.comments

import androidx.compose.animation.core.animateIntAsState
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.paging.LoadState
import androidx.paging.PagingData
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.itemKey
import com.mobi.ripple.GlobalAppManager
import com.mobi.ripple.R
import com.mobi.ripple.core.presentation.components.CircularProgressIndicatorRow
import com.mobi.ripple.core.presentation.components.HeartIcon
import com.mobi.ripple.core.presentation.components.MediumButton
import com.mobi.ripple.core.presentation.components.OptionItem
import com.mobi.ripple.core.presentation.components.ProfilePicture
import com.mobi.ripple.core.presentation.effects.bounceClick
import com.mobi.ripple.core.presentation.post.model.CommentModel
import com.mobi.ripple.core.presentation.post.model.ReplyModel
import com.mobi.ripple.core.theme.ErrorRed
import com.mobi.ripple.core.theme.LikeRed
import com.mobi.ripple.core.theme.Shapes
import com.mobi.ripple.core.util.FormattableNumber
import com.mobi.ripple.core.util.InstantPeriodTransformer
import kotlinx.coroutines.flow.Flow

@Composable
fun CommentItem(
    comment: CommentModel,
    repliesFlow: Flow<PagingData<ReplyModel>>,
    onPfpClicked: (username: String) -> Unit,
    onCommentAction: (CommentAction) -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
    ) {
        CommentLayout(
            comment = comment,
            onPfpClicked = onPfpClicked,
            onCommentAction = onCommentAction
        )

        val replyItemHeightDp = remember { 100 }
        val repliesLazyItems = repliesFlow.collectAsLazyPagingItems()
        val animateHeightDp by animateIntAsState(
            targetValue = repliesLazyItems.itemCount * replyItemHeightDp,
            label = "height for reply items"
        )
        if (repliesLazyItems.itemCount > 0) {
            LazyColumn(
                modifier = Modifier
                    .heightIn(max = animateHeightDp.dp)
                    .height(animateHeightDp.dp)
                    .padding(start = 36.dp),
                userScrollEnabled = false
            ) {
                if (repliesLazyItems.loadState.refresh is LoadState.Loading) {
                    item { CircularProgressIndicatorRow() }
                }

                items(count = repliesLazyItems.itemCount,
                    key = repliesLazyItems.itemKey { it.replyId }
                ) { index ->
                    val item = repliesLazyItems[index]
                    item?.let {
                        ReplyItem(
                            modifier = Modifier.height(replyItemHeightDp.dp),
                            commentId = comment.commentId,
                            onPfpClicked = onPfpClicked,
                            replyModel = it,
                            onCommentAction = onCommentAction
                        )
                    }
                }
                if (repliesLazyItems.loadState.append is LoadState.Loading) {
                    item { CircularProgressIndicatorRow() }
                }
            }
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun CommentLayout(
    comment: CommentModel,
    onPfpClicked: (username: String) -> Unit,
    onCommentAction: (CommentAction) -> Unit
) {
    val showMyCommentOptions = remember { mutableStateOf(false) }
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 16.dp)
    ) {
        ProfilePicture(
            modifier = Modifier
                .padding(end = 8.dp)
                .clip(CircleShape)
                .combinedClickable(
                    onClick = { onPfpClicked(comment.authorUsername) },
                    onLongClick = {
                        GlobalAppManager.storedUsername?.let { storedUsername ->
                            if (storedUsername == comment.authorUsername) {
                                showMyCommentOptions.value = true
                            }
                        }
                    }
                )
                .size(31.dp),
            borderWidthDp = 1.dp,
            profilePicture = comment.authorProfilePicture
        )
        Column {
            CommentHeaderTexts(
                modifier = Modifier.padding(bottom = 2.dp),
                fullName = comment.authorName,
                username = comment.authorUsername
            )
            //TODO: longer text should be expandable
            Text(
                modifier = Modifier,
                text = comment.comment,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onBackground
            )
            Text(
                modifier = Modifier
                    .clickable {
                        onCommentAction(
                            CommentAction.NewReply(
                                comment.commentId,
                                "Replying to ${comment.authorUsername}"
                            )
                        )
                    }
                    .padding(top = 4.dp),
                text = "Reply",
                style = MaterialTheme.typography.labelMedium
                    .copy(fontWeight = FontWeight.Bold, letterSpacing = 0.5.sp),
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            if (comment.repliesCount > 0) {
                Text(
                    modifier = Modifier
                        .padding(start = 42.dp)
                        .clickable {
                            onCommentAction(
                                CommentAction.LoadReplies(comment.commentId)
                            )
                        }
                        .padding(vertical = 6.dp),
                    text = "View ${FormattableNumber.format(comment.repliesCount)} comment" +
                            if(comment.repliesCount > 1) "s" else "",
                    style = MaterialTheme.typography.labelMedium
                        .copy(fontWeight = FontWeight.Bold, letterSpacing = 0.5.sp),
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
            }
        }
        Spacer(modifier = Modifier.weight(1f))
        CommentRightColumn(
            modifier = Modifier.padding(end = 4.dp),
            comment = comment,
            onLikeClicked = {
                onCommentAction(
                    CommentAction.LikeComment(comment.commentId)
                )
            }
        )
    }
    if (showMyCommentOptions.value) {
        MyCommentOptions(
            onDismissRequest = { showMyCommentOptions.value = false },
            onEdit = {
                onCommentAction(
                    CommentAction.EditComment(
                        commentId = comment.commentId,
                        actionMessage = "Editing comment",
                        text = comment.comment
                    )
                )
            },
            onDelete = {
                onCommentAction(
                    CommentAction.DeleteComment(commentId = comment.commentId)
                )
            }
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun MyCommentOptions(
    onDismissRequest: () -> Unit,
    onEdit: () -> Unit,
    onDelete: () -> Unit
) {
    val showDeleteConfirmationDialog = remember { mutableStateOf(false) }
    ModalBottomSheet(
        dragHandle = { BottomSheetDragHandle(text = "Comment actions") },
        onDismissRequest = { onDismissRequest() }
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp)
        ) {
            OptionItem(
                iconId = R.drawable.edit_pen_icon,
                text = "Edit comment",
                onClick = {
                    onEdit()
                    onDismissRequest()
                }
            )
            OptionItem(
                iconId = R.drawable.delete_icon,
                text = "Delete comment",
                color = ErrorRed,
                onClick = { showDeleteConfirmationDialog.value = true }
            )
            if (showDeleteConfirmationDialog.value) {
                AlertDialog(
                    shape = Shapes.medium,
                    containerColor = MaterialTheme.colorScheme.surface,
                    onDismissRequest = { showDeleteConfirmationDialog.value = false },
                    title = {
                        Text(
                            text = "Delete comment?",
                            style = MaterialTheme.typography.titleMedium,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    },
                    dismissButton = {

                        MediumButton(
                            modifier = Modifier.clip(Shapes.small),
                            containerColor = Color.Transparent,
                            text = "Cancel",
                            onClick = { showDeleteConfirmationDialog.value = false }
                        )
                    },
                    confirmButton = {
                        MediumButton(
                            modifier = Modifier.clip(Shapes.small),
                            containerColor = ErrorRed,
                            textColor = MaterialTheme.colorScheme.surface,
                            text = "Confirm",
                            onClick = {
                                onDelete()
                                showDeleteConfirmationDialog.value = false
                                onDismissRequest()
                            })
                    }
                )
            }
        }
    }
}

@Composable
private fun CommentHeaderTexts(
    modifier: Modifier = Modifier,
    fullName: String?,
    username: String
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Start
    ) {
        val startText = fullName ?: username
        fullName?.let {
            HeaderStartText(text = startText)
        } ?: HeaderStartText(text = "@$startText")
        fullName?.let {
            if (username.length <= 20) {
                Spacer(modifier = Modifier.width(5.dp))
                HeaderEndText(text = "@${username}")
            }
        }
    }
}

@Composable
private fun HeaderStartText(modifier: Modifier = Modifier, text: String) {
    Text(
        modifier = modifier,
        text = text,
        style = MaterialTheme.typography.titleSmall,
        color = MaterialTheme.colorScheme.onSurface
    )
}

@Composable
private fun HeaderEndText(text: String) {
    Text(
        text = text,
        style = MaterialTheme.typography.bodyMedium,
        color = MaterialTheme.colorScheme.onSurfaceVariant
    )
}

@Composable
private fun CommentRightColumn(
    modifier: Modifier = Modifier,
    comment: CommentModel,
    onLikeClicked: () -> Unit
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            modifier = Modifier.padding(bottom = 4.dp),
            text = InstantPeriodTransformer.transformToPassedTimeString(comment.createdDate),
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            HeartIcon(
                modifier = Modifier
                    .size(22.dp)
                    .bounceClick { onLikeClicked() },
                tint = if (comment.liked) LikeRed
                else MaterialTheme.colorScheme.onSurfaceVariant
            )
            Text(
                modifier = Modifier,
                text = FormattableNumber.format(comment.likesCount),
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}
