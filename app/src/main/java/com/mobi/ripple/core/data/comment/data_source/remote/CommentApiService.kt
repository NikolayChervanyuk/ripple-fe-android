package com.mobi.ripple.core.data.comment.data_source.remote

import com.mobi.ripple.core.data.comment.data_source.remote.dto.EditCommentRequest
import com.mobi.ripple.core.data.comment.data_source.remote.dto.UploadCommentRequest
import com.mobi.ripple.core.data.common.data_source.wrappers.ApiResponse

interface CommentApiService {
    suspend fun uploadComment(
        postId: String,
        commentRequest: UploadCommentRequest
    ): ApiResponse<Boolean>

    suspend fun likeOrUnlikeComment(postId: String, commentId: String): ApiResponse<Boolean>
    suspend fun editComment(
        postId: String,
        commentId: String,
        editCommentRequest: EditCommentRequest
    ): ApiResponse<Boolean>

    suspend fun deleteComment(postId: String, commentId: String): ApiResponse<Boolean>

}