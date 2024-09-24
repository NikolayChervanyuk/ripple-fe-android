package com.mobi.ripple.core.data.reply.data_source.remote

import com.mobi.ripple.core.config.AppUrls
import com.mobi.ripple.core.data.reply.data_source.remote.dto.EditReplyRequest
import com.mobi.ripple.core.data.reply.data_source.remote.dto.UploadReplyRequest
import com.mobi.ripple.core.data.common.data_source.wrappers.ApiRequest
import com.mobi.ripple.core.data.common.data_source.wrappers.ApiResponse
import io.ktor.client.HttpClient
import io.ktor.client.request.delete
import io.ktor.client.request.post
import io.ktor.client.request.put
import io.ktor.client.request.setBody

class ReplyApiServiceImpl(
    private val client: HttpClient
) : ReplyApiService {
    override suspend fun uploadReply(
        postId: String,
        commentId: String,
        uploadReplyRequest: UploadReplyRequest
    ): ApiResponse<Boolean> = ApiRequest<Boolean> {
        client.post(AppUrls.PostUrls.uploadCommentReply(postId, commentId)) {
            setBody(uploadReplyRequest)
        }
    }.sendRequest()

    override suspend fun editReply(
        postId: String,
        commentId: String,
        replyId: String,
        editReplyRequest: EditReplyRequest
    ): ApiResponse<Boolean> = ApiRequest<Boolean> {
        client.put(AppUrls.PostUrls.editDeleteReply(postId, commentId, replyId)) {
            setBody(editReplyRequest)
        }
    }.sendRequest()

    override suspend fun deleteReply(
        postId: String,
        commentId: String,
        replyId: String
    ): ApiResponse<Boolean> = ApiRequest<Boolean> {
        client.delete(AppUrls.PostUrls.editDeleteReply(postId, commentId, replyId))
    }.sendRequest()

    override suspend fun likeOrUnlikeReply(
        postId: String,
        commentId: String,
        replyId: String
    ): ApiResponse<Boolean> = ApiRequest<Boolean> {
        client.put(AppUrls.PostUrls.likeOrUnlikeReply(postId, commentId, replyId))
    }.sendRequest()

}