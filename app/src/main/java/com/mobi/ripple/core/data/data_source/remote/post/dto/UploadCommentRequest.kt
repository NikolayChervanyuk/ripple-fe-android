package com.mobi.ripple.core.data.data_source.remote.post.dto

import com.mobi.ripple.core.domain.model.post.UploadComment
import kotlinx.serialization.Serializable

@Serializable
data class UploadCommentRequest(
    val comment: String
)

fun UploadComment.asUploadCommentRequest() = UploadCommentRequest(
    comment = commentText
)
