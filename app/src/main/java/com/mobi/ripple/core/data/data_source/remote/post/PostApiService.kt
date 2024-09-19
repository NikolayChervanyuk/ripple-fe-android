package com.mobi.ripple.core.data.data_source.remote.post

import com.mobi.ripple.core.data.data_source.remote.post.dto.PostCommentResponse
import com.mobi.ripple.core.data.data_source.remote.post.dto.PostResponse
import com.mobi.ripple.core.data.data_source.remote.post.dto.PostSimpleUserResponse
import com.mobi.ripple.core.data.data_source.remote.comment.dto.UploadCommentRequest
import com.mobi.ripple.core.data.data_source.remote.wrappers.ApiResponse

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