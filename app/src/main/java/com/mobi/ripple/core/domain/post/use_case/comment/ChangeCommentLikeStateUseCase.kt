package com.mobi.ripple.core.domain.post.use_case.comment

import com.mobi.ripple.core.domain.common.Response
import com.mobi.ripple.core.domain.post.repository.CommentRepository


class ChangeCommentLikeStateUseCase(
    private val repository: CommentRepository
) {
    suspend operator fun invoke(postId: String, commentId: String): Response<Boolean?> {
        return repository.likeOrUnlikeComment(postId, commentId)
    }
}