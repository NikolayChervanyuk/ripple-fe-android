package com.mobi.ripple.core.domain.post.repository

import com.mobi.ripple.core.domain.common.Response
import com.mobi.ripple.core.domain.post.model.UploadComment

interface CommentRepository {
    suspend fun uploadComment(postId: String, comment: UploadComment): Response<Boolean?>
    suspend fun likeOrUnlikeComment(postId: String, commentId: String): Response<Boolean?>
    suspend fun editComment(
        postId: String,
        commentId: String,
        newCommentText: String
    ): Response<Boolean?>

    suspend fun deleteComment(postId: String, commentId: String): Response<Boolean?>
}