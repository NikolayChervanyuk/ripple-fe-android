package com.mobi.ripple.core.data.reply.data_source.remote

import com.mobi.ripple.core.data.reply.data_source.remote.dto.EditReplyRequest
import com.mobi.ripple.core.data.reply.data_source.remote.dto.UploadReplyRequest
import com.mobi.ripple.core.data.common.data_source.wrappers.ApiResponse
import com.mobi.ripple.core.data.reply.data_source.remote.dto.ReplyResponse

interface ReplyApiService {

    suspend fun getLatestReplies(
        postId: String,
        commentId: String,
        page: Int
    ): ApiResponse<List<ReplyResponse>>

    suspend fun uploadReply(
        postId: String,
        commentId: String,
        uploadReplyRequest: UploadReplyRequest
    ): ApiResponse<Boolean>

    suspend fun editReply(
        postId: String,
        commentId: String,
        replyId: String,
        editReplyRequest: EditReplyRequest
    ): ApiResponse<Boolean>

    suspend fun deleteReply(
        postId: String,
        commentId: String,
        replyId: String
    ): ApiResponse<Boolean>

    suspend fun likeOrUnlikeReply(
        postId: String,
        commentId: String,
        replyId: String
    ): ApiResponse<Boolean>
}