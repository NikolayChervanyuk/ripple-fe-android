package com.mobi.ripple.core.presentation.post.components

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.LinkAnnotation
import androidx.compose.ui.text.TextLinkStyles
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withLink
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import com.mobi.ripple.core.presentation.components.CommentBubbleIcon
import com.mobi.ripple.core.presentation.components.ForwardIcon
import com.mobi.ripple.core.presentation.components.HeartIcon
import com.mobi.ripple.core.presentation.effects.bounceClick
import com.mobi.ripple.core.presentation.post.model.PostModel
import com.mobi.ripple.core.theme.LikeRed
import com.mobi.ripple.core.util.FormattableNumber
import com.mobi.ripple.core.util.SoundEffects

@Composable
fun PostFooter(
    modifier: Modifier = Modifier,
    postModel: PostModel,
    likesCount: Long,
    isLiked: Boolean,
    commentsCount: Long, //?
    onLikeClicked: () -> Unit,
    onCommentsClicked: () -> Unit,
    onShareClicked: () -> Unit
) {
    Column(
        modifier = modifier
    ) {
        CaptionText(
            modifier = Modifier
                .padding(horizontal = 8.dp)
                .padding(top = 12.dp),
//            simpleUserModel = postSimpleUser,
            postModel = postModel
        )
        PostActionsRow(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 60.dp)
                .padding(top = 12.dp),
//            postModel = postModel,
            likesCount = likesCount,
            isLiked = isLiked,
            commentsCount = commentsCount,
            onLikeClicked = onLikeClicked,
            onCommentsClicked = onCommentsClicked,
            onShareClicked = onShareClicked
        )
    }
}

@Composable
fun CaptionText(
    modifier: Modifier = Modifier,
//    simpleUserModel: PostSimpleUserModel,
    postModel: PostModel
) {
    postModel.caption.value?.let { captionText ->
        val userText = "${postModel.authorFullName.value ?: postModel.authorUsername.value}: "
        val prependToUserText = if (postModel.authorFullName.value == null) "@" else ""
        val shortenedCaptionText = remember { mutableStateOf(captionText) }
        val isCaptionShortened = remember { mutableStateOf(true) }
        val isCaptionTooLong = remember { mutableStateOf(false) }

        if (isCaptionShortened.value) {
            if (captionText.length > 120) {
                shortenedCaptionText.value =
                    shortenedCaptionText.value.substring(0..120)
                isCaptionTooLong.value = true
            }
        } else {
            shortenedCaptionText.value = captionText
        }
        val annotatedCaptionString = buildAnnotatedString {
            withStyle(
                MaterialTheme.typography.bodyMedium
                    .copy(fontWeight = FontWeight.Bold)
                    .toSpanStyle()
            ) {
                append("$prependToUserText$userText")
            }
            append(shortenedCaptionText.value)
            if (isCaptionTooLong.value &&
                isCaptionShortened.value
            ) {
                append("...")
                withLink(
                    link = LinkAnnotation
                        .Clickable(
                            tag = "more",
                            linkInteractionListener = {
                                isCaptionShortened.value = false
                            },
                            styles = TextLinkStyles(
                                style = MaterialTheme.typography.bodyMedium
                                    .copy(
                                        fontWeight = FontWeight.Bold,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                    .toSpanStyle()
                            )
                        )
                ) {
                    append("more")
                }
            }
        }

        Text(
            modifier = modifier
                .animateContentSize(),
            text = annotatedCaptionString,
            textAlign = TextAlign.Justify,
            style = MaterialTheme.typography.bodyMedium,
            color = Color.Black
        )
    }
}

@Composable
private fun PostActionsRow(
    modifier: Modifier = Modifier,
//    postModel: PostModel,
    likesCount: Long,
    isLiked: Boolean,
    commentsCount: Long,
    onLikeClicked: () -> Unit,
    onCommentsClicked: () -> Unit,
    onShareClicked: () -> Unit
) {
//    val model = remember { mutableStateOf(postModel)}
//    val isLiked = remember { postModel.liked }
//    val likesCount = remember { mutableLongStateOf(postModel.likesCount.value) }
//    if (isLiked.value != postModel.liked.value) {
//        isLiked.value = postModel.liked.value
//        likesCount.longValue = postModel.likesCount.value
//    }
    val context = LocalContext.current
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        PostLikeButton(
            modifier = Modifier
                .bounceClick {
//                    postModel.likesCount.longValue += if (isLiked.value) -1 else 1
//                    isLiked.value = !isLiked.value
                    if (!isLiked) SoundEffects.LikeSound.play(context)
                    onLikeClicked()
                }
                .size(30.dp),
            isLiked = isLiked,
            likes = likesCount
        )
        PostCommentsButton(
            modifier = Modifier
                .clickable(
                    interactionSource = remember { MutableInteractionSource() },
                    indication = null
                ) {
                    onCommentsClicked()
                }
                .size(30.dp),
            commentsCount = commentsCount
        )
        PostShareButton(
            modifier = Modifier
                .clickable {
                    onShareClicked()
                }
                .size(30.dp)
        )
    }
}

@Composable
private fun PostLikeButton(
    modifier: Modifier = Modifier,
    isLiked: Boolean,
    likes: Long,
) {
    val tint = if (isLiked) LikeRed
    else MaterialTheme.colorScheme.onSurfaceVariant
    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        HeartIcon(
            modifier = modifier,
            tint = tint
        )
        Text(
            modifier = Modifier
                .padding(top = 4.dp)
                .width(IntrinsicSize.Max),
            text = FormattableNumber.format(likes),
            style = MaterialTheme.typography.labelMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            textAlign = TextAlign.Center
        )
    }
}


@Composable
private fun PostCommentsButton(
    modifier: Modifier = Modifier,
    commentsCount: Long
) {
    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        CommentBubbleIcon(modifier = modifier)
        Text(
            modifier = Modifier
                .padding(top = 4.dp)
                .width(IntrinsicSize.Max),
            text = FormattableNumber.format(commentsCount),
            style = MaterialTheme.typography.labelMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            textAlign = TextAlign.Center
        )
    }
}

@Composable
private fun PostShareButton(modifier: Modifier = Modifier) {
    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        ForwardIcon(modifier = modifier)
        Text(
            modifier = Modifier
                .padding(top = 4.dp)
                .width(IntrinsicSize.Max),
            text = FormattableNumber.format(0),
            style = MaterialTheme.typography.labelMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            textAlign = TextAlign.Center
        )
    }
}