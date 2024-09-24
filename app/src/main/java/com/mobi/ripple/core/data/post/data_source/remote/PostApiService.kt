package com.mobi.ripple.core.data.post.data_source.remote

import com.mobi.ripple.core.data.post.data_source.remote.dto.PostCommentResponse
import com.mobi.ripple.core.data.post.data_source.remote.dto.PostResponse
import com.mobi.ripple.core.data.post.data_source.remote.dto.PostSimpleUserResponse
import com.mobi.ripple.core.data.comment.data_source.remote.dto.UploadCommentRequest
import com.mobi.ripple.core.data.common.data_source.wrappers.ApiResponse

interface PostApiService {
    suspend fun getPost(postId: String): ApiResponse<PostResponse>
    suspend fun getPosts(authorId: String, page: Int): ApiResponse<List<PostResponse>>
    suspend fun getSimpleUser(userId: String): ApiResponse<PostSimpleUserResponse>
    suspend fun likeOrUnlikePost(postId: String): ApiResponse<Boolean>
    suspend fun getPostComments(postId: String, page: Int): ApiResponse<List<PostCommentResponse>>
    suspend fun uploadComment(
        postId: String,
        commentRequest: UploadCommentRequest
    ): ApiResponse<Boolean>
    suspend fun likeOrUnlikeComment(postId: String, commentId: String): ApiResponse<Boolean>
}