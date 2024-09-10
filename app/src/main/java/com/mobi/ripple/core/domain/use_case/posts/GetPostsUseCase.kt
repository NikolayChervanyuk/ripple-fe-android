package com.mobi.ripple.core.domain.use_case.posts

import androidx.paging.PagingData
import com.mobi.ripple.core.domain.model.post.Post
import com.mobi.ripple.core.domain.repository.PostRepository
import kotlinx.coroutines.flow.Flow

class GetPostsUseCase(
    private val repository: PostRepository
) {
    suspend operator fun invoke(startIndex: Int, authorId: String): Flow<PagingData<Post>> {
        return repository.getPostsFlow(startIndex, authorId)
    }
}