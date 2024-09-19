package com.mobi.ripple.core.domain.use_case.post.comment.reply

import com.mobi.ripple.core.domain.model.Response
import com.mobi.ripple.core.domain.repository.CommentRepository
import com.mobi.ripple.core.domain.repository.ReplyRepository


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