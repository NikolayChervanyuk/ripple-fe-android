package com.mobi.ripple.core.presentation.post.model

import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import com.mobi.ripple.core.domain.model.post.Reply
import com.mobi.ripple.core.util.BitmapUtils
import java.time.Instant

data class ReplyModel(
    val replyId: String,
    val authorProfilePicture: ImageBitmap?,
    val authorName: String?,
    val authorUsername: String,
    val createdDate: Instant,
    val lastUpdatedDate: Instant,
    val likesCount: Long,
    val liked: Boolean,
    val reply: String
)

fun Reply.asReplyModel() = ReplyModel(
    replyId = replyId,
    authorProfilePicture = authorProfilePicture?.let {
        BitmapUtils.convertImageByteArrayToBitmap(it).asImageBitmap()
    },
    authorName = authorName,
    authorUsername = authorUsername,
    createdDate = createdDate,
    lastUpdatedDate = lastUpdatedDate,
    likesCount = likesCount,
    liked = liked,
    reply = reply
)