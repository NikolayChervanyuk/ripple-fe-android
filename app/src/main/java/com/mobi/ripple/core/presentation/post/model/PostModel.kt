package com.mobi.ripple.core.presentation.post.model

import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import com.mobi.ripple.core.domain.model.post.Post
import com.mobi.ripple.core.util.BitmapUtils
import java.time.Instant

data class PostModel(
    val id: String,
    val creationDate: Instant,
    val lastModifiedDate: Instant,
    val authorId: String,
    val authorFullName: String?,
    val authorUsername: String,
    val isAuthorActive: Boolean,
    val authorPfp: ImageBitmap?,
    val postImage: ImageBitmap?,
    val caption: String?,
    var likesCount: Long,
    var liked: Boolean,
    val commentsCount: Long
)

fun Post.asPostModel() = PostModel(
    id = id,
    creationDate = creationDate,
    lastModifiedDate = lastModifiedDate,
    authorId = authorId,
    authorFullName = authorFullName,
    authorUsername = authorUsername,
    isAuthorActive = isAuthorActive,
    authorPfp = authorPfp?.let { BitmapUtils.convertImageByteArrayToBitmap(it).asImageBitmap() },
    postImage = BitmapUtils.convertImageByteArrayToBitmap(postImage).asImageBitmap(),
    caption = caption,
    likesCount = likesCount,
    liked = liked,
    commentsCount = commentsCount

)
