package com.mobi.ripple.core.domain.post.use_case.comment.reply

import androidx.paging.PagingData
import com.mobi.ripple.core.domain.post.model.Reply
import com.mobi.ripple.core.domain.post.repository.CommentRepository
import com.mobi.ripple.core.domain.post.repository.ReplyRepository
import kotlinx.coroutines.flow.Flow

class GetCommentRepliesFlowUseCase(
    private val repository: ReplyRepository
) {
    suspend operator fun invoke(postId: String, commentId: String): Flow<PagingData<Reply>> {
        return repository.getRepliesFlow(postId, commentId)
    }
}