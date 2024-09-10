package com.mobi.ripple.core.presentation.post.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.mobi.ripple.core.presentation.components.ActionButton
import com.mobi.ripple.core.presentation.components.ProfilePicture
import com.mobi.ripple.core.presentation.post.model.PostModel
import com.mobi.ripple.core.presentation.post.model.PostSimpleUserModel
import com.mobi.ripple.core.theme.RippleTheme
import com.mobi.ripple.core.util.InstantPeriodTransformer
import java.time.Instant

@Composable
fun PostHeader(
    postSimpleUser: PostSimpleUserModel,
    postModel: PostModel
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(5.dp),
        verticalAlignment = Alignment.Top
    ) {
        ProfilePicture(
            modifier = Modifier
                .padding(end = 10.dp)
                .size(38.dp),
            profilePicture = postSimpleUser.profilePicture,
            isActive = postSimpleUser.isActive
        )
        Column {
            Row(
                modifier = Modifier,
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Start
            ) {
                val startText = postSimpleUser.fullName ?: postSimpleUser.username
                postSimpleUser.fullName?.let {

                    StartText(text = startText)
                } ?: StartText(text = "@$startText")
                postSimpleUser.fullName?.let {
                    if (postSimpleUser.username.length <= 20) {
                        Spacer(modifier = Modifier.width(5.dp))
                        EndText(text = "@${postSimpleUser.username}")
                    }
                }
            }
            PostCreationPassedPeriod(
                modifier = Modifier.padding(top = 2.dp),
                text = InstantPeriodTransformer
                    .transformToPassedTimeString(
                        postModel.creationDate
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

@Preview
@Composable
private fun PostHeaderPreview() {
    RippleTheme {
        Surface {
            PostHeader(
                postSimpleUser = PostSimpleUserModel(
                    id = "1234",
                    fullName = "Ivan Ivanov",
                    username = "vanko333",
                    isActive = true,
                    profilePicture = null
                ),
                postModel = PostModel(
                    id = "12345",
                    creationDate = Instant.now().minusSeconds(5L),
                    lastModifiedDate = Instant.now().minusSeconds(3L),
                    authorId = "1234",
                    postImage = null,
                    caption = "A test caption to be seen in a preview. " +
                            "Don't mind this text as it doesn't make any sense",
                    likesCount = 27,
                    liked = false,
                    commentsCount = 13L
                )
            )
        }
    }
}