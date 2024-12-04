package com.mobi.ripple.core.data.post.data_source.remote

import com.mobi.ripple.core.config.AppUrls
import com.mobi.ripple.core.data.post.data_source.remote.dto.PostCommentResponse
import com.mobi.ripple.core.data.post.data_source.remote.dto.PostResponse
import com.mobi.ripple.core.data.post.data_source.remote.dto.PostSimpleUserResponse
import com.mobi.ripple.core.data.comment.data_source.remote.dto.UploadCommentRequest
import com.mobi.ripple.core.data.common.data_source.wrappers.ApiRequest
import com.mobi.ripple.core.data.common.data_source.wrappers.ApiResponse
import io.ktor.client.HttpClient
import io.ktor.client.request.get
import io.ktor.client.request.post
import io.ktor.client.request.put
import io.ktor.client.request.setBody

class PostApiServiceImpl(
    private val client: HttpClient
) : PostApiService {
    override suspend fun getPost(
        postId: String
    ): ApiResponse<PostResponse> = ApiRequest<PostResponse> {
        client.get(AppUrls.PostUrls.getPost(postId))
    }.sendRequest()

    override suspend fun getPosts(
        authorId: String,
        page: Int
    ): ApiResponse<List<PostResponse>> = ApiRequest<List<PostResponse>> {
        client.get(AppUrls.PostUrls.getPosts(authorId, page))
    }.sendRequest()

    override suspend fun getSimpleUser(
        userId: String
    ): ApiResponse<PostSimpleUserResponse> = ApiRequest<PostSimpleUserResponse> {
        client.get(AppUrls.PostUrls.getSimpleUser(userId))
    }.sendRequest()

    override suspend fun likeOrUnlikePost(
        postId: String
    ): ApiResponse<Boolean> = ApiRequest<Boolean> {
        client.put(AppUrls.PostUrls.likeOrUnlikePost(postId))
    }.sendRequest()

    override suspend fun getPostComments(
        postId: String,
        page: Int
    ): ApiResponse<List<PostCommentResponse>> = ApiRequest<List<PostCommentResponse>> {
        client.get(AppUrls.PostUrls.getPostComments(postId, page))
    }.sendRequest()

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
}