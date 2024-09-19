package com.mobi.ripple.core.domain.use_case.post.comment

import com.mobi.ripple.core.domain.model.Response
import com.mobi.ripple.core.domain.repository.CommentRepository

class EditCommentUseCase(
    private val repository: CommentRepository
) {
    suspend operator fun invoke(
        postId: String,
        commentId: String,
        commentText: String
    ): Response<Boolean?> {
        return repository.editComment(postId, commentId, commentText)
    }
}