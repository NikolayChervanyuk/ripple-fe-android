//package com.mobi.ripple.core.presentation.post.components
//
//import android.annotation.SuppressLint
//import androidx.compose.animation.core.tween
//import androidx.compose.animation.expandVertically
//import androidx.compose.animation.shrinkVertically
//import androidx.compose.foundation.background
//import androidx.compose.foundation.clickable
//import androidx.compose.foundation.layout.Arrangement
//import androidx.compose.foundation.layout.Box
//import androidx.compose.foundation.layout.Column
//import androidx.compose.foundation.layout.Row
//import androidx.compose.foundation.layout.Spacer
//import androidx.compose.foundation.layout.fillMaxWidth
//import androidx.compose.foundation.layout.height
//import androidx.compose.foundation.layout.heightIn
//import androidx.compose.foundation.layout.padding
//import androidx.compose.foundation.layout.size
//import androidx.compose.foundation.layout.width
//import androidx.compose.foundation.lazy.LazyColumn
//import androidx.compose.foundation.shape.CircleShape
//import androidx.compose.material3.BottomSheetScaffold
//import androidx.compose.material3.BottomSheetScaffoldState
//import androidx.compose.material3.ExperimentalMaterial3Api
//import androidx.compose.material3.MaterialTheme
//import androidx.compose.material3.Text
//import androidx.compose.runtime.Composable
//import androidx.compose.runtime.LaunchedEffect
//import androidx.compose.runtime.SideEffect
//import androidx.compose.runtime.mutableStateOf
//import androidx.compose.runtime.rememberCoroutineScope
//import androidx.compose.runtime.saveable.rememberSaveable
//import androidx.compose.ui.Alignment
//import androidx.compose.ui.Modifier
//import androidx.compose.ui.draw.clip
//import androidx.compose.ui.graphics.Brush
//import androidx.compose.ui.graphics.Color
//import androidx.compose.ui.graphics.ImageBitmap
//import androidx.compose.ui.unit.dp
//import androidx.compose.ui.window.Dialog
//import androidx.paging.PagingData
//import androidx.paging.compose.collectAsLazyPagingItems
//import androidx.paging.compose.itemKey
//import com.mobi.ripple.core.presentation.components.HeartIcon
//import com.mobi.ripple.core.presentation.components.InvalidFieldMessage
//import com.mobi.ripple.core.presentation.components.ProfilePicture
//import com.mobi.ripple.core.presentation.components.RippleInputField
//import com.mobi.ripple.core.presentation.components.SendIcon
//import com.mobi.ripple.core.presentation.effects.bounceClick
//import com.mobi.ripple.core.presentation.effects.fadingEdge
//import com.mobi.ripple.core.presentation.post.model.CommentModel
//import com.mobi.ripple.core.theme.LikeRed
//import com.mobi.ripple.core.util.FormattableNumber
//import com.mobi.ripple.core.util.InstantPeriodTransformer
//import com.mobi.ripple.core.util.validator.FieldValidator
//import kotlinx.coroutines.flow.Flow
//import kotlinx.coroutines.launch
//
//@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
//@OptIn(ExperimentalMaterial3Api::class)
//@Composable
//fun CommentsBottomSheetScaffold(
//    commentsFlow: Flow<PagingData<CommentModel>>,
//    commentsCount: Long,
//    refreshTrigger: Boolean,
//    personalPfp: ImageBitmap?,
//    showBottomSheet: Boolean,
//    sheetScaffoldState: BottomSheetScaffoldState,
//    onDismissRequest: () -> Unit,
//    onUploadComment: (commentText: String) -> Unit,
//    onCommentLikeClicked: (commentId: String) -> Unit,
//    content: @Composable () -> Unit
//) {
//    val coroutineScope = rememberCoroutineScope()
//
//    val commentsLazyPaging = commentsFlow.collectAsLazyPagingItems()
//
//    LaunchedEffect(key1 = refreshTrigger) {
//        commentsLazyPaging.refresh()
//    }
//
//    SideEffect {
//        if (showBottomSheet) {
//            coroutineScope.launch {
//                sheetScaffoldState.bottomSheetState.show()
//            }
//        } else {
//            coroutineScope.launch {
//                sheetScaffoldState.bottomSheetState.hide()
//            }
//        }
//    }
//    Dialog(onDismissRequest = { /*TODO*/ }) {
//        BottomSheetScaffold(
//            sheetDragHandle = { BottomSheetDragHandle(commentsCount = commentsCount) },
//            scaffoldState = sheetScaffoldState,
//            sheetContent = {
//                // Content of the bottom sheet (e.g., list of comments)
//                LazyColumn(
//                    modifier = Modifier
//                        .fillMaxWidth()
//                        .heightIn(min = 250.dp)
//                        .padding(start = 8.dp, end = 8.dp, top = 6.dp)
//                ) {
//                    items(
//                        count = commentsLazyPaging.itemCount,
//                        key = commentsLazyPaging.itemKey { it.commentId },
//                    ) { index ->
//                        val item = commentsLazyPaging[index]
//                        item?.let {
//                            CommentItem(
//                                comment = it,
//                                onLikeClicked = { onCommentLikeClicked(it.commentId) }
//                            )
//                        }
//                    }
//                }
//            },
//            sheetPeekHeight = 0.dp
//        ) {
//            content.invoke()
//        }
//
//    }
//}
//
//@Composable
//private fun CommentItem(
//    comment: CommentModel,
//    onLikeClicked: () -> Unit
//) {
//    Row(
//        modifier = Modifier
//            .fillMaxWidth()
//            .padding(bottom = 20.dp)
//    ) {
//        ProfilePicture(
//            modifier = Modifier.padding(end = 8.dp),
//            profilePicture = comment.authorProfilePicture
//        )
//        Column {
//            CommentHeaderTexts(
//                modifier = Modifier.padding(bottom = 10.dp),
//                fullName = comment.authorName,
//                username = comment.authorUsername
//            )
//            //TODO: longer text should be expandable
//            Text(
//                modifier = Modifier,
//                text = comment.comment,
//                style = MaterialTheme.typography.bodyMedium,
//                color = MaterialTheme.colorScheme.onBackground
//            )
//        }
//        CommentRightColumn(
//            comment = comment,
//            onLikeClicked = onLikeClicked
//        )
//    }
//}
//
//@Composable
//private fun CommentRightColumn(
//    modifier: Modifier = Modifier,
//    comment: CommentModel,
//    onLikeClicked: () -> Unit
//) {
//    Column(
//        modifier = modifier,
//        horizontalAlignment = Alignment.End
//    ) {
//        Text(
//            modifier = Modifier.padding(bottom = 4.dp),
//            text = InstantPeriodTransformer.transformToPassedTimeString(comment.createdDate),
//            style = MaterialTheme.typography.titleSmall,
//            color = MaterialTheme.colorScheme.onSurfaceVariant
//        )
//        Column(
//            horizontalAlignment = Alignment.CenterHorizontally
//        ) {
//            HeartIcon(
//                modifier = Modifier
//                    .size(22.dp)
//                    .bounceClick { onLikeClicked() },
//                tint = if (comment.liked) LikeRed
//                else MaterialTheme.colorScheme.onSurfaceVariant
//            )
//            Text(
//                modifier = Modifier,
//                text = FormattableNumber.format(comment.likesCount),
//                style = MaterialTheme.typography.labelMedium,
//                color = MaterialTheme.colorScheme.onSurfaceVariant
//            )
//        }
//    }
//}
//
//@Composable
//private fun CommentHeaderTexts(
//    modifier: Modifier = Modifier,
//    fullName: String?,
//    username: String
//) {
//    Row(
//        modifier = modifier,
//        verticalAlignment = Alignment.CenterVertically,
//        horizontalArrangement = Arrangement.Start
//    ) {
//        val startText = fullName ?: username
//        fullName?.let {
//            HeaderStartText(text = startText)
//        } ?: HeaderStartText(text = "@$startText")
//        fullName?.let {
//            if (username.length <= 20) {
//                Spacer(modifier = Modifier.width(5.dp))
//                HeaderEndText(text = "@${username}")
//            }
//        }
//    }
//}
//
//@Composable
//private fun HeaderStartText(modifier: Modifier = Modifier, text: String) {
//    Text(
//        modifier = modifier,
//        text = text,
//        style = MaterialTheme.typography.titleSmall,
//        color = MaterialTheme.colorScheme.onSurface
//    )
//}
//
//@Composable
//private fun HeaderEndText(text: String) {
//    Text(
//        text = text,
//        style = MaterialTheme.typography.bodyMedium,
//        color = MaterialTheme.colorScheme.onSurfaceVariant
//    )
//}
//
//@Composable
//private fun CommentTextField(
//    modifier: Modifier = Modifier,
//    personalPfp: ImageBitmap?,
//    onUploadComment: (String) -> Unit
//) {
//    val fade = Brush.verticalGradient(
//        0f to Color.Transparent,
//        0.15f to Color.Black
//    )
//
//    val commentText = rememberSaveable { mutableStateOf("") }
//
//    val sendButtonTint = if (commentText.value.isBlank()) {
//        MaterialTheme.colorScheme.onSurfaceVariant
//    } else MaterialTheme.colorScheme.onBackground
//
//    val showInvalidCommentMessage = rememberSaveable { mutableStateOf(false) }
//    Column(modifier = modifier) {
//        InvalidCommentMessage(
//            modifier = Modifier.padding(bottom = 3.dp),
//            show = showInvalidCommentMessage.value
//        )
//        Row(
//            modifier = Modifier
//                .fadingEdge(fade)
//                .background(MaterialTheme.colorScheme.surface)
//                .padding(top = 3.dp)
//                .fillMaxWidth(),
//            verticalAlignment = Alignment.CenterVertically
//        ) {
//            ProfilePicture(
//                modifier = Modifier
//                    .padding(end = 8.dp)
//                    .size(32.dp),
//                profilePicture = personalPfp,
//                borderWidthDp = 1.dp
//            )
//            RippleInputField(
//                placeholder = "Comment here...",
//                onTextChanged = {
//                    if (FieldValidator.isCommentValid(it) || it.isBlank()) {
//                        commentText.value = it
//                        showInvalidCommentMessage.value = false
//                    }
//                },
//                trailingIcon = {
//                    SendIcon(
//                        modifier = Modifier
//                            .clickable {
//                                if (FieldValidator.isCommentValid(commentText.value)) {
//                                    onUploadComment(commentText.value)
//                                } else {
//                                    showInvalidCommentMessage.value = true
//                                }
//                            }
//                            .size(18.dp),
//                        tint = sendButtonTint
//                    )
//                }
//            )
//        }
//    }
//}
//
//@Composable
//private fun InvalidCommentMessage(
//    modifier: Modifier = Modifier,
//    show: Boolean
//) {
//    InvalidFieldMessage(
//        modifier = modifier,
//        show = show,
//        message = "Comment should be less than ${FieldValidator.MAX_COMMENT_LENGTH} characters and not blank",
//        enter = expandVertically(
//            expandFrom = Alignment.Bottom,
//            animationSpec = tween(200)
//        ),
//        exit = shrinkVertically(
//            shrinkTowards = Alignment.Bottom,
//            animationSpec = tween(200)
//        )
//    )
//
//}
//
//@Composable
//private fun BottomSheetDragHandle(
//    commentsCount: Long
//) {
//    Column(
//        modifier = Modifier
//            .padding(14.dp),
//        horizontalAlignment = Alignment.CenterHorizontally
//    ) {
//        Box(
//            modifier = Modifier
//                .clip(CircleShape)
//                .background(color = MaterialTheme.colorScheme.onSurfaceVariant)
//                .size(width = 50.dp, height = 5.dp)
//                .padding(bottom = 10.dp)
//        )
//        Text(
//            modifier = Modifier.padding(top = 10.dp, bottom = 12.dp),
//            text = "${
//                FormattableNumber.format(
//                    number = commentsCount,
//                    shouldTrimOnZero = true
//                )
//            } comments",
//            style = MaterialTheme.typography.titleSmall,
//            color = MaterialTheme.colorScheme.onSurface
//        )
//    }
//}