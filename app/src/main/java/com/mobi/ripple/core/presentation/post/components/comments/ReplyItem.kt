package com.mobi.ripple.core.presentation.post.components.comments

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.mobi.ripple.GlobalAppManager
import com.mobi.ripple.R
import com.mobi.ripple.core.presentation.components.HeartIcon
import com.mobi.ripple.core.presentation.components.MediumButton
import com.mobi.ripple.core.presentation.components.OptionItem
import com.mobi.ripple.core.presentation.components.PictureFrame
import com.mobi.ripple.core.presentation.effects.bounceClick
import com.mobi.ripple.core.presentation.post.model.ReplyModel
import com.mobi.ripple.core.theme.ErrorRed
import com.mobi.ripple.core.theme.LikeRed
import com.mobi.ripple.core.theme.Shapes
import com.mobi.ripple.core.util.FormattableNumber
import com.mobi.ripple.core.util.InstantPeriodTransformer

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ReplyItem(
    modifier: Modifier = Modifier,
    commentId: String,
    replyModel: ReplyModel,
    onPfpClicked: (username: String) -> Unit,
    onCommentAction: (CommentAction) -> Unit
) {
    val showMyReplyOptions = remember { mutableStateOf(false) }
    Row(
        modifier = modifier
    ) {
        PictureFrame(
            modifier = Modifier
                .padding(end = 8.dp)
                .clip(CircleShape)
                .combinedClickable(
                    onClick = { onPfpClicked(replyModel.authorUsername) },
                    onLongClick = {
                        GlobalAppManager.storedUsername?.let { storedUsername ->
                            if (storedUsername == replyModel.authorUsername) {
                                showMyReplyOptions.value = true
                            }
                        }
                    }
                )
                .size(31.dp),
            borderWidthDp = 1.dp,
            picture = replyModel.authorProfilePicture
        )
        Column {
            ReplyHeaderTexts(
                modifier = Modifier.padding(bottom = 2.dp),
                fullName = replyModel.authorName,
                username = replyModel.authorUsername
            )
            //TODO: longer text should be expandable
            Text(
                modifier = Modifier,
                text = replyModel.reply,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onBackground
            )
        }
        Spacer(modifier = Modifier.weight(1f))
        ReplyRightColumn(
            modifier = Modifier.padding(end = 7.dp),
            reply = replyModel,
            onLikeClicked = {
                onCommentAction(
                    CommentAction.LikeReply(commentId, replyModel.replyId)
                )
            }
        )
    }
    if (showMyReplyOptions.value) {
        MyReplyOptions(
            onDismissRequest = { showMyReplyOptions.value = false },
            onEdit = {
                onCommentAction(
                    CommentAction.EditReply(
                        commentId = commentId,
                        replyId = replyModel.replyId,
                        actionMessage = "Editing reply",
                        text = replyModel.reply
                    )
                )
            },
            onDelete = {
                onCommentAction(
                    CommentAction.DeleteReply(commentId = commentId, replyId = replyModel.replyId)
                )
            }
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun MyReplyOptions(
    onDismissRequest: () -> Unit,
    onEdit: () -> Unit,
    onDelete: () -> Unit
) {
    val showDeleteConfirmationDialog = remember { mutableStateOf(false) }
    ModalBottomSheet(
        dragHandle = { BottomSheetDragHandle(text = "Reply actions") },
        onDismissRequest = { onDismissRequest() }
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp)
        ) {
            OptionItem(
                iconId = R.drawable.edit_pen_icon,
                text = "Edit reply",
                onClick = {
                    onEdit()
                    onDismissRequest()
                }
            )
            OptionItem(
                iconId = R.drawable.delete_icon,
                text = "Delete reply",
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
                            text = "Delete reply?",
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
private fun ReplyHeaderTexts(
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
private fun ReplyRightColumn(
    modifier: Modifier = Modifier,
    reply: ReplyModel,
    onLikeClicked: () -> Unit
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            modifier = Modifier.padding(bottom = 4.dp),
            text = InstantPeriodTransformer.transformToPassedTimeString(reply.createdDate),
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            HeartIcon(
                modifier = Modifier
                    .size(18.dp)
                    .bounceClick { onLikeClicked() },
                tint = if (reply.liked) LikeRed
                else MaterialTheme.colorScheme.onSurfaceVariant
            )
            Text(
                modifier = Modifier,
                text = FormattableNumber.format(reply.likesCount),
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}
