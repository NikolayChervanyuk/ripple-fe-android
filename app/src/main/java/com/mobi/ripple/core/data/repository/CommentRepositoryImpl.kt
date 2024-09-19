package com.mobi.ripple.core.data.repository

import androidx.paging.PagingData
import com.mobi.ripple.core.data.data_source.remote.comment.CommentApiService
import com.mobi.ripple.core.data.data_source.remote.comment.dto.EditCommentRequest
import com.mobi.ripple.core.data.data_source.remote.comment.dto.asUploadCommentRequest
import com.mobi.ripple.core.domain.model.Response
import com.mobi.ripple.core.domain.model.post.Reply
import com.mobi.ripple.core.domain.model.post.UploadComment
import com.mobi.ripple.core.domain.repository.CommentRepository
import kotlinx.coroutines.flow.Flow

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

    override suspend fun getCommentRepliesFlow(
        postId: String,
        commentId: String
    ): Flow<PagingData<Reply>> {
        TODO("Not yet implemented")
    }
}