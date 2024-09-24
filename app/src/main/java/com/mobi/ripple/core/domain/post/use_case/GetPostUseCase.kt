package com.mobi.ripple.core.domain.post.use_case

import com.mobi.ripple.core.domain.common.Response
import com.mobi.ripple.core.domain.post.model.Post
import com.mobi.ripple.core.domain.post.repository.PostRepository

class GetPostUseCase(
    private val repository: PostRepository
) {
    suspend operator fun invoke(postId: String): Response<Post> {
        return repository.getPost(postId)
    }
}