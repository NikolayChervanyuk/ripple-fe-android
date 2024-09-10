package com.mobi.ripple.core.domain.use_case.post

import androidx.paging.PagingData
import com.mobi.ripple.core.domain.model.post.Comment
import com.mobi.ripple.core.domain.repository.PostRepository
import kotlinx.coroutines.flow.Flow

class GetPostCommentsFlowUseCase(
    private val repository: PostRepository
) {
    suspend operator fun invoke(postId: String): Flow<PagingData<Comment>> {
        return repository.getPostCommentsFlow(postId)
    }
}