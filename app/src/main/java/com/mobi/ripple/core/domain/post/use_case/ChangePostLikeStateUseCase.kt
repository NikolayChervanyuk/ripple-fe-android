package com.mobi.ripple.core.domain.post.use_case

import com.mobi.ripple.core.domain.common.Response
import com.mobi.ripple.core.domain.post.repository.PostRepository


class ChangePostLikeStateUseCase(
    private val repository: PostRepository
) {
    suspend operator fun invoke(postId: String): Response<Boolean?> {
        return repository.likeOrUnlikePost(postId)
    }
}
