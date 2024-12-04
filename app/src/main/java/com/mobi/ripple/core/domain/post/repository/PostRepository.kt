package com.mobi.ripple.core.domain.post.repository

import androidx.paging.PagingData
import com.mobi.ripple.core.domain.common.Response
import com.mobi.ripple.core.domain.post.model.Comment
import com.mobi.ripple.core.domain.post.model.Post
import com.mobi.ripple.core.domain.post.model.PostSimpleUser
import com.mobi.ripple.core.util.paging.PagedData
import com.mobi.ripple.core.util.paging.PagedDataRepository
import kotlinx.coroutines.flow.Flow

interface PostRepository {
    suspend fun getPost(postId: String): Response<Post>
    suspend fun getPostsFlow(startItemIndex: Int, authorId: String): Flow<PagingData<Post>>
    suspend fun getSimpleUser(userId: String): Response<PostSimpleUser>
    suspend fun likeOrUnlikePost(postId: String): Response<Boolean?>
    suspend fun getPostCommentsFlow(postId: String): Flow<PagingData<Comment>>
}