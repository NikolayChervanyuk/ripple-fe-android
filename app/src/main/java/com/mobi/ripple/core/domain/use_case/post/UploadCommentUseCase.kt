package com.mobi.ripple.core.domain.use_case.post

import com.mobi.ripple.core.domain.model.Response
import com.mobi.ripple.core.domain.model.post.UploadComment
import com.mobi.ripple.core.domain.repository.PostRepository

class UploadCommentUseCase(
    private val repository: PostRepository
) {
    suspend operator fun invoke(postId: String, commentText: String): Response<Boolean?> {
        return repository.uploadComment(postId, UploadComment(commentText))
    }
}