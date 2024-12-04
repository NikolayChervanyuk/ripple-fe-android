package com.mobi.ripple.core.data.comment.data_source.remote

import com.mobi.ripple.core.config.AppUrls
import com.mobi.ripple.core.data.comment.data_source.remote.dto.EditCommentRequest
import com.mobi.ripple.core.data.comment.data_source.remote.dto.UploadCommentRequest
import com.mobi.ripple.core.data.common.data_source.wrappers.ApiRequest
import com.mobi.ripple.core.data.common.data_source.wrappers.ApiResponse
import io.ktor.client.HttpClient
import io.ktor.client.request.delete
import io.ktor.client.request.post
import io.ktor.client.request.put
import io.ktor.client.request.setBody

class CommentApiServiceImpl(
    private val client: HttpClient
): CommentApiService {

    override suspend fun uploadComment(
        postId: String,
        commentRequest: UploadCommentRequest
    ): ApiResponse<Boolean> = ApiRequest<Boolean> {
        client.post(AppUrls.PostUrls.uploadPostComment(postId)) {
            setBody(commentRequest)
        }
    }.sendRequest()

    override suspend fun likeOrUnlikeComment(
        postId: String,
        commentId: String
    ): ApiResponse<Boolean> = ApiRequest<Boolean> {
        client.put(AppUrls.PostUrls.likeOrUnlikeComment(postId, commentId))
    }.sendRequest()

    override suspend fun editComment(
        postId: String,
        commentId: String,
        editCommentRequest: EditCommentRequest
    ): ApiResponse<Boolean> = ApiRequest<Boolean> {
        client.put(AppUrls.PostUrls.editDeleteComment(postId, commentId)) {
            setBody(editCommentRequest)
        }
    }.sendRequest()

    override suspend fun deleteComment(
        postId: String,
        commentId: String
    ): ApiResponse<Boolean> = ApiRequest<Boolean> {
        client.delete(AppUrls.PostUrls.editDeleteComment(postId, commentId))
    }.sendRequest()
}