package com.mobi.ripple.core.domain.post.use_case.comment.reply

import com.mobi.ripple.core.domain.common.Response
import com.mobi.ripple.core.domain.post.repository.ReplyRepository

class UploadReplyUseCase(
    private val repository: ReplyRepository
) {
    suspend operator fun invoke(
        postId: String,
        commentId: String,
        replyText: String
    ): Response<Boolean?> {
        return repository.uploadReply(postId, commentId, replyText)
    }
}