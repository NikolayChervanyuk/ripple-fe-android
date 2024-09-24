package com.mobi.ripple.core.domain.post.use_case.comment.reply

import com.mobi.ripple.core.domain.common.Response
import com.mobi.ripple.core.domain.post.repository.ReplyRepository


class EditReplyUseCase(
    private val repository: ReplyRepository
) {
    suspend operator fun invoke(
        postId: String,
        commentId: String,
        replyId: String,
        newReplyText: String
    ): Response<Boolean?> {
        return repository.editReply(postId, commentId, replyId, newReplyText)
    }
}