package com.mobi.ripple.core.data.repository

import com.mobi.ripple.core.data.data_source.remote.reply.ReplyApiService
import com.mobi.ripple.core.data.data_source.remote.reply.dto.EditReplyRequest
import com.mobi.ripple.core.data.data_source.remote.reply.dto.UploadReplyRequest
import com.mobi.ripple.core.domain.model.Response
import com.mobi.ripple.core.domain.repository.ReplyRepository

class ReplyRepositoryImpl(
    private val replyApiService: ReplyApiService
): ReplyRepository {
    override suspend fun uploadReply(
        postId: String,
        commentId: String,
        replyText: String
    ): Response<Boolean?> {
        val apiResponse = replyApiService
            .uploadReply(postId, commentId, UploadReplyRequest(replyText))

        return apiResponse.toResponse(apiResponse.content)
    }

    override suspend fun editReply(
        postId: String,
        commentId: String,
        replyId: String,
        newReplyText: String
    ): Response<Boolean?> {
        val apiResponse = replyApiService
            .editReply(postId, commentId, replyId, EditReplyRequest(newReplyText))

        return apiResponse.toResponse(apiResponse.content)
    }

    override suspend fun deleteReply(
        postId: String,
        commentId: String,
        replyId: String
    ): Response<Boolean?> {
        val apiResponse = replyApiService.deleteReply(postId, commentId, replyId)

        return apiResponse.toResponse(apiResponse.content)
    }

    override suspend fun likeOrUnlikeReply(
        postId: String,
        commentId: String,
        replyId: String
    ): Response<Boolean?> {
        val apiResponse = replyApiService.likeOrUnlikeReply(postId, commentId, replyId)

        return apiResponse.toResponse(apiResponse.content)
    }
}