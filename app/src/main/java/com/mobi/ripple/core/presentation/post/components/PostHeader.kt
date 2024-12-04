package com.mobi.ripple.core.presentation.post.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.mobi.ripple.core.presentation.components.ActionButton
import com.mobi.ripple.core.presentation.components.PictureFrame
import com.mobi.ripple.core.presentation.post.model.PostModel
import com.mobi.ripple.core.util.InstantPeriodTransformer

@Composable
fun PostHeader(
    onProfileNavigationRequest: (username: String) -> Unit,
    postModel: PostModel
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(5.dp),
        verticalAlignment = Alignment.Top
    ) {
        PictureFrame(
            modifier = Modifier
                .padding(end = 10.dp)
                .clickable { onProfileNavigationRequest(postModel.authorUsername.value) }
                .size(38.dp),
            picture = postModel.authorPfp.value,
            isActive = postModel.isAuthorActive.value
        )
        Column {
            Row(
                modifier = Modifier,
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Start
            ) {
                val startText = postModel.authorFullName.value ?: postModel.authorUsername.value
                postModel.authorFullName.value?.let {

                    StartText(text = startText)
                } ?: StartText(text = "@$startText")
                postModel.authorFullName.value?.let {
                    if (postModel.authorUsername.value.length <= 20) {
                        Spacer(modifier = Modifier.width(5.dp))
                        EndText(text = "@${postModel.authorUsername.value}")
                    }
                }
            }
            PostCreationPassedPeriod(
                modifier = Modifier.padding(top = 2.dp),
                text = InstantPeriodTransformer
                    .transformToPassedTimeString(
                        postModel.creationDate.value
                    )
            )
        }
        Spacer(modifier = Modifier.weight(1f))
        ActionButton(
            modifier = Modifier
                .padding(top = 3.dp)
                .size(20.dp),
            onClick = { TODO("implement action button") }
        )
    }
}

@Composable
private fun StartText(modifier: Modifier = Modifier, text: String) {
    Text(
        modifier = modifier,
        text = text,
        style = MaterialTheme.typography.titleSmall,
        color = MaterialTheme.colorScheme.onSurface
    )
}

@Composable
private fun EndText(text: String) {
    Text(
        text = text,
        style = MaterialTheme.typography.bodyMedium,
        color = MaterialTheme.colorScheme.onSurfaceVariant
    )
}


@Composable
private fun PostCreationPassedPeriod(
    modifier: Modifier = Modifier,
    text: String
) {
    Text(
        modifier = modifier,
        text = text,
        style = MaterialTheme.typography.bodyMedium,
        color = MaterialTheme.colorScheme.onSurfaceVariant
    )
}

//@Preview
//@Composable
//private fun PostHeaderPreview() {
//    RippleTheme {
//        Surface {
//            PostHeader(
////                postSimpleUser = PostSimpleUserModel(
////                    id = "1234",
////                    fullName = "Ivan Ivanov",
////                    username = "vanko333",
////                    isActive = true,
////                    profilePicture = null
////                ),
//                postModel = PostModel(
//                    id = "12345",
//                    creationDate = Instant.now().minusSeconds(5L),
//                    lastModifiedDate = Instant.now().minusSeconds(3L),
//                    authorId = "1234",
//                    authorFullName = "Petko",
//                    authorUsername = "petko11",
//                    isAuthorActive = true,
//                    authorPfp = null,
//                    postImage = null,
//                    caption = "A test caption to be seen in a preview. " +
//                            "Don't mind this text as it doesn't make any sense",
//                    likesCount = 27,
//                    liked = false,
//                    commentsCount = 13L
//                )
//            )
//        }
//    }
//}