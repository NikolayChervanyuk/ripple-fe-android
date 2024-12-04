package com.mobi.ripple.core.data.post.repository

import androidx.paging.PagingData
import androidx.paging.map
import com.mobi.ripple.core.data.common.AppDatabase
import com.mobi.ripple.core.data.post.data_source.pager.PostCommentPager
import com.mobi.ripple.core.data.post.data_source.pager.PostsPager
import com.mobi.ripple.core.data.post.data_source.remote.PostApiService
import com.mobi.ripple.core.domain.common.Response
import com.mobi.ripple.core.domain.post.model.Comment
import com.mobi.ripple.core.domain.post.model.Post
import com.mobi.ripple.core.domain.post.model.PostSimpleUser
import com.mobi.ripple.core.domain.post.repository.PostRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext

class PostRepositoryImpl(
    private val postApiService: PostApiService,
    private val database: AppDatabase
) : PostRepository {
    override suspend fun getPost(postId: String): Response<Post> {
        val apiResponse = postApiService.getPost(postId)

        return apiResponse.toResponse(apiResponse.content?.asPost())
    }

    override suspend fun getPostsFlow(
        startItemIndex: Int,
        authorId: String
    ): Flow<PagingData<Post>> {
        return PostsPager(
            apiService = postApiService,
            database = database,
            authorId = authorId,
            startItemIndex = startItemIndex
        ).getPager().flow.map { pagingData ->
            pagingData.map { it.asPost() }
        }
    }

    override suspend fun getSimpleUser(userId: String): Response<PostSimpleUser> {
        val apiResponse = postApiService.getSimpleUser(userId)

        return apiResponse.toResponse(apiResponse.content?.asPostSimpleUser())
    }

    override suspend fun likeOrUnlikePost(postId: String): Response<Boolean?> {
        val apiResponse = postApiService.likeOrUnlikePost(postId)

        return apiResponse.toResponse(apiResponse.content)
    }

    override suspend fun getPostCommentsFlow(
        postId: String
    ): Flow<PagingData<Comment>> {
        return PostCommentPager(
            postApiService,
            postId
        ).getPager().flow.map { pagingData ->
            pagingData.map { it.asComment() }
        }
    }
}