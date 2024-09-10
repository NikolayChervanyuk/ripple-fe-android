package com.mobi.ripple.core.domain.use_case.post

import com.mobi.ripple.core.domain.model.Response
import com.mobi.ripple.core.domain.model.post.Post
import com.mobi.ripple.core.domain.repository.PostRepository

class GetPostUseCase(
    private val repository: PostRepository
) {
    suspend operator fun invoke(postId: String): Response<Post> {
        return repository.getPost(postId)
    }
}