package com.mobi.ripple.core.data.data_source.remote.reply

import com.mobi.ripple.core.data.data_source.remote.reply.dto.EditReplyRequest
import com.mobi.ripple.core.data.data_source.remote.reply.dto.UploadReplyRequest
import com.mobi.ripple.core.data.data_source.remote.wrappers.ApiResponse

interface ReplyApiService {
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