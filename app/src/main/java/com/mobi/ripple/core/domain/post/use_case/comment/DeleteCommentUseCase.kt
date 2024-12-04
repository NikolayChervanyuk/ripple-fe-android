package com.mobi.ripple.core.domain.post.use_case.comment

import com.mobi.ripple.core.domain.common.Response
import com.mobi.ripple.core.domain.post.repository.CommentRepository

class DeleteCommentUseCase(
    private val repository: CommentRepository
) {
    suspend operator fun invoke(postId: String, commentId: String): Response<Boolean?> {
        return repository.deleteComment(postId, commentId)
    }
}