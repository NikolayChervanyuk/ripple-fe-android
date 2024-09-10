package com.mobi.ripple.core.domain.use_case.post

import com.mobi.ripple.core.domain.model.Response
import com.mobi.ripple.core.domain.repository.PostRepository


class ChangePostLikeStateUseCase(
    private val repository: PostRepository
) {
    suspend operator fun invoke(postId: String): Response<Boolean?> {
        return repository.likeOrUnlikePost(postId)
    }
}