package com.mobi.ripple.core.data.comment.repository

import com.mobi.ripple.core.data.comment.data_source.remote.CommentApiService
import com.mobi.ripple.core.data.comment.data_source.remote.dto.EditCommentRequest
import com.mobi.ripple.core.data.comment.data_source.remote.dto.asUploadCommentRequest
import com.mobi.ripple.core.domain.common.Response
import com.mobi.ripple.core.domain.post.model.UploadComment
import com.mobi.ripple.core.domain.post.repository.CommentRepository

class CommentRepositoryImpl(
    private val commentApiService: CommentApiService
) : CommentRepository {

    override suspend fun uploadComment(postId: String, comment: UploadComment): Response<Boolean?> {
        val apiResponse = commentApiService.uploadComment(postId, comment.asUploadCommentRequest())

        return apiResponse.toResponse(apiResponse.content)
    }

    override suspend fun likeOrUnlikeComment(
        postId: String,
        commentId: String
    ): Response<Boolean?> {
        val apiResponse = commentApiService.likeOrUnlikeComment(postId, commentId)

        return apiResponse.toResponse(apiResponse.content)
    }

    override suspend fun editComment(
        postId: String,
        commentId: String,
        newCommentText: String
    ): Response<Boolean?> {
        val apiResponse = commentApiService
            .editComment(postId, commentId, EditCommentRequest(newCommentText))

        return apiResponse.toResponse(apiResponse.content)
    }

    override suspend fun deleteComment(postId: String, commentId: String): Response<Boolean?> {
        val apiResponse = commentApiService.deleteComment(postId, commentId)

        return apiResponse.toResponse(apiResponse.content)
    }
}