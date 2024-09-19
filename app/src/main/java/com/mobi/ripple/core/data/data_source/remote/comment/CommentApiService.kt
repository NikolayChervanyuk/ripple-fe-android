package com.mobi.ripple.core.data.data_source.remote.comment

import com.mobi.ripple.core.data.data_source.remote.comment.dto.EditCommentRequest
import com.mobi.ripple.core.data.data_source.remote.comment.dto.UploadCommentRequest
import com.mobi.ripple.core.data.data_source.remote.wrappers.ApiResponse

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