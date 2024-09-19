package com.mobi.ripple.core.domain.repository

import androidx.paging.PagingData
import com.mobi.ripple.core.domain.model.Response
import com.mobi.ripple.core.domain.model.post.Comment
import com.mobi.ripple.core.domain.model.post.Post
import com.mobi.ripple.core.domain.model.post.PostSimpleUser
import com.mobi.ripple.core.domain.model.post.UploadComment
import com.mobi.ripple.feature_app.feature_profile.domain.model.UserProfileSimplePost
import kotlinx.coroutines.flow.Flow

interface PostRepository {
    suspend fun getPost(postId: String): Response<Post>
    suspend fun getPostsFlow(startIndex: Int, authorId: String): Flow<PagingData<Post>>
    suspend fun getSimpleUser(userId: String): Response<PostSimpleUser>
    suspend fun likeOrUnlikePost(postId: String): Response<Boolean?>
    suspend fun getPostCommentsFlow(postId: String): Flow<PagingData<Comment>>
}