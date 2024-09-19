package com.mobi.ripple.core.domain.use_case.post.comment.reply

import androidx.paging.PagingData
import com.mobi.ripple.core.domain.model.post.Reply
import com.mobi.ripple.core.domain.repository.CommentRepository
import kotlinx.coroutines.flow.Flow

class GetCommentRepliesFlowUseCase(
    private val repository: CommentRepository
) {
    suspend operator fun invoke(postId: String, commentId: String): Flow<PagingData<Reply>> {
        return repository.getCommentRepliesFlow(postId, commentId)
    }
}