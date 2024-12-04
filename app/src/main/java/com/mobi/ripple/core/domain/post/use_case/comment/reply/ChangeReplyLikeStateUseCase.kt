package com.mobi.ripple.core.domain.post.use_case.comment.reply

import com.mobi.ripple.core.domain.common.Response
import com.mobi.ripple.core.domain.post.repository.ReplyRepository


class ChangeReplyLikeStateUseCase(
    private val repository: ReplyRepository
) {
    suspend operator fun invoke(
        postId: String,
        commentId: String,
        replyId: String
    ): Response<Boolean?> {
        return repository.likeOrUnlikeReply(postId, commentId, replyId)
    }
}
