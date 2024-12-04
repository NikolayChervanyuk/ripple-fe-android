package com.mobi.ripple.core.data.comment.data_source.remote.dto

import com.mobi.ripple.core.domain.post.model.UploadComment
import kotlinx.serialization.Serializable

@Serializable
data class UploadCommentRequest(
    val comment: String
)

fun UploadComment.asUploadCommentRequest() = UploadCommentRequest(
    comment = commentText
)
