package com.mobi.ripple.core.presentation.post.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.tooling.preview.Preview
import com.mobi.ripple.core.presentation.components.DefaultCircularProgressIndicator
import com.mobi.ripple.core.presentation.post.model.PostModel
import com.mobi.ripple.core.presentation.post.model.PostSimpleUserModel
import com.mobi.ripple.core.theme.RippleTheme
import java.time.Instant

@Composable
fun PostContent(
    modifier: Modifier = Modifier,
    postModel: PostModel
) {
    Column(
        modifier = modifier
    ) {
        PostImage(
            modifier = Modifier
                .background(MaterialTheme.colorScheme.onBackground)
                .fillMaxWidth()
                .aspectRatio(0.75f),
            image = postModel.postImage
        )
    }
}

@Composable
private fun PostImage(
    modifier: Modifier = Modifier,
    image: ImageBitmap?
) {
    Box(
        modifier = modifier,
        contentAlignment = Alignment.Center
    ) {
        image?.let {
            Image(
                modifier = Modifier
                    .fillMaxWidth(),
                bitmap = it,
                contentDescription = "post image",
                contentScale = ContentScale.Fit
            )
        } ?: DefaultCircularProgressIndicator()//PostLoadingErrorMessage()
    }
}

@Composable
private fun PostLoadingErrorMessage() {
    Text(
        text = "Can't load content",
        style = MaterialTheme.typography.titleMedium,
        color = MaterialTheme.colorScheme.surface
    )
}

@Preview
@Composable
private fun CaptionTextPreview() {
    RippleTheme {
        Surface {
            CaptionText(
//                simpleUserModel = PostSimpleUserModel(
//                    id = "1234",
//                    fullName = "Ivan Ivanov",
//                    username = "vanko333",
//                    isActive = true,
//                    profilePicture = null
//                ),
                postModel = PostModel(
                    id = "12345",
                    creationDate = Instant.now().minusSeconds(5L),
                    lastModifiedDate = Instant.now().minusSeconds(3L),
                    authorId = "1234",
                    authorFullName = "Petko",
                    authorUsername = "petko11",
                    isAuthorActive = true,
                    authorPfp = null,
                    postImage = null,
                    caption = "A test caption to be seen in a preview. " +
                            "Don't mind this text as it doesn't make any sense " +
                            "Don't mind this text as it doesn't make any sense",
                    likesCount = 27,
                    liked = false,
                    commentsCount = 13L
                )
            )
        }
    }
}
