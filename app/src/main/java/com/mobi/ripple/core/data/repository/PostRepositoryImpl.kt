package com.mobi.ripple.core.data.repository

import androidx.paging.PagingData
import androidx.paging.map
import com.mobi.ripple.core.data.data_source.local.AppDatabase
import com.mobi.ripple.core.data.data_source.pager.post.PostCommentPager
import com.mobi.ripple.core.data.data_source.pager.posts.PostsPager
import com.mobi.ripple.core.data.data_source.remote.post.PostApiService
import com.mobi.ripple.core.domain.model.Response
import com.mobi.ripple.core.domain.model.post.Comment
import com.mobi.ripple.core.domain.model.post.Post
import com.mobi.ripple.core.domain.model.post.PostSimpleUser
import com.mobi.ripple.core.domain.repository.PostRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class PostRepositoryImpl(
    private val postApiService: PostApiService,
    private val database: AppDatabase
) : PostRepository {
    override suspend fun getPost(postId: String): Response<Post> {
        val apiResponse = postApiService.getPost(postId)

        return apiResponse.toResponse(apiResponse.content?.asPost())
    }

    override suspend fun getPostsFlow(
        startIndex: Int,
        authorId: String
    ): Flow<PagingData<Post>> {
        return PostsPager(
            apiService = postApiService,
            authorId = authorId,
            startIndex = startIndex
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