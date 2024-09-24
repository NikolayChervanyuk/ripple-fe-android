package com.mobi.ripple.core.domain.post.use_case

import androidx.paging.PagingData
import com.mobi.ripple.core.domain.post.model.Comment
import com.mobi.ripple.core.domain.post.repository.PostRepository
import kotlinx.coroutines.flow.Flow

class GetPostCommentsFlowUseCase(
    private val repository: PostRepository
) {
    suspend operator fun invoke(postId: String): Flow<PagingData<Comment>> {
        return repository.getPostCommentsFlow(postId)
    }
}