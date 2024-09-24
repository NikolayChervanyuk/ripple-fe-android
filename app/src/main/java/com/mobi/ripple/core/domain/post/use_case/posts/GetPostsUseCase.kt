package com.mobi.ripple.core.domain.post.use_case.posts

import androidx.paging.PagingData
import com.mobi.ripple.core.domain.post.model.Post
import com.mobi.ripple.core.domain.post.repository.PostRepository
import com.mobi.ripple.core.util.paging.PagedData
import com.mobi.ripple.core.util.paging.PagedDataRepository
import kotlinx.coroutines.flow.Flow

class GetPostsUseCase(
    private val repository: PostRepository
) {
    suspend operator fun invoke(startItemIndex: Int, authorId: String): Flow<PagingData<Post>> {
        return repository.getPostsFlow(startItemIndex, authorId)
    }
}