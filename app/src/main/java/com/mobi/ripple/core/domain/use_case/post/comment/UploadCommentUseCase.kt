package com.mobi.ripple.core.domain.use_case.post.comment

import com.mobi.ripple.core.domain.model.Response
import com.mobi.ripple.core.domain.model.post.UploadComment
import com.mobi.ripple.core.domain.repository.CommentRepository

class UploadCommentUseCase(
    private val repository: CommentRepository
) {
    suspend operator fun invoke(postId: String, commentText: String): Response<Boolean?> {
        return repository.uploadComment(postId, UploadComment(commentText))
    }
}