package com.mobi.ripple.core.data.reply.repository

import com.mobi.ripple.core.data.reply.data_source.remote.ReplyApiService
import com.mobi.ripple.core.data.reply.data_source.remote.dto.EditReplyRequest
import com.mobi.ripple.core.data.reply.data_source.remote.dto.UploadReplyRequest
import com.mobi.ripple.core.domain.common.Response
import com.mobi.ripple.core.domain.post.repository.ReplyRepository

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