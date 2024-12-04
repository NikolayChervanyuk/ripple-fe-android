package com.mobi.ripple.core.domain.post.use_case.comment

import com.mobi.ripple.core.domain.common.Response
import com.mobi.ripple.core.domain.post.model.UploadComment
import com.mobi.ripple.core.domain.post.repository.CommentRepository

class UploadCommentUseCase(
    private val repository: CommentRepository
) {
    suspend operator fun invoke(postId: String, commentText: String): Response<Boolean?> {
        return repository.uploadComment(postId, UploadComment(commentText))
    }
}