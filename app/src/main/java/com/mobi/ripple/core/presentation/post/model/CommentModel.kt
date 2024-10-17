package com.mobi.ripple.core.presentation.post.model

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.paging.PagingData
import com.mobi.ripple.core.domain.post.model.Comment
import com.mobi.ripple.core.domain.post.use_case.PostUseCases
import com.mobi.ripple.core.util.BitmapUtils
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow
import java.time.Instant

data class CommentModel(
    val commentId: String,
    val authorProfilePicture: ImageBitmap?,
    val authorName: String?,
    val authorUsername: String,
    val createdDate: Instant,
    val lastUpdatedDate: Instant,
    val likesCount: Long,
    var liked: Boolean,
    val repliesCount: Long,
    val comment: String,
    var repliesFlow: MutableState<Flow<PagingData<ReplyModel>>> = mutableStateOf(emptyFlow()),
    val postUseCases: PostUseCases
) {

}

fun Comment.asCommentModel(postUseCases: PostUseCases) = CommentModel(
    commentId = commentId,
    authorProfilePicture = authorProfilePicture?.let {
        BitmapUtils.convertImageByteArrayToBitmap(it).asImageBitmap()
    },
    authorName = authorName,
    authorUsername = authorUsername,
    createdDate = createdDate,
    lastUpdatedDate = lastUpdatedDate,
    likesCount = likesCount,
    liked = liked,
    repliesCount = repliesCount,
    comment = comment,
    postUseCases = postUseCases
)